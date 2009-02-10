package org.hibersap.configuration;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.hibersap.configuration.xml.HibersapConfig;
import org.hibersap.configuration.xml.Property;
import org.hibersap.configuration.xml.SessionFactoryConfig;
import org.junit.Test;

public class EnvironmentTest
{
    @Test
    public void testConfigurationFromXML()
        throws Exception
    {
        final HibersapConfig config = Environment.readXMLProperties( Environment.HIBERSAP_XML_FILE );
        SessionFactoryConfig sf = config.getSessionFactories().get( 0 );
        assertEquals( "TEST", sf.getName() );
        assertEquals( "org.hibersap.execution.jco.JCoContext", sf.getContext() );
        assertEquals( "java:/eis/sap/A12", sf.getJcaConnectionFactory() );

        Set<Property> properties = sf.getProperties();
        for ( Property property : properties )
        {
            if ( "jco.client.ashost".equals( property.getName() ) )
                assertEquals( "127.0.0.1", property.getValue() );
            if ( "jco.client.client".equals( property.getName() ) )
                assertEquals( "42", property.getValue() );
        }
    }
}
