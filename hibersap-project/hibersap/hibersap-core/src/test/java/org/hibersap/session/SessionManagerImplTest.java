package org.hibersap.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.configuration.DummyContext;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.conversion.BooleanConverter;
import org.hibersap.interceptor.ExecutionInterceptor;
import org.junit.Before;
import org.junit.Test;

public class SessionManagerImplTest
{
    private SessionManagerImpl manager;

    @Before
    public void setUp()
    {
        SessionManagerConfig smConfig = new SessionManagerConfig( "name" ).setContext( DummyContext.class.getName() );

        AnnotationConfiguration config = new AnnotationConfiguration();
        config.setSessionManagerConfig( smConfig );

        manager = (SessionManagerImpl) config.buildSessionManager();
    }

    @Test
    public void testInitialize()
        throws Exception
    {
        assertSame( DummyContext.class, manager.getSettings().getContext().getClass() );
        assertEquals( "name", manager.getConfig().getName() );
        assertNotNull( manager.getConverterCache() );
        assertNotNull( manager.getBapiMappings() );
        assertNotNull( manager.getExecutionInterceptors() );
    }

    @Test
    public void testSerialize()
        throws Exception
    {
        // add a Converter
        manager.getConverterCache().getConverter( BooleanConverter.class );

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream( baos );
        out.writeObject( manager );
        out.close();

        ByteArrayInputStream bain = new ByteArrayInputStream( baos.toByteArray() );
        ObjectInputStream in = new ObjectInputStream( bain );
        SessionManagerImpl managerRead = (SessionManagerImpl) in.readObject();
        in.close();

        assertNotSame( manager, managerRead );
        assertEquals( manager.getConfig(), managerRead.getConfig() );
        assertEquals( manager.getSettings(), managerRead.getSettings() );
        assertEquals( manager.getConverterCache(), managerRead.getConverterCache() );

        assertEquals( 1, manager.getExecutionInterceptors().size() );
        assertEquals( 1, managerRead.getExecutionInterceptors().size() );
        ExecutionInterceptor interceptor = manager.getExecutionInterceptors().iterator().next();
        ExecutionInterceptor interceptorRead = managerRead.getExecutionInterceptors().iterator().next();
        assertEquals( interceptor.getClass(), interceptorRead.getClass() );
    }
}
