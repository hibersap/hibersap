package org.hibersap.jboss4.deployer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.management.ObjectName;

import org.apache.log4j.Logger;
import org.hibersap.configuration.xml.HibersapConfig;
import org.hibersap.configuration.xml.HibersapJaxbXmlParser;
import org.hibersap.jboss4.jmx.HibersapService;
import org.jboss.deployment.DeploymentException;
import org.jboss.deployment.DeploymentInfo;
import org.jboss.deployment.SubDeployer;
import org.jboss.deployment.SubDeployerSupport;
import org.jboss.mx.util.MBeanProxyExt;
import org.jboss.mx.util.ObjectNameConverter;
import org.jboss.system.ServiceControllerMBean;

public class HibersapDeployer extends SubDeployerSupport implements
		SubDeployer, HibersapDeployerMBean {

	private static final Logger LOGGER = Logger
			.getLogger(HibersapDeployer.class);

	private ServiceControllerMBean serviceController;

	private SubDeployer thisProxy;

	public HibersapDeployer() {
		setSuffixes(new String[] { ".sap", ".jar" });

		// before the jar deployer!
		setRelativeOrder(350);
	}

	@Override
	protected void startService() throws Exception {
		serviceController = (ServiceControllerMBean) MBeanProxyExt.create(
				ServiceControllerMBean.class,
				ServiceControllerMBean.OBJECT_NAME, server);
		thisProxy = (SubDeployer) MBeanProxyExt.create(SubDeployer.class, super
				.getServiceName(), super.getServer());
		mainDeployer.addDeployer(thisProxy);
	}

	@Override
	protected void stopService() throws Exception {
		mainDeployer.removeDeployer(thisProxy);
		serviceController = null;
		thisProxy = null;
	}

	/**
	 * copied from EJB3Deployer
	 */
	public static boolean hasFile(DeploymentInfo di, String filePath) {
		String urlStr = di.url.getFile();
		try {
			URL dd = di.localCl.findResource(filePath);
			if (dd != null) {

				// If the DD url is not a subset of the urlStr then this is
				// coming
				// from a jar referenced by the deployment jar manifest and the
				// this deployment jar it should not be treated as persistence
				if (di.localUrl != null) {
					urlStr = di.localUrl.toString();
				}

				String ddStr = dd.toString();
				if (ddStr.indexOf(urlStr) >= 0) {
					return true;
				}
			}
		} catch (Exception ignore) {
		}
		return false;
	}

	@Override
	public boolean accepts(DeploymentInfo di) {

		System.out.println("accept " + di);

		String url = di.url.getFile();

		System.out.println("accept url " + url);

		boolean hasFile = hasFile(di, "META-INF/hibersap.xml");

		System.out.println("accept hasFile " + hasFile);

		LOGGER.debug("Deployer accepting file " + url + " => " + hasFile);
		return hasFile;
	}

	@Override
	public void init(DeploymentInfo di) throws DeploymentException {

		System.out.println("init " + di);

		try {
			if (di.url.getProtocol().equalsIgnoreCase("file")) {
				File file = new File(di.url.getFile());

				if (!file.isDirectory()) {
					// If not directory we watch the package
					di.watch = di.url;
				} else {
					// If directory we watch the xml files
					di.watch = new URL(di.url, "META-INF/hibersap.xml");
				}
			} else {
				// We watch the top only, no directory support
				di.watch = di.url;
			}
		} catch (Exception e) {
			if (e instanceof DeploymentException) {
				throw (DeploymentException) e;
			}
			throw new DeploymentException("failed to initialize", e);
		}

		System.out.println("init " + di);

		// invoke super-class initialization
		super.init(di);
	}

	@Override
	public void create(DeploymentInfo di) throws DeploymentException {

		System.out.println("start " + di);

		URL url = (di.localUrl != null ? di.localUrl : di.url);

		URL dd = di.localCl.findResource("META-INF/hibersap.xml");

		System.out.println("init: " + url);

		try {
			InputStream in = dd.openStream();
			try {
				HibersapConfig config = new HibersapJaxbXmlParser()
						.parseResource(in, dd.toString());
				LOGGER.info("deploy " + url + " " + config);

				HibersapService hibersapService = new HibersapService(config, di.localCl);

				String name = "org.hibersap:service=HibersapService,module="
						+ di.shortName;
				ObjectName jmxName = ObjectNameConverter.convert(name);
				di.getServer().registerMBean(hibersapService, jmxName);
				di.deployedObject = jmxName;
				serviceController.create(di.deployedObject);
			} catch (Exception e) {
				throw new DeploymentException(e);

			} finally {
				in.close();
			}
		} catch (IOException e1) {
			throw new DeploymentException(e1);
		}

		super.create(di);
	}
	
	@Override
	public void start(DeploymentInfo di) throws DeploymentException {
		try {
			serviceController.start(di.deployedObject);
		} catch (Exception e) {
			throw new DeploymentException(e);
		}
		
		super.stop(di);
	}

	@Override
	public void stop(DeploymentInfo di) throws DeploymentException {
		try {
			serviceController.stop(di.deployedObject);
		} catch (Exception e) {
			throw new DeploymentException(e);
		}

		super.stop(di);
	}

	@Override
	public void destroy(DeploymentInfo di) throws DeploymentException {
		try {
			serviceController.destroy(di.deployedObject);
			serviceController.remove(di.deployedObject);
		} catch (Exception e) {
			throw new DeploymentException(e);
		}

		super.destroy(di);
	}
}
