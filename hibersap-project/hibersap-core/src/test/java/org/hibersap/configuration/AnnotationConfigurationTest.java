package org.hibersap.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.hibersap.bapi.BapiTransactionCommit;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.session.SessionFactory;
import org.junit.Test;

public class AnnotationConfigurationTest
{
    private static final Class<BapiTransactionCommit> BAPI_CLASS = BapiTransactionCommit.class;

    private AnnotationConfiguration config = new AnnotationConfiguration();

    @Test
    public void addsStandardInterceptors()
    {
        SessionFactory sessionFactory = configureAndBuildSessionFactory();
        assertEquals( 1, sessionFactory.getInterceptors().size() );
    }

    @Test
    public void addsAnnotatedClass()
    {
        config.addAnnotatedClass( BAPI_CLASS );
        SessionFactory sessionFactory = configureAndBuildSessionFactory();

        Map<Class<?>, BapiMapping> bapiMappings = sessionFactory.getBapiMappings();
        assertEquals( 1, bapiMappings.size() );
        assertNotNull( bapiMappings.get( BAPI_CLASS ) );
    }

    private SessionFactory configureAndBuildSessionFactory()
    {
        config.setProperty( Environment.SESSION_FACTORY_NAME, "Test" );
        config.setProperty( Environment.CONTEXT_CLASS, DummyContext.class.getName() );
        SessionFactory sessionFactory = config.buildSessionFactory();
        return sessionFactory;
    }
}
