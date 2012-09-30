/*
 * Copyright (c) 2008-2012 akquinet tech@spree GmbH
 *
 * This file is part of Hibersap.
 *
 * Hibersap is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Hibersap is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Hibersap. If
 * not, see <http://www.gnu.org/licenses/>.
 */

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

import static org.fest.assertions.Assertions.assertThat;

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
        assertThat( sessionManager.getContext() ).isInstanceOf( DummyContext.class );
        assertThat( sessionManager.getConfig().getName() ).isEqualTo( "name" );
        assertThat( sessionManager.getConverterCache() ).isNotNull();
        assertThat( sessionManager.getBapiMappings() ).isNotNull();
        assertThat( sessionManager.getExecutionInterceptors() ).isNotNull();
    }

    @Test
    public void hasSameConfigurationAfterDeserialization() throws Exception
    {
        SessionManagerImpl managerRead = serializeAndDeserializeSessionManager();

        assertThat( managerRead.getConfig() ).isEqualTo( sessionManager.getConfig() );
    }

    @Test
    public void hasConverterCacheAfterDeserialization() throws Exception
    {
        SessionManagerImpl managerRead = serializeAndDeserializeSessionManager();

        assertThat( managerRead.getConverterCache() ).isNotNull();
    }

    @Test
    public void hasSameContextAfterDeserialization() throws Exception
    {
        SessionManagerImpl managerRead = serializeAndDeserializeSessionManager();

        assertThat( managerRead.getContext() ).isNotNull();
        assertThat( managerRead.getContext() ).isEqualTo( sessionManager.getContext() );
    }

    @Test
    public void hasSameBapiInterceptorsAfterDeserialization() throws Exception
    {
        SessionManagerImpl managerRead = serializeAndDeserializeSessionManager();

        final Set<BapiInterceptor> bapiInterceptors = managerRead.getBapiInterceptors();
        assertThat( bapiInterceptors ).isNotNull();
        assertThat( bapiInterceptors ).hasSize( 1 );
        assertThat( bapiInterceptors.iterator().next() ).isInstanceOf( BeanValidationInterceptor.class );
    }

    @Test
    public void hasSameExecutionInterceptorsAfterDeserialization() throws Exception
    {
        SessionManagerImpl managerRead = serializeAndDeserializeSessionManager();

        final Set<ExecutionInterceptor> executionInterceptors = managerRead.getExecutionInterceptors();
        assertThat( executionInterceptors ).isNotNull();
        assertThat( executionInterceptors ).hasSize( 1 );
        assertThat( executionInterceptors.iterator().next() ).isInstanceOf( SapErrorInterceptor.class );
    }

    @Test
    public void isClosedAfterDeserializationWhenItWasClosedBefore() throws Exception
    {
        sessionManager.close();
        SessionManagerImpl managerRead = serializeAndDeserializeSessionManager();

        assertThat( managerRead.isClosed()).isTrue();
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
