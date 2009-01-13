package org.hibersap.jmx;

import org.jboss.beans.metadata.api.annotations.Inject;
import org.jboss.beans.metadata.api.model.FromContext;
import org.jboss.kernel.plugins.bootstrap.basic.KernelConstants;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.logging.Logger;
import org.jboss.virtual.VirtualFile;

public class HiberSap implements HiberSapMBean {

	private static final Logger LOG = Logger.getLogger(HiberSap.class);

	private Object beanName;

	private KernelController controller;

	@SuppressWarnings("unused")
	private final VirtualFile root;

	private String sessionFactoryName;

	public HiberSap(final VirtualFile root) {
		this.root = root;
	}

	public String getSessionFactoryName() {
		return sessionFactoryName;
	}

	@Inject(fromContext = FromContext.NAME)
	public void setBeanName(final Object beanName) {
		this.beanName = beanName;
	}

	@Inject(bean = KernelConstants.KERNEL_CONTROLLER_NAME)
	public void setController(final KernelController controller) {
		this.controller = controller;
	}

	public void setSessionFactoryName(final String sessionFactoryName) {
		this.sessionFactoryName = sessionFactoryName;
	}

	public void start() {
		LOG.debug("Starting HiberSap");
		LOG.trace("beanName=" + beanName + " controller=" + controller);

	}

	public void stop() {
		LOG.debug("Stopping HiberSap");
	}

}
