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

package org.hibersap.execution.jca;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.resource.cci.ConnectionFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.ConfigurationException;
import org.hibersap.HibersapException;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.execution.Connection;
import org.hibersap.execution.jca.cci.SapBapiJcaAdapterConnectionSpecFactory;
import org.hibersap.session.Context;

/**
 * Implementation for JCA, which uses a deployed resource adapter to connect to SAP.
 *
 * @author dahm
 */
public class JCAContext implements Context {

    private static final Log LOG = LogFactory.getLog( JCAContext.class );

    private static final String DEFAULT_CONNECTION_SPEC_FACTORY_CLASS = SapBapiJcaAdapterConnectionSpecFactory.class
            .getName();

    private ConnectionFactory connectionFactory;

    private String connectionSpecFactoryName;

    /**
     * {@inheritDoc}
     */
    public void configure( final SessionManagerConfig config )
            throws HibersapException {
        final String jndiName = getJndiName( config );
        connectionFactory = getConnectionFactory( jndiName );
        connectionSpecFactoryName = getConnectionSpecFactoryName( config );
    }

    private String getConnectionSpecFactoryName( final SessionManagerConfig config ) {
        String className = config.getJcaConnectionSpecFactory();

        if ( StringUtils.isEmpty( className ) ) {
            LOG.info( "JCA ConnectionSpecFactory not defined in Hibersap configuration, using default: "
                              + DEFAULT_CONNECTION_SPEC_FACTORY_CLASS );
            className = DEFAULT_CONNECTION_SPEC_FACTORY_CLASS;
        }
        return className;
    }

    /**
     * {@inheritDoc}
     */
    public Connection getConnection() {
        return new JCAConnection( connectionFactory, connectionSpecFactoryName );
    }

    private ConnectionFactory getConnectionFactory( final String jndiName ) {
        try {
            final InitialContext initialContext = new InitialContext();
            final Object object = initialContext.lookup( jndiName );

            if ( object == null ) {
                throw new HibersapException( "Name not bound: " + jndiName );
            }

            if ( !( object instanceof ConnectionFactory ) ) {
                throw new HibersapException( "Object bound under " + jndiName + " is no ConnectionFactory" );
            }

            return (ConnectionFactory) object;
        } catch ( final NamingException e ) {
            throw new HibersapException( "JNDI lookup:", e );
        }
    }

    private String getJndiName( final SessionManagerConfig config ) {
        final String jndiName = config.getJcaConnectionFactory();

        if ( StringUtils.isEmpty( jndiName ) ) {
            throw new ConfigurationException( "JCA connection factory not defined in Hibersap configuration" );
        }
        return jndiName;
    }

    /**
     * {@inheritDoc}
     */
    public void close() {
        connectionFactory = null;
    }
}
