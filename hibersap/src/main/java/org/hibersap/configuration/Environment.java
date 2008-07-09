package org.hibersap.configuration;

/*
 * Copyright (C) 2008 akquinet tech@spree GmbH
 * 
 * This file is part of Hibersap.
 *
 * Hibersap is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hibersap is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Hibersap.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.HibersapException;


/**
 * @author Carsten Erker
 */
public final class Environment
{
    public static final String VERSION = "0.1";

    public static final String SESSION_FACTORY_NAME = "hibersap.session_factory_name";

    public static final String CONNECTION_PROVIDER = "hibersap.connection_provider_class";

    public static final String EXECUTOR = "hibersap.executor_class";

    public static final String REPOSITORY_NAME = "hibersap.jco.repository_name";

    public static final String CONNECTION_SAPCLIENT = "hibersap.jco.client.client";

    public static final String CONNECTION_USERNAME = "hibersap.jco.client.user";

    public static final String CONNECTION_PASSWORD = "hibersap.jco.client.passwd";

    public static final String CONNECTION_LANGUAGE = "hibersap.jco.client.lang";

    public static final String CONNECTION_APPLICATION_SERVER = "hibersap.jco.client.ashost";

    public static final String CONNECTION_SYSTEMNUMBER = "hibersap.jco.client.sysnr";

    public static final String CONNECTION_POOL_SIZE = "hibersap.jco.pool_size";

    public static final String CONNECTION_POOL_NAME = "hibersap.jco.pool_name";

    // SAP Resource Adapter settings
    public static final String CONNECTION_RA = "hibersap.connection.ra";

    // Session context management
    public static final String CURRENT_SESSION_CONTEXT_CLASS = "hibersap.current_session_context_class";

    // Echo all executed commands to stdout
    public static final String DO_LOG_COMMANDS = "hibersap.log.commands";

    private static Properties PROPERTIES = null;

    private static Log LOG = LogFactory.getLog( Environment.class );

    static
    {
        LOG.info( "Hibersap " + VERSION );

        PROPERTIES = new Properties();

        try
        {
            InputStream stream = ConfigHelper.getResourceAsStream( "/hibersap.properties" );
            try
            {
                PROPERTIES.load( stream );
                LOG.info( "loaded properties from resource hibersap.properties: " + PROPERTIES );
            }
            catch ( Exception e )
            {
                LOG.error( "problem loading properties from hibersap.properties" );
            }
            finally
            {
                try
                {
                    stream.close();
                }
                catch ( IOException ioe )
                {
                    LOG.error( "could not close stream on hibersap.properties", ioe );
                }
            }
        }
        catch ( HibersapException he )
        {
            LOG.info( "hibersap.properties not found" );
        }

        try
        {
            PROPERTIES.putAll( System.getProperties() );
        }
        catch ( SecurityException se )
        {
            LOG.warn( "could not copy system properties, system properties will be ignored" );
        }
    }

    /**
     * Return <tt>System</tt> properties, extended by any properties specified
     * in <tt>hibersap.properties</tt>.
     * 
     * @return Properties
     */
    public static Properties getProperties()
    {
        Properties copy = new Properties();
        copy.putAll( PROPERTIES );
        return copy;
    }
}
