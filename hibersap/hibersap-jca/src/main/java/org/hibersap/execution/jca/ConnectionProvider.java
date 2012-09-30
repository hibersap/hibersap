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

package org.hibersap.execution.jca;

import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.ConnectionSpec;

import org.hibersap.ConfigurationException;
import org.hibersap.HibersapException;
import org.hibersap.execution.jca.cci.ConnectionSpecFactory;
import org.hibersap.mapping.ReflectionHelper;
import org.hibersap.session.Credentials;

public class ConnectionProvider
{
    private final ConnectionFactory connectionFactory;

    private final String connectionSpecFactoryName;

    private Connection connection;

    private Credentials credentials;

    ConnectionProvider( ConnectionFactory connectionFactory, String connectionSpecFactoryName )
    {
        this.connectionFactory = connectionFactory;
        this.connectionSpecFactoryName = connectionSpecFactoryName;
    }

    Connection getConnection()
    {
        if ( connection == null )
        {
            connection = newConnection();
        }
        return connection;
    }

    void setCredentials( Credentials credentials )
    {
        this.credentials = credentials;
    }

    private Connection newConnection()
    {
        try
        {
            if ( credentials == null )
            {
                return connectionFactory.getConnection();
            }
            else
            {
                ConnectionSpec connectionSpec = newConnectionSpecFactory( connectionSpecFactoryName )
                    .createConnectionSpec( credentials );
                return connectionFactory.getConnection( connectionSpec );
            }
        }
        catch ( ResourceException e )
        {
            throw new HibersapException( "Problem creating Connection", e );
        }
    }

    private ConnectionSpecFactory newConnectionSpecFactory( final String className )
    {
        Class<?> clazz;
        try
        {
            clazz = ReflectionHelper.getClassForName( className );
        }
        catch ( ClassNotFoundException e )
        {
            throw new ConfigurationException( "ConnectionSpecFactory implementation class not found: " + className, e );
        }

        if ( !ConnectionSpecFactory.class.isAssignableFrom( clazz ) )
        {
            throw new ConfigurationException( "Class " + clazz.getName() + " does not implement "
                + ConnectionSpecFactory.class.getName() );
        }

        Object newInstance = ReflectionHelper.newInstance( clazz );

        return (ConnectionSpecFactory) newInstance;
    }

}
