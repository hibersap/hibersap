package org.hibersap.configuration;

import org.hibersap.bapi.BapiTransactionCommit;
import org.hibersap.session.SapErrorInterceptor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.hibersap.configuration.xml.HibersapConfig;
import org.hibersap.configuration.xml.Property;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.junit.Test;

public class EnvironmentTest
{
    @Test
    public void testConfigurationFromXML()
        throws Exception
    {
        final HibersapConfig config = Environment.readXMLProperties( Environment.HIBERSAP_XML_FILE );
        SessionManagerConfig sf = config.getSessionManagers().get( 0 );
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

        final Set<String> classes = sf.getAnnotatedClasses();
        assertEquals(1, classes.size());
        assertTrue(classes.contains(BapiTransactionCommit.class.getName()));

        final Set<String> interceptorClasses = sf.getInterceptorClasses();
        assertEquals(1, interceptorClasses.size());
        assertTrue(interceptorClasses.contains(SapErrorInterceptor.class.getName()));
    }
}
