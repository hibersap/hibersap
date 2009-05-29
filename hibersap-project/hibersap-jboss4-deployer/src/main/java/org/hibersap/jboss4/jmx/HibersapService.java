package org.hibersap.jboss4.jmx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.configuration.xml.HibersapConfig;
import org.hibersap.configuration.xml.SessionManagerConfig;

public class HibersapService implements HibersapServiceMBean {

	private static final Logger LOGGER = Logger
			.getLogger(HibersapService.class);

	private final HibersapConfig config;

	private final AtomicBoolean status = new AtomicBoolean();

	private final List<AnnotationConfiguration> configs = new ArrayList<AnnotationConfiguration>();

	private ClassLoader cl;

	public HibersapService(HibersapConfig config, ClassLoader classLoader) {
		this.config = config;
		this.cl = classLoader;
	}

	public void start() {
		if (status.compareAndSet(false, true)) {
			LOGGER.info("Start");

			Iterator<SessionManagerConfig> i = config.getSessionManagers()
					.iterator();

			while (i.hasNext()) {
				SessionManagerConfig sessionManagerConfig = i.next();

				AnnotationConfiguration annotationConfiguration = new AnnotationConfiguration(
						sessionManagerConfig);

				configs.add(annotationConfiguration);

				register(configs);
			}
		}
	}

	private void register(List<AnnotationConfiguration> configs2) {
		try {
			InitialContext ic = new InitialContext();

			try {
				ic.lookup("java:hibersap");
			} catch (NameNotFoundException e) {
				ic.createSubcontext("java:hibersap");
			}

			Context context = (Context) ic.lookup("java:hibersap");

			ClassLoader old = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(cl);
			try {
				Iterator<AnnotationConfiguration> i = configs2.iterator();

				while (i.hasNext()) {
					AnnotationConfiguration configuration = i.next();

					context.bind(configuration.getSessionManagerConfig()
							.getName(), configuration.buildSessionManager());
				}
			} finally {
				Thread.currentThread().setContextClassLoader(old);
				ic.close();
			}
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	public void stop() {
		if (status.compareAndSet(true, false)) {
			LOGGER.info("Stop");

			unregister(configs);

			configs.clear();
		}
	}

	private void unregister(List<AnnotationConfiguration> configs2) {
		try {
			InitialContext ic = new InitialContext();

			try {
				Iterator<AnnotationConfiguration> i = configs2.iterator();

				while (i.hasNext()) {
					AnnotationConfiguration configuration = i.next();

					ic
							.unbind("java:hibersap/"
									+ configuration.getSessionManagerConfig()
											.getName());
				}
			} finally {
				ic.close();
			}
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	public String getStatus() {
		return status.get() ? "STARTED" : "STOPPED";
	}

	public String viewConfiguration() {
		StringBuilder builder = new StringBuilder();

		Iterator<SessionManagerConfig> i = config.getSessionManagers()
				.iterator();
		
		while (i.hasNext()) {
			SessionManagerConfig next = i.next();
			builder.append(next.toString());
		}
		
		return builder.toString();
	}
}
