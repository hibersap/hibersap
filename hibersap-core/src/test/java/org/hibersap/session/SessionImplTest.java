/*
 * Copyright (c) 2008-2014 akquinet tech@spree GmbH
 *
 * This file is part of Hibersap.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hibersap.session;

import org.hibersap.HibersapException;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.conversion.ConverterCache;
import org.hibersap.execution.Connection;
import org.hibersap.interceptor.BapiInterceptor;
import org.hibersap.interceptor.ExecutionInterceptor;
import org.hibersap.mapping.model.BapiMapping;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

@SuppressWarnings( "unchecked" )
public class SessionImplTest {

    private final BapiInterceptor bapiInterceptor = createStrictMock( BapiInterceptor.class );
    private final ExecutionInterceptor executionInterceptor = createStrictMock( ExecutionInterceptor.class );

    private final SessionImpl session = new SessionImpl( new SessionManagerStub() );

    @Test
    public void bapiInterceptorsFromConfigurationAreCalledWhenExecutingFunction() {
        final Object bapiObject = new Object();
        bapiInterceptor.beforeExecution( bapiObject );
        bapiInterceptor.afterExecution( bapiObject );
        replay( bapiInterceptor );

        session.execute( bapiObject );

        verify( bapiInterceptor );
    }

    @Test
    public void bapiInterceptorsThatAreAddedAtRuntimeAreCalledWhenExecutingFunction() {
        BapiInterceptor myInterceptor = createMock( BapiInterceptor.class );

        final Object bapiObject = new Object();
        myInterceptor.beforeExecution( bapiObject );
        myInterceptor.afterExecution( bapiObject );
        replay( myInterceptor );

        session.addBapiInterceptor( myInterceptor );
        session.execute( bapiObject );

        verify( myInterceptor );
    }

    @Test
    public void executionInterceptorsFromConfigurationAreCalledWhenExecutingFunction() {
        final Object bapiObject = new Object();
        executionInterceptor.beforeExecution( (BapiMapping) anyObject(), (Map<String, Object>) anyObject() );
        executionInterceptor.afterExecution( (BapiMapping) anyObject(), (Map<String, Object>) anyObject() );
        replay( executionInterceptor );

        session.execute( bapiObject );

        verify( executionInterceptor );
    }

    @Test
    public void executionInterceptorsThatAreAddedAtRuntimeAreCalledWhenExecutingFunction() {
        ExecutionInterceptor myInterceptor = createMock( ExecutionInterceptor.class );

        final Object bapiObject = new Object();
        myInterceptor.beforeExecution( (BapiMapping) anyObject(), (Map<String, Object>) anyObject() );
        myInterceptor.afterExecution( (BapiMapping) anyObject(), (Map<String, Object>) anyObject() );
        replay( myInterceptor );

        session.addExecutionInterceptor( myInterceptor );
        session.execute( bapiObject );

        verify( myInterceptor );
    }

    private class SessionManagerStub implements SessionManagerImplementor {

        public Map<Class<?>, BapiMapping> getBapiMappings() {
            final HashMap<Class<?>, BapiMapping> mappings = new HashMap<Class<?>, BapiMapping>();
            mappings.put( Object.class, new BapiMapping( Object.class, "BAPI_NAME", null ) );
            return mappings;
        }

        public ConverterCache getConverterCache() {
            return new ConverterCache();
        }

        public SessionManagerConfig getConfig() {
            return new SessionManagerConfig();
        }

        public Context getContext() {
            return new ContextStub();
        }

        public Session openSession() {
            return null;
        }

        public Set<ExecutionInterceptor> getExecutionInterceptors() {

            return Collections.singleton( executionInterceptor );
        }

        public Set<BapiInterceptor> getBapiInterceptors() {
            return Collections.singleton( bapiInterceptor );
        }
    }

    private class ContextStub implements Context {

        public void configure( SessionManagerConfig config ) throws HibersapException {
        }

        public void close() {
        }

        public Connection getConnection() {
            return new ConnectionStub();
        }
    }

    private class ConnectionStub implements Connection {

        public void setCredentials( Credentials credentials ) {
        }

        public Transaction beginTransaction( SessionImplementor session ) {
            return null;
        }

        public Transaction getTransaction() {
            return null;
        }

        public void execute( String bapiName, Map<String, Object> functionMap ) {
        }

        public void close() {
        }
    }
}