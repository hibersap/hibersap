package org.hibersap.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.configuration.DummyContext;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.junit.Test;

public class SessionManagerImplTest
{
    @Test
    public void initialize()
        throws Exception
    {
        SessionManagerConfig smConfig = new SessionManagerConfig( "name" ).setContext( DummyContext.class.getName() );

        AnnotationConfiguration config = new AnnotationConfiguration();
        config.setSessionManagerConfig( smConfig );

        SessionManagerImpl manager = (SessionManagerImpl) config.buildSessionManager();

        assertSame( DummyContext.class, manager.getSettings().getContext().getClass() );
        assertEquals( "name", manager.getConfig().getName() );
        assertNotNull( manager.getConverterCache() );
        assertNotNull( manager.getBapiMappings() );
        assertNotNull( manager.getInterceptors() );
    }
}
