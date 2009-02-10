package org.hibersap.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.configuration.DummyContext;
import org.hibersap.configuration.xml.SessionFactoryConfig;
import org.junit.Test;

public class SessionFactoryImplTest
{
    @Test
    public void initialize()
        throws Exception
    {
        SessionFactoryConfig sfConfig = new SessionFactoryConfig( "name" ).setContext( DummyContext.class.getName() );

        AnnotationConfiguration config = new AnnotationConfiguration();
        config.setConfig( sfConfig );

        SessionFactoryImpl factory = (SessionFactoryImpl) config.buildSessionFactory();

        assertSame( DummyContext.class, factory.getSettings().getContext().getClass() );
        assertEquals( "name", factory.getConfig().getName() );
        assertNotNull( factory.getConverterCache() );
        assertNotNull( factory.getBapiMappings() );
        assertNotNull( factory.getInterceptors() );
    }
}
