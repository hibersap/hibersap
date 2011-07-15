package org.hibersap.jbossdeployer;

import org.hibersap.jbossdeployer.metadata.HiberSapMetaData;
import org.jboss.deployers.vfs.spi.deployer.SchemaResolverDeployer;
import org.jboss.logging.Logger;

public class HiberSapParsingDeployer extends
		SchemaResolverDeployer<HiberSapMetaData> {
	private static final Logger LOG = Logger
			.getLogger(HiberSapParsingDeployer.class);

	public HiberSapParsingDeployer() {
		super(HiberSapMetaData.class);
		setName("hibersap.xml");

		setRegisterWithJBossXB(true);
		setUseSchemaValidation(false);
		setUseValidation(false);

		LOG.trace("instance created");
	}

}
