package org.hibersap.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.hibersap.bapi.BapiTransactionCommit;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.session.SessionFactory;
import org.hibersap.session.SessionFactoryImpl;
import org.junit.Test;

public class AnnotationConfigurationTest
{
    private static final Class<BapiTransactionCommit> BAPI_CLASS = BapiTransactionCommit.class;

    private AnnotationConfiguration configuration = new AnnotationConfiguration();

    @Test
    public void addsStandardInterceptors()
    {
        SessionFactoryImpl sessionFactory = configureAndBuildSessionFactory();
        assertEquals( 1, sessionFactory.getInterceptors().size() );
    }

    @Test
    public void addsAnnotatedClass()
    {
        configuration.addAnnotatedClass( BAPI_CLASS );
        SessionFactoryImpl sessionFactory = configureAndBuildSessionFactory();

        Map<Class<?>, BapiMapping> bapiMappings = sessionFactory.getBapiMappings();
        assertEquals( 1, bapiMappings.size() );
        assertNotNull( bapiMappings.get( BAPI_CLASS ) );
    }

    private SessionFactoryImpl configureAndBuildSessionFactory()
    {
        configuration.getConfig().setContext( DummyContext.class.getName() );
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        return (SessionFactoryImpl) sessionFactory;
    }
}
