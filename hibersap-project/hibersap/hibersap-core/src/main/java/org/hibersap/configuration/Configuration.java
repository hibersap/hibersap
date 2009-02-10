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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.ConfigurationException;
import org.hibersap.configuration.xml.SessionFactoryConfig;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.session.ExecutionInterceptor;
import org.hibersap.session.SapErrorInterceptor;
import org.hibersap.session.SessionFactory;
import org.hibersap.session.SessionFactoryImpl;

/**
 * Abstract Superclass for different configuration strategies. Implements properties / settings
 * handling.
 * 
 * @author Carsten Erker
 */
public abstract class Configuration
    implements Serializable
{
    private static final long serialVersionUID = 1L;

    private static final Log LOG = LogFactory.getLog( Configuration.class );

    protected SessionFactoryConfig sessionFactoryConfig;

    protected final Map<Class<?>, BapiMapping> bapiMappingForClass = new HashMap<Class<?>, BapiMapping>();

    protected final List<ExecutionInterceptor> interceptors = new ArrayList<ExecutionInterceptor>();

    /**
     * Creates a Configuration for a concrete Session Factory. The Session Factory must be
     * configured in the hibersap.xml file.
     * 
     * @param sessionFactoryName The Session Factory name as specified in the hibersap.xml file
     */
    public Configuration( String sessionFactoryName )
    {
        this.sessionFactoryConfig = Environment.getHibersapConfig().getSessionFactory( sessionFactoryName );
    }

    /**
     * Creates a Configuration for the first Session Factory specified in the hibersap.xml file.
     * This method should only be used when there is exactly one Session Factory specified in
     * hibersap.xml.
     */
    public Configuration()
    {
        List<SessionFactoryConfig> sessionFactories = Environment.getHibersapConfig().getSessionFactories();
        int sfCount = sessionFactories.size();
        if ( sfCount > 0 )
        {
            this.sessionFactoryConfig = sessionFactories.get( 0 );
        }
        else
        {
            throw new ConfigurationException( "Can not find a configured Session Factory" );
        }
        if ( sfCount > 1 )
        {
            LOG
                .warn( "Only the first session factory ("
                    + sessionFactoryConfig.getName()
                    + ") specified in hibersap.xml was configured. To configure the other specified session factories "
                    + "you must explicitly call the constructor of the org.hibersap.configuration.Configuration implementation "
                    + "with the sessionFactoryName argument." );
        }
    }

    /**
     * Creates a Configuration for programmatic configuration of Hibersap. You have to create a
     * org.hibersap.configuration.xml.SessionFactoryConfig first.
     * 
     * @param sessionFactoryConfig The session factory configuration
     */
    public Configuration( SessionFactoryConfig sessionFactoryConfig )
    {
        this.sessionFactoryConfig = sessionFactoryConfig;
    }

    /**
     * Builds the session factory for this Configuration.
     * 
     * @return The session factory
     */
    public SessionFactory buildSessionFactory()
    {
        addInterceptor( new SapErrorInterceptor() );
        return new SessionFactoryImpl( this, buildSettings( sessionFactoryConfig ) );
    }

    private Settings buildSettings( SessionFactoryConfig config )
    {
        return SettingsFactory.create( config );
    }

    public Map<Class<?>, BapiMapping> getBapiMappings()
    {
        return bapiMappingForClass;
    }

    public SessionFactoryConfig getConfig()
    {
        return sessionFactoryConfig;
    }

    public void setConfig( SessionFactoryConfig config )
    {
        this.sessionFactoryConfig = config;
    }

    public List<ExecutionInterceptor> getInterceptors()
    {
        return interceptors;
    }

    public void addInterceptor( ExecutionInterceptor interceptor )
    {
        this.interceptors.add( interceptor );
    }
}
