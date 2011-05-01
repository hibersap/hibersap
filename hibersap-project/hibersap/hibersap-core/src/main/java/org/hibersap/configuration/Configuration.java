package org.hibersap.configuration;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.ConfigurationException;
import org.hibersap.configuration.xml.HibersapConfig;
import org.hibersap.configuration.xml.HibersapJaxbXmlParser;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.interceptor.BapiInterceptor;
import org.hibersap.interceptor.ExecutionInterceptor;
import org.hibersap.interceptor.impl.SapErrorInterceptor;
import org.hibersap.mapping.ReflectionHelper;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.session.SessionManager;
import org.hibersap.session.SessionManagerImpl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Abstract Superclass for different configuration strategies. Implements properties / settings
 * handling.
 *
 * @author Carsten Erker
 */
public abstract class Configuration implements Serializable
{
    private static final long serialVersionUID = 1L;

    private static final Log LOG = LogFactory.getLog( Configuration.class );

    private HibersapConfig hibersapConfig;

    private SessionManagerConfig sessionManagerConfig;

    private final Map<Class<?>, BapiMapping> bapiMappingForClass = new HashMap<Class<?>, BapiMapping>();

    private final Set<ExecutionInterceptor> executionInterceptors = new HashSet<ExecutionInterceptor>();

    private final Set<BapiInterceptor> bapiInterceptors = new HashSet<BapiInterceptor>();

    /**
     * Creates a Configuration for programmatic configuration of Hibersap. You have to create a
     * org.hibersap.configuration.xml.SessionManagerConfig first.
     *
     * @param sessionManagerConfig The session manager configuration
     */
    public Configuration( SessionManagerConfig sessionManagerConfig )
    {
        this.sessionManagerConfig = sessionManagerConfig;
    }

    /**
     * Creates a Configuration for a concrete SessionManager. The SessionManager must be configured
     * in the hibersap.xml file.
     *
     * @param sessionManagerName The SessionManager name as specified in the hibersap.xml file
     */
    public Configuration( String sessionManagerName )
    {
        this.sessionManagerConfig = getHibersapConfig().getSessionManager( sessionManagerName );
    }

    /**
     * Creates a Configuration for the first SessionManager specified in the hibersap.xml file. This
     * method should only be used when there is exactly one SessionManager specified in
     * hibersap.xml.
     */
    public Configuration()
    {
        List<SessionManagerConfig> sessionManagers = getHibersapConfig().getSessionManagers();
        int smCount = sessionManagers.size();
        if ( smCount > 0 )
        {
            this.sessionManagerConfig = sessionManagers.get( 0 );

            LOG
                    .warn( "Only the first session manager ("
                            + sessionManagerConfig.getName()
                            +
                            ") specified in hibersap.xml was configured. To configure the other specified session managers "
                            +
                            "you must explicitly call the constructor of the org.hibersap.configuration.Configuration implementation "
                            + "with the sessionManagerName argument." );
        }
        else
        {
            throw new ConfigurationException( "Can not find a configured SessionManager" );
        }
    }

    private HibersapConfig getHibersapConfig()
    {
        if ( hibersapConfig == null )
        {
            final HibersapJaxbXmlParser parser = new HibersapJaxbXmlParser();
            hibersapConfig = parser.parseResource( Environment.HIBERSAP_XML_FILE );
        }
        return hibersapConfig;
    }

    /**
     * Builds the session manager for this Configuration.
     *
     * @return The session manager
     */
    public SessionManager buildSessionManager()
    {
        LOG.info( "Hibersap Version " + Environment.VERSION );
        LOG.info( "Building SessionManager '" + sessionManagerConfig.getName() + "'" );
        addExecutionInterceptors();
        addBapiInterceptors();
        return new SessionManagerImpl( this, buildSettings( sessionManagerConfig ) );
    }

    private void addExecutionInterceptors()
    {
        final Set<String> classNames = sessionManagerConfig.getExecutionInterceptorClasses();
        addExecutionInterceptors( ReflectionHelper.createInstances( classNames, ExecutionInterceptor.class ) );
        addExecutionInterceptor( new SapErrorInterceptor() );
    }

    private void addBapiInterceptors()
    {
        final Set<String> classNames = sessionManagerConfig.getBapiInterceptorClasses();
        addBapiInterceptors( ReflectionHelper.createInstances( classNames, BapiInterceptor.class ) );
    }

    private Settings buildSettings( SessionManagerConfig config )
    {
        return SettingsFactory.create( config );
    }

    public Map<Class<?>, BapiMapping> getBapiMappings()
    {
        return bapiMappingForClass;
    }

    public SessionManagerConfig getSessionManagerConfig()
    {
        return sessionManagerConfig;
    }

    public void setSessionManagerConfig( SessionManagerConfig config )
    {
        this.sessionManagerConfig = config;
    }

    public Set<ExecutionInterceptor> getExecutionInterceptors()
    {
        return Collections.unmodifiableSet( executionInterceptors );
    }

    public void addExecutionInterceptor( ExecutionInterceptor interceptor )
    {
        this.executionInterceptors.add( interceptor );
    }

    public void addExecutionInterceptors( Collection<ExecutionInterceptor> interceptors )
    {
        this.executionInterceptors.addAll( interceptors );
    }

    public Set<BapiInterceptor> getBapiInterceptors()
    {
        return Collections.unmodifiableSet( bapiInterceptors );
    }

    public void addBapiInterceptors( Collection<BapiInterceptor> interceptors )
    {
        bapiInterceptors.addAll( interceptors );
    }
}
