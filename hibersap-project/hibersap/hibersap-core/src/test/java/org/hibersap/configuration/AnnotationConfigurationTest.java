package org.hibersap.configuration;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hibersap.ConfigurationException;
import org.hibersap.MappingException;
import org.hibersap.bapi.BapiTransactionCommit;
import org.hibersap.interceptor.ExecutionInterceptor;
import org.hibersap.interceptor.impl.SapErrorInterceptor;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.session.SessionManager;
import org.hibersap.session.SessionManagerImpl;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class AnnotationConfigurationTest
{
    private static final Class<BapiTransactionCommit> BAPI_CLASS = BapiTransactionCommit.class;

    private AnnotationConfiguration configuration = new AnnotationConfiguration();

    @Test
    public void addsSapErrorInterceptorToSessionManagerPerDefault()
    {
        SessionManagerImpl sessionManager = configureAndBuildSessionManager();

        final Set<ExecutionInterceptor> interceptors = sessionManager.getExecutionInterceptors();

        assertThat( interceptors, hasInterceptorOfType( SapErrorInterceptor.class ) );
    }

    private Matcher<Iterable<? super ExecutionInterceptor>> hasInterceptorOfType(
            Class<? extends ExecutionInterceptor> interceptorClass )
    {
        return hasItem( CoreMatchers.<ExecutionInterceptor>instanceOf( interceptorClass ) );
    }

    @Test
    public void addsAnnotatedClassToSessionManager()
    {
        configuration.getSessionManagerConfig().addAnnotatedClass( BAPI_CLASS );
        SessionManagerImpl sessionManager = configureAndBuildSessionManager();

        Map<Class<?>, BapiMapping> bapiMappings = sessionManager.getBapiMappings();
        assertThat( bapiMappings.size(), equalTo( 1 ) );
        assertThat( bapiMappings.get( BAPI_CLASS ), notNullValue() );
    }

    @Test( expected = MappingException.class )
    public void throwsMappingExceptionWhenClassWasAddedThatIsNotAnnotated()
    {
        configuration.getSessionManagerConfig().addAnnotatedClass( Object.class );
        configureAndBuildSessionManager();
    }

    @Test( expected = ConfigurationException.class )
    public void throwsConfigurationExceptionWhenBapiClassWasAddedThatIsNotAnnotated()
    {
        configuration.getSessionManagerConfig().getAnnotatedClasses( ).add( "does.not.Exist" );
        configureAndBuildSessionManager();
    }

    private SessionManagerImpl configureAndBuildSessionManager()
    {
        configuration.getSessionManagerConfig().setContext( DummyContext.class.getName() );
        SessionManager sessionManager = configuration.buildSessionManager();
        return ( SessionManagerImpl ) sessionManager;
    }
}
