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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.HibersapException;
import org.hibersap.execution.Connection;
import org.hibersap.execution.PojoMapper;
import org.hibersap.interceptor.BapiInterceptor;
import org.hibersap.interceptor.ExecutionInterceptor;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.util.Announcer;

import java.util.Map;

/*
 * @author Carsten Erker
 */
public class SessionImpl implements Session, SessionImplementor {

    private static final Log LOG = LogFactory.getLog( SessionImpl.class );

    private final SessionManagerImplementor sessionManager;
    private final Announcer<ExecutionInterceptor> executionInterceptors = Announcer.to( ExecutionInterceptor.class );
    private final Announcer<BapiInterceptor> bapiInterceptors = Announcer.to( BapiInterceptor.class );
    private final Credentials credentials;
    private boolean closed = false;
    private PojoMapper pojoMapper;
    private Connection connection;

    public SessionImpl( final SessionManagerImplementor sessionManager ) {
        this( sessionManager, null );
    }

    public SessionImpl( final SessionManagerImplementor sessionManager, final Credentials credentials ) {
        this.sessionManager = sessionManager;
        this.credentials = credentials;

        pojoMapper = new PojoMapper( sessionManager.getConverterCache() );
        connection = sessionManager.getContext().getConnection();
        if ( credentialsProvided() ) {
            LOG.debug( "Providing credentials" );
            connection.setCredentials( credentials );
        }
        executionInterceptors.addAllListeners( sessionManager.getExecutionInterceptors() );
        bapiInterceptors.addAllListeners( sessionManager.getBapiInterceptors() );
    }

    public Transaction beginTransaction() {
        assertNotClosed();
        return connection.beginTransaction( this );
    }

    public void close() {
        assertNotClosed();
        connection.close();
        setClosed();
    }

    private boolean credentialsProvided() {
        return credentials != null;
    }

    private void assertNotClosed() {
        if ( isClosed() ) {
            throw new HibersapException( "Session is already closed" );
        }
    }

    public void execute( final Object bapiObject ) {
        assertNotClosed();
        Class<?> bapiClass = bapiObject.getClass();
        Map<String, BapiMapping> bapiMappings = sessionManager.getBapiMappings();
        if ( bapiMappings.containsKey( bapiClass ) ) {
            bapiInterceptors.announce().beforeExecution( bapiObject );
            execute( bapiObject, bapiMappings.get( bapiClass ) );
            bapiInterceptors.announce().afterExecution( bapiObject );
        } else {
            throw new HibersapException( bapiClass.getName() + " is not mapped as a Bapi class" );
        }
    }

    public void execute( final Object bapiObject, final BapiMapping bapiMapping ) {
        assertNotClosed();

        String bapiName = bapiMapping.getBapiName();
        LOG.debug( "Executing " + bapiName );

        Map<String, Object> functionMap = pojoMapper.mapPojoToFunctionMap( bapiObject, bapiMapping );

        executionInterceptors.announce().beforeExecution( bapiMapping, functionMap );

        connection.execute( bapiName, functionMap );

        executionInterceptors.announce().afterExecution( bapiMapping, functionMap );

        pojoMapper.mapFunctionMapToPojo( bapiObject, functionMap, bapiMapping );
    }

    public SessionManagerImplementor getSessionManager() {
        return sessionManager;
    }

    public Transaction getTransaction() {
        assertNotClosed();
        return connection.getTransaction();
    }

    public boolean isClosed() {
        return closed;
    }

    private void setClosed() {
        closed = true;
    }

    public void addExecutionInterceptor( final ExecutionInterceptor interceptor ) {
        executionInterceptors.addListener( interceptor );
    }

    public void addBapiInterceptor( final BapiInterceptor interceptor ) {
        bapiInterceptors.addListener( interceptor );
    }
}
