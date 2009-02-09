package org.hibersap.configuration;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.junit.Test;

public class EnvironmentTest
{
    @Test
    public void testConfigurationFromProperties()
        throws Exception
    {
        final Properties properties = Environment.readStringProperties( Environment.HIBERSAP_PROPERTIES_FILE );
        assertEquals( "TEST", properties.getProperty( HibersapProperties.SESSION_FACTORY_NAME ) );
        assertEquals( "127.0.0.1", properties.getProperty( "jco.client.ashost" ) );
    }

    @Test
    public void testConfigurationFromXML()
        throws Exception
    {
        final Properties properties = Environment.readXMLProperties( Environment.HIBERSAP_XML_FILE );
        assertEquals( "TEST", properties.getProperty( HibersapProperties.SESSION_FACTORY_NAME ) );
        assertEquals( "org.hibersap.execution.jco.JCoContext", properties
            .getProperty( HibersapProperties.CONTEXT_CLASS ) );
        assertEquals( "42", properties.getProperty( "jco.client.client" ) );
        assertEquals( "127.0.0.1", properties.getProperty( "jco.client.ashost" ) );
        assertEquals( "java:/eis/sap/A12", properties.getProperty( HibersapProperties.JCA_CONNECTION_FACTORY ) );
    }
}
