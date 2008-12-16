package org.hibersap.examples.jee;

import javax.resource.ResourceException;
import javax.resource.cci.MappedRecord;

public interface RAConnectorService
{
    String RA_JNDI_NAME = "java:/eis/sap/A12";

    String LOCAL_JNDI_NAME = "hibersap/jee/RAConnector/local";

    String JNDI_NAME = "hibersap/jee/RAConnector/remote";

    MappedRecord getFlightList( final MappedRecord inputRecord )
        throws ResourceException;
}
