package org.hibersap.session;

import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.configuration.DummyContext;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.conversion.BooleanConverter;
import org.hibersap.interceptor.BapiInterceptor;
import org.hibersap.interceptor.ExecutionInterceptor;
import org.hibersap.interceptor.impl.SapErrorInterceptor;
import org.hibersap.validation.BeanValidationInterceptor;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class SessionManagerImplTest
{
    private SessionManagerImpl sessionManager;

    @Before
    public void createSessionManager()
    {
        SessionManagerConfig smConfig = new SessionManagerConfig( "name" ).setContext( DummyContext.class.getName() );

        AnnotationConfiguration config = new AnnotationConfiguration( smConfig );

        sessionManager = ( SessionManagerImpl ) config.buildSessionManager();
    }

    @Test
    public void hasConfiguredAndStandardValuesWhenCreated() throws Exception
    {
        assertThat( sessionManager.getContext(), instanceOf( DummyContext.class ) );
        assertThat( sessionManager.getConfig().getName(), equalTo( "name" ) );
        assertThat( sessionManager.getConverterCache(), notNullValue() );
        assertThat( sessionManager.getBapiMappings(), notNullValue() );
        assertThat( sessionManager.getExecutionInterceptors(), notNullValue() );
    }

    @Test
    public void hasSameConfigurationAfterDeserialization() throws Exception
    {
        SessionManagerImpl managerRead = serializeAndDeserializeSessionManager();

        assertThat( managerRead.getConfig(), equalTo( sessionManager.getConfig() ) );
    }

    @Test
    public void hasConverterCacheAfterDeserialization() throws Exception
    {
        SessionManagerImpl managerRead = serializeAndDeserializeSessionManager();

        assertThat( managerRead.getConverterCache(), notNullValue() );
    }

    @Test
    public void hasSameContextAfterDeserialization() throws Exception
    {
        SessionManagerImpl managerRead = serializeAndDeserializeSessionManager();

        assertThat( managerRead.getContext(), notNullValue() );
        assertThat( managerRead.getContext(), equalTo( sessionManager.getContext() ) );
    }

    @Test
    public void hasSameBapiInterceptorsAfterDeserialization() throws Exception
    {
        SessionManagerImpl managerRead = serializeAndDeserializeSessionManager();

        final Set<BapiInterceptor> bapiInterceptors = managerRead.getBapiInterceptors();
        assertThat( bapiInterceptors, notNullValue() );
        assertThat( bapiInterceptors.size(), is( 1 ) );
        assertThat( bapiInterceptors.iterator().next(), instanceOf( BeanValidationInterceptor.class ) );
    }

    @Test
    public void hasSameExecutionInterceptorsAfterDeserialization() throws Exception
    {
        SessionManagerImpl managerRead = serializeAndDeserializeSessionManager();

        final Set<ExecutionInterceptor> executionInterceptors = managerRead.getExecutionInterceptors();
        assertThat( executionInterceptors, notNullValue() );
        assertThat( executionInterceptors.size(), is( 1 ) );
        assertThat( executionInterceptors.iterator().next(), instanceOf( SapErrorInterceptor.class ) );
    }

    @Test
    public void isClosedAfterDeserializationWhenItWasClosedBefore() throws Exception
    {
        sessionManager.close();
        SessionManagerImpl managerRead = serializeAndDeserializeSessionManager();

        assertThat( managerRead.isClosed(), is( true ) );
    }

    private SessionManagerImpl serializeAndDeserializeSessionManager() throws IOException, ClassNotFoundException
    {
        // Converter gets created lazily on first access
        if ( !sessionManager.isClosed() )
        {
            sessionManager.getConverterCache().getConverter( BooleanConverter.class );
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream( baos );
        out.writeObject( sessionManager );
        out.close();

        ByteArrayInputStream bain = new ByteArrayInputStream( baos.toByteArray() );
        ObjectInputStream in = new ObjectInputStream( bain );
        SessionManagerImpl managerRead = ( SessionManagerImpl ) in.readObject();
        in.close();
        return managerRead;
    }
}
