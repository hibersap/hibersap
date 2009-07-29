package org.hibersap.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.hibersap.bapi.BapiTransactionCommit;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.session.SessionManager;
import org.hibersap.session.SessionManagerImpl;
import org.junit.Test;

public class AnnotationConfigurationTest
{
    private static final Class<BapiTransactionCommit> BAPI_CLASS = BapiTransactionCommit.class;

    private AnnotationConfiguration configuration = new AnnotationConfiguration();

    @Test
    public void testAddsStandardInterceptors()
    {
        SessionManagerImpl sessionManager = configureAndBuildSessionManager();
        assertEquals( 1, sessionManager.getInterceptors().size() );
    }

    @Test
    public void testAddsAnnotatedClass()
    {
        configuration.getSessionManagerConfig().addAnnotatedClass( BAPI_CLASS );
        SessionManagerImpl sessionManager = configureAndBuildSessionManager();

        Map<Class<?>, BapiMapping> bapiMappings = sessionManager.getBapiMappings();
        assertEquals( 1, bapiMappings.size() );
        assertNotNull( bapiMappings.get( BAPI_CLASS ) );
    }

    private SessionManagerImpl configureAndBuildSessionManager()
    {
        configuration.getSessionManagerConfig().setContext( DummyContext.class.getName() );
        SessionManager sessionManager = configuration.buildSessionManager();
        return (SessionManagerImpl) sessionManager;
    }
}
