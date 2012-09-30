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
public class SessionImplTest
{
    private final BapiInterceptor bapiInterceptor = createStrictMock( BapiInterceptor.class );
    private final ExecutionInterceptor executionInterceptor = createStrictMock( ExecutionInterceptor.class );

    private final SessionImpl session = new SessionImpl( new SessionManagerStub() );

    @Test
    public void bapiInterceptorsFromConfigurationAreCalledWhenExecutingFunction()
    {
        final Object bapiObject = new Object();
        bapiInterceptor.beforeExecution( bapiObject );
        bapiInterceptor.afterExecution( bapiObject );
        replay( bapiInterceptor );

        session.execute( bapiObject );

        verify( bapiInterceptor );
    }

    @Test
    public void bapiInterceptorsThatAreAddedAtRuntimeAreCalledWhenExecutingFunction()
    {
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
    public void executionInterceptorsFromConfigurationAreCalledWhenExecutingFunction()
    {
        final Object bapiObject = new Object();
        executionInterceptor.beforeExecution( ( BapiMapping ) anyObject(), ( Map<String, Object> ) anyObject() );
        executionInterceptor.afterExecution( ( BapiMapping ) anyObject(), ( Map<String, Object> ) anyObject() );
        replay( executionInterceptor );

        session.execute( bapiObject );

        verify( executionInterceptor );
    }

    @Test
    public void executionInterceptorsThatAreAddedAtRuntimeAreCalledWhenExecutingFunction()
    {
        ExecutionInterceptor myInterceptor = createMock( ExecutionInterceptor.class );

        final Object bapiObject = new Object();
        myInterceptor.beforeExecution( ( BapiMapping ) anyObject(), ( Map<String, Object> ) anyObject() );
        myInterceptor.afterExecution( ( BapiMapping ) anyObject(), ( Map<String, Object> ) anyObject() );
        replay( myInterceptor );

        session.addExecutionInterceptor( myInterceptor );
        session.execute( bapiObject );

        verify( myInterceptor );
    }

    private class SessionManagerStub implements SessionManagerImplementor
    {
        public Map<Class<?>, BapiMapping> getBapiMappings()
        {
            final HashMap<Class<?>, BapiMapping> mappings = new HashMap<Class<?>, BapiMapping>();
            mappings.put( Object.class, new BapiMapping( Object.class, "BAPI_NAME", null ) );
            return mappings;
        }

        public ConverterCache getConverterCache()
        {
            return new ConverterCache();
        }

        public SessionManagerConfig getConfig()
        {
            return new SessionManagerConfig();
        }

        public Context getContext()
        {
            return new ContextStub();
        }

        public Session openSession()
        {
            return null;
        }

        public Set<ExecutionInterceptor> getExecutionInterceptors()
        {

            return Collections.singleton( executionInterceptor );
        }

        public Set<BapiInterceptor> getBapiInterceptors()
        {
            return Collections.singleton( bapiInterceptor );
        }
    }

    private class ContextStub implements Context
    {
        public void configure( SessionManagerConfig config ) throws HibersapException
        {
        }

        public void close()
        {
        }

        public Connection getConnection()
        {
            return new ConnectionStub();
        }
    }

    private class ConnectionStub implements Connection
    {
        public void setCredentials( Credentials credentials )
        {
        }

        public Transaction beginTransaction( SessionImplementor session )
        {
            return null;
        }

        public Transaction getTransaction()
        {
            return null;
        }

        public void execute( String bapiName, Map<String, Object> functionMap )
        {
        }

        public void close()
        {
        }
    }
}