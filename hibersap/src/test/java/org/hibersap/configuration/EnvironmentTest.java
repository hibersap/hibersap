package org.hibersap.configuration;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.junit.Test;

public class EnvironmentTest
{
    @Test
    public void testProperties()
        throws Exception
    {
        final Properties properties = Environment.getProperties();
        assertEquals( "TEST", properties.getProperty( Environment.SESSION_FACTORY_NAME ) );
        assertEquals( "127.0.0.1", properties.getProperty( "hibersap.jco.client.ashost" ) );
    }
}
