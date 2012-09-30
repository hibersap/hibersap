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

package org.hibersap.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.ConfigurationException;
import org.hibersap.configuration.xml.HibersapConfig;
import org.hibersap.configuration.xml.HibersapJaxbXmlParser;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.session.SessionManager;
import org.hibersap.session.SessionManagerImpl;

import java.util.List;
import java.util.Map;

/**
 * Abstract Superclass for different configuration strategies.
 * Implements properties / settings handling.
 * The concrete class' responsibility is to provide the BapiMappings,
 * e.g. by analyzing annotated classes or XML mappings.
 *
 * @author Carsten Erker
 */
public abstract class Configuration
{
    private static final Log LOG = LogFactory.getLog( Configuration.class );

    static
    {
        LOG.info( "Hibersap Version " + Environment.VERSION );
    }

    private final ConfigurationData data;

    /**
     * Creates a Configuration for programmatic configuration of Hibersap. You have to create a
     * org.hibersap.configuration.xml.SessionManagerConfig first.
     *
     * @param sessionManagerConfig The session manager configuration
     */
    public Configuration( SessionManagerConfig sessionManagerConfig )
    {
        this.data = new ConfigurationData( sessionManagerConfig );
    }

    /**
     * Creates a Configuration for a concrete SessionManager. The SessionManager must be configured
     * in the hibersap.xml file.
     *
     * @param sessionManagerName The SessionManager name as specified in the hibersap.xml file
     */
    public Configuration( String sessionManagerName )
    {
        SessionManagerConfig sessionManagerConfig = readHibersapConfig().getSessionManager( sessionManagerName );
        this.data = new ConfigurationData( sessionManagerConfig );
    }

    /**
     * Creates a Configuration for the first SessionManager specified in the hibersap.xml file. This
     * method should only be used when there is exactly one SessionManager specified in
     * hibersap.xml.
     */
    public Configuration()
    {
        List<SessionManagerConfig> sessionManagers = readHibersapConfig().getSessionManagers();

        if ( sessionManagers.size() > 0 )
        {
            this.data = new ConfigurationData( sessionManagers.get( 0 ) );

            LOG.warn( "Only the first session manager (" + data.getSessionManagerConfig().getName()
                    + ") specified in hibersap.xml was configured. To configure the other specified session managers "
                    + "you must explicitly call the constructor of the org.hibersap.configuration.Configuration "
                    + "implementation with the sessionManagerName argument." );
        }
        else
        {
            throw new ConfigurationException( "Can not find a configured SessionManager" );
        }
    }

    private HibersapConfig readHibersapConfig()
    {
        LOG.debug( "Reading HibersapConfig from configuration file" );

        final HibersapJaxbXmlParser parser = new HibersapJaxbXmlParser();
        return parser.parseResource( Environment.HIBERSAP_XML_FILE );
    }

    /**
     * Builds the session manager for this Configuration.
     *
     * @return The session manager
     */
    public SessionManager buildSessionManager()
    {
        final SessionManagerConfig config = data.getSessionManagerConfig();

        LOG.info( "Building SessionManager '" + config.getName() + "'" );

        data.setExecutionInterceptors( ConfigurationHelper.createExecutionInterceptors( config ) );
        data.setBapiInterceptors( ConfigurationHelper.createBapiInterceptors( config ) );
        return new SessionManagerImpl( data, ConfigurationHelper.createContext( config ) );
    }

    /**
     * Concrete subclasses should use this method to provide the BAPI mapping information.
     *
     * @param bapiMappings A Map with the BAPI class as key and the BapiMappings as value.
     */
    protected void setBapiMappings( Map<Class<?>, BapiMapping> bapiMappings )
    {
        data.setBapiMappingsForClass( bapiMappings );
    }

    protected SessionManagerConfig getSessionManagerConfig()
    {
        return data.getSessionManagerConfig();
    }
}
