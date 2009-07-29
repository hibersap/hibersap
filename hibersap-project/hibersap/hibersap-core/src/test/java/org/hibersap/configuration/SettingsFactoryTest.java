package org.hibersap.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.session.Context;
import org.junit.Test;

public class SettingsFactoryTest
{
    @Test
    public void testInitializesContextClass()
        throws Exception
    {
        SessionManagerConfig config = new SessionManagerConfig( "Test" ).setContext( DummyContext.class.getName() );

        Settings settings = SettingsFactory.create( config );

        Context context = settings.getContext();
        assertNotNull( context );
        assertEquals( DummyContext.class, context.getClass() );
    }
}
