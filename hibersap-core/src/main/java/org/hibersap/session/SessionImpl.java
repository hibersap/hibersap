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
public class SessionImpl
        implements Session, SessionImplementor
{
    private static final Log LOG = LogFactory.getLog( SessionImpl.class );

    private boolean closed = false;

    private final SessionManagerImplementor sessionManager;

    private PojoMapper pojoMapper;

    private Connection connection;

    private final Announcer<ExecutionInterceptor> executionInterceptors = Announcer.to( ExecutionInterceptor.class );

    private final Announcer<BapiInterceptor> bapiInterceptors = Announcer.to( BapiInterceptor.class );

    private final Credentials credentials;

    public SessionImpl( SessionManagerImplementor sessionManager )
    {
        this( sessionManager, null );
    }

    public SessionImpl( SessionManagerImplementor sessionManager, Credentials credentials )
    {
        this.sessionManager = sessionManager;
        this.credentials = credentials;

        pojoMapper = new PojoMapper( sessionManager.getConverterCache() );
        connection = sessionManager.getContext().getConnection();
        if ( credentialsProvided() )
        {
            LOG.debug( "Providing credentials" );
            connection.setCredentials( credentials );
        }
        executionInterceptors.addAllListeners( sessionManager.getExecutionInterceptors() );
        bapiInterceptors.addAllListeners( sessionManager.getBapiInterceptors() );
    }

    public Transaction beginTransaction()
    {
        assertNotClosed();
        return connection.beginTransaction( this );
    }

    public void close()
    {
        assertNotClosed();
        connection.close();
        setClosed();
    }

    private boolean credentialsProvided()
    {
        return credentials != null;
    }

    private void assertNotClosed()
    {
        if ( isClosed() )
        {
            throw new HibersapException( "Session is already closed" );
        }
    }

    public void execute( Object bapiObject )
    {
        assertNotClosed();
        Class<?> bapiClass = bapiObject.getClass();
        Map<Class<?>, BapiMapping> bapiMappings = sessionManager.getBapiMappings();
        if ( bapiMappings.containsKey( bapiClass ) )
        {
            bapiInterceptors.announce().beforeExecution( bapiObject );
            execute( bapiObject, bapiMappings.get( bapiClass ) );
            bapiInterceptors.announce().afterExecution( bapiObject );
        }
        else
        {
            throw new HibersapException( bapiClass.getName() + " is not mapped as a Bapi class" );
        }
    }

    public void execute( Object bapiObject, BapiMapping bapiMapping )
    {
        assertNotClosed();

        String bapiName = bapiMapping.getBapiName();
        LOG.debug( "Executing " + bapiName );

        Map<String, Object> functionMap = pojoMapper.mapPojoToFunctionMap( bapiObject, bapiMapping );

        executionInterceptors.announce().beforeExecution( bapiMapping, functionMap );

        connection.execute( bapiName, functionMap );

        executionInterceptors.announce().afterExecution( bapiMapping, functionMap );

        pojoMapper.mapFunctionMapToPojo( bapiObject, functionMap, bapiMapping );
    }

    public SessionManagerImplementor getSessionManager()
    {
        return sessionManager;
    }

    public Transaction getTransaction()
    {
        assertNotClosed();
        return connection.getTransaction();
    }

    public boolean isClosed()
    {
        return closed;
    }

    private void setClosed()
    {
        closed = true;
    }

    public void addExecutionInterceptor( ExecutionInterceptor interceptor )
    {
        executionInterceptors.addListener( interceptor );
    }

    public void addBapiInterceptor( BapiInterceptor interceptor )
    {
        bapiInterceptors.addListener( interceptor );
    }
}
