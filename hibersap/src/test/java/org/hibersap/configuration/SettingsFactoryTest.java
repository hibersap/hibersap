package org.hibersap.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Properties;

import org.hibersap.session.Context;
import org.junit.Test;

public class SettingsFactoryTest
{
    @Test
    public void initializesContextClass()
        throws Exception
    {
        Properties properties = new Properties();
        properties.setProperty( Environment.SESSION_FACTORY_NAME, "Test" );
        properties.setProperty( Environment.CONTEXT_CLASS, DummyContext.class.getName() );
        Settings settings = SettingsFactory.create( properties );

        Context context = settings.getContext();
        assertNotNull( context );
        assertEquals( DummyContext.class, context.getClass() );
    }
}
