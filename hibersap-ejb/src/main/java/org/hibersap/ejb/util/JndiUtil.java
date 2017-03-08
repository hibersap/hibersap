/*
 * Copyright (c) 2008-2017 akquinet tech@spree GmbH
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

package org.hibersap.ejb.util;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.HibersapException;
import org.hibersap.session.SessionManager;

public final class JndiUtil {

    private static final Log LOG = LogFactory.getLog( JndiUtil.class );

    private JndiUtil() {
        // use static utility methods
    }

    @PostConstruct
    public static void rebindSessionManager( final SessionManager sessionManager, final String jndiName ) {
        LOG.info( "Binding Hibersap SessionManager '" + sessionManager.getConfig().getName()
                          + "' to JNDI name '" + jndiName + "'" );

        try {
            Context ctx = new InitialContext();
            ctx.rebind( jndiName, sessionManager );
        } catch ( NamingException e ) {
            throw new HibersapException( "Failed binding Hibersap SessionManager to JNDI name [" + jndiName + "]", e );
        }
    }

    @PreDestroy
    public static void unbindSessionManager( final String jndiName ) {
        LOG.info( "Unbinding Hibersap SessionManager from JNDI name '" + jndiName + "'" );

        try {
            Context ctx = new InitialContext();
            ctx.unbind( jndiName );
        } catch ( NamingException e ) {
            LOG.warn( "Failed to unbind Hibersap SessionManager binding for JNDI name [" + jndiName + "]", e );
        }
    }
}
