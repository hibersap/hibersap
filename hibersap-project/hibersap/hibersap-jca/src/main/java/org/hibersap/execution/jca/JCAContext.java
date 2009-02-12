package org.hibersap.execution.jca;

/*
 * Copyright (C) 2008 akquinet tech@spree GmbH
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

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.resource.cci.ConnectionFactory;

import org.apache.commons.lang.StringUtils;
import org.hibersap.ConfigurationException;
import org.hibersap.HibersapException;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.execution.Connection;
import org.hibersap.session.Context;

/**
 * Implementation for JCA, which uses a deployed resource adapter to connect to SAP.
 * 
 * @author dahm
 */
public class JCAContext
    implements Context
{
    private static final long serialVersionUID = 1L;

    private ConnectionFactory connectionFactory;

    /**
     * {@inheritDoc}
     */
    public void configure( final SessionManagerConfig config )
        throws HibersapException
    {
        final String jndiName = getJndiName( config );

        connectionFactory = getConnectionFactory( jndiName );
    }

    /**
     * {@inheritDoc}
     */
    public Connection getConnection()
    {
        return new JCAConnection( connectionFactory );
    }

    private ConnectionFactory getConnectionFactory( final String jndiName )
    {
        try
        {
            final InitialContext initialContext = new InitialContext();
            final Object object = initialContext.lookup( jndiName );

            if ( object == null )
            {
                throw new HibersapException( "Name not bound: " + jndiName );
            }

            if ( !( object instanceof ConnectionFactory ) )
            {
                throw new HibersapException( "Object bound under " + jndiName + " is no ConnectionFactory" );
            }

            return (ConnectionFactory) object;
        }
        catch ( final NamingException e )
        {
            throw new HibersapException( "JNDI lookup:", e );
        }
    }

    private String getJndiName( final SessionManagerConfig config )
    {
        final String jndiName = config.getJcaConnectionFactory();

        if ( StringUtils.isEmpty( jndiName ) )
        {
            throw new ConfigurationException( "JCA connection factory not defined in Hibersap configuration" );
        }
        return jndiName;
    }

    /**
     * {@inheritDoc}
     */
    public void reset()
    {
        connectionFactory = null;
    }
}
