package org.hibersap.examples.jee;

import javax.resource.ResourceException;

public interface RAConnectorService
{
    String RA_JNDI_NAME = "java:/eis/sap/A12";

    String LOCAL_JNDI_NAME = "hibersap/jee/RAConnector/local";

    String JNDI_NAME = "hibersap/jee/RAConnector/remote";

    boolean adapterExists()
        throws ResourceException;
}
