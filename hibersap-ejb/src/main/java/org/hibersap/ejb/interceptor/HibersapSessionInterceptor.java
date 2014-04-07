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

package org.hibersap.ejb.interceptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.HibersapException;
import org.hibersap.session.Session;
import org.hibersap.session.SessionManager;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;
import static org.hibersap.ejb.util.ReflectionUtil.getHibersapSessionFields;
import static org.hibersap.ejb.util.ReflectionUtil.getSessionManagerJndiName;
import static org.hibersap.ejb.util.ReflectionUtil.injectSessionIntoTarget;

public class HibersapSessionInterceptor
{
    private static final String HIBERSAP_SESSION_PREFIX = "hibersap.session.";

    private static final Log LOGGER = LogFactory.getLog( HibersapSessionInterceptor.class );

    @AroundInvoke
    public Object injectSessionsIntoEjb( InvocationContext ctx ) throws Exception
    {
        Set<Field> sessionFields = getHibersapSessionFields( ctx.getTarget() );

        Map<Session, String> sessionsCreated = new HashMap<Session, String>();
        try
        {
            for ( Field sessionField : sessionFields )
            {
                String jndiName = getSessionManagerJndiName( sessionField );
                String key = HIBERSAP_SESSION_PREFIX + jndiName;
                Session session = ( Session ) ctx.getContextData().get( key );

                if ( session == null )
                {
                    LOGGER.debug( "Erzeuge Hibersap-Session f�r SessionManager " + jndiName );
                    session = openSession( jndiName );
                    sessionsCreated.put( session, jndiName );
                    ctx.getContextData().put( key, session );
                }

                injectSessionIntoTarget( ctx.getTarget(), sessionField, session );
            }

            return ctx.proceed();
        }
        finally
        {
            closeSessions( sessionsCreated, ctx.getContextData() );
        }
    }

    private void closeSessions( final Map<Session, String> sessions, final Map<String, Object> contextData )
    {
        Set<String> sessionManagerNamesWithError = new HashSet<String>();
        for ( Session session : sessions.keySet() )
        {
            String jndiName = sessions.get( session );
            try
            {
                contextData.remove( HIBERSAP_SESSION_PREFIX + jndiName );

                if ( session != null && !session.isClosed() )
                {
                    LOGGER.debug( "Schlie�e Hibersap-Session f�r SessionManager " + jndiName );
                    session.close();
                }
            }
            catch ( RuntimeException e )
            {
                LOGGER.error( "Error closing Hibersap Session for SessionManager with JNDI name " + jndiName, e );
                sessionManagerNamesWithError.add( jndiName );
            }
        }
        if ( !sessionManagerNamesWithError.isEmpty() )
        {
            throw new HibersapException( format(
                    "Error closing Session(s) for the SessionManager(s): %s. "
                            +
                            "The corresponding SAP connection may not be released! For individual reasons see error logs.",
                    sessionManagerNamesWithError ) );
        }
    }

    private Session openSession( final String sessionManagerJndiName )
    {
        SessionManager sessionManager = lookupSessionManager( sessionManagerJndiName );
        return sessionManager.openSession();
    }

    private SessionManager lookupSessionManager( final String jndiName )
    {
        InitialContext context = null;
        try
        {
            context = new InitialContext();
            Object object = context.lookup( jndiName );

            if ( object == null )
            {
                throw new HibersapException(
                        format( "Lookup for JNDI name '%s' returned null. Expected to find an instance of %s",
                                jndiName, SessionManager.class.getName() ) );
            }
            if ( !SessionManager.class.isAssignableFrom( object.getClass() ) )
            {
                throw new HibersapException(
                        format( "Object bound under JNDI name '%s' is not a %s but an instance of %s",
                                jndiName, SessionManager.class.getName(), object.getClass().getName() ) );
            }
            return ( SessionManager ) object;
        }
        catch ( NamingException e )
        {
            throw new HibersapException( "Error creating InitialContext", e );
        }
        finally
        {
            if ( context != null )
            {
                try
                {
                    context.close();
                }
                catch ( NamingException e )
                {
                    LOGGER.warn( "Error closing InitialContext", e );
                }
            }
        }
    }
}
