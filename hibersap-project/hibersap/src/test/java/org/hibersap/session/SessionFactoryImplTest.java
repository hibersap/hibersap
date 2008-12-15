package org.hibersap.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.Properties;

import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.configuration.DummyContext;
import org.hibersap.configuration.Environment;
import org.junit.Test;

public class SessionFactoryImplTest
{
    @Test
    public void initialize()
        throws Exception
    {
        Properties properties = new Properties();
        properties.setProperty( Environment.SESSION_FACTORY_NAME, "name" );
        properties.setProperty( Environment.CONTEXT_CLASS, DummyContext.class.getName() );

        AnnotationConfiguration config = new AnnotationConfiguration();
        config.setProperties( properties );
        SessionFactory factory = config.buildSessionFactory();

        assertSame( DummyContext.class, factory.getSettings().getContext().getClass() );
        assertEquals( "name", factory.getProperties().getProperty( Environment.SESSION_FACTORY_NAME ) );
        assertNotNull( factory.getConverterCache() );
        assertNotNull( factory.getBapiMappings() );
        assertNotNull( factory.getInterceptors() );
    }
}
