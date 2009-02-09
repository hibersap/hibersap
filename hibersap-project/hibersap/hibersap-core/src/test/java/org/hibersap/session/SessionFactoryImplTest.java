package org.hibersap.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.Properties;

import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.configuration.DummyContext;
import org.hibersap.configuration.HibersapProperties;
import org.junit.Test;

public class SessionFactoryImplTest
{
    @Test
    public void initialize()
        throws Exception
    {
        Properties properties = new Properties();
        properties.setProperty( HibersapProperties.SESSION_FACTORY_NAME, "name" );
        properties.setProperty( HibersapProperties.CONTEXT_CLASS, DummyContext.class.getName() );

        AnnotationConfiguration config = new AnnotationConfiguration();
        config.setProperties( properties );
        SessionFactoryImpl factory = (SessionFactoryImpl) config.buildSessionFactory();

        assertSame( DummyContext.class, factory.getSettings().getContext().getClass() );
        assertEquals( "name", factory.getProperties().getProperty( HibersapProperties.SESSION_FACTORY_NAME ) );
        assertNotNull( factory.getConverterCache() );
        assertNotNull( factory.getBapiMappings() );
        assertNotNull( factory.getInterceptors() );
    }
}
