package org.hibersap.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.session.Context;
import org.junit.Test;

public class ContextFactoryTest
{
    @Test
    public void testInitializesContextClass()
        throws Exception
    {
        SessionManagerConfig config = new SessionManagerConfig( "Test" ).setContext( DummyContext.class.getName() );
        Context context = ContextFactory.create( config );

        assertNotNull( context );
        assertEquals( DummyContext.class, context.getClass() );
    }
}
