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

package org.hibersap.ejb.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.HibersapException;
import org.hibersap.session.SessionManager;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JndiUtil
{
    private static final Log LOG = LogFactory.getLog( JndiUtil.class );

    private JndiUtil()
    {
        // use static utility methods
    }

    @PostConstruct
    public static void rebindSessionManager( SessionManager sessionManager, String jndiName )
    {
        LOG.info( "Binding Hibersap SessionManager '" + sessionManager.getConfig().getName()
                + "' to JNDI name '" + jndiName + "'" );

        try
        {
            Context ctx = new InitialContext();
            ctx.rebind( jndiName, sessionManager );
        }
        catch ( NamingException e )
        {
            throw new HibersapException( "Failed binding Hibersap SessionManager to JNDI name [" + jndiName + "]", e );
        }
    }

    @PreDestroy
    public static void unbindSessionManager( String jndiName )
    {
        LOG.info( "Unbinding Hibersap SessionManager from JNDI name '" + jndiName + "'" );

        try
        {
            Context ctx = new InitialContext();
            ctx.unbind( jndiName );
        }
        catch ( NamingException e )
        {
            LOG.warn( "Failed to unbind Hibersap SessionManager binding for JNDI name [" + jndiName + "]", e );
        }
    }
}
