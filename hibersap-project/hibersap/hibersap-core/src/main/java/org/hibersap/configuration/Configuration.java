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
import java.util.Properties;

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

    protected Properties properties = Environment.getProperties();

    protected final Map<Class<?>, BapiMapping> bapiMappingForClass = new HashMap<Class<?>, BapiMapping>();

    protected final List<ExecutionInterceptor> interceptors = new ArrayList<ExecutionInterceptor>();

    public SessionFactory buildSessionFactory()
    {
        addInterceptor( new SapErrorInterceptor() );
        return new SessionFactoryImpl( this, buildSettings( properties ) );
    }

    public Settings buildSettings( Properties props )
    {
        return SettingsFactory.create( props );
    }

    public Map<Class<?>, BapiMapping> getBapiMappings()
    {
        return bapiMappingForClass;
    }

    public Properties getProperties()
    {
        return properties;
    }

    /**
     * Get a property.
     */
    public String getProperty( String key )
    {
        return properties.getProperty( key );
    }

    /**
     * Specify a completely new set of properties
     */
    public Configuration setProperties( Properties properties )
    {
        this.properties = properties;
        return this;
    }

    /**
     * Add or change a property.
     */
    public Configuration setProperty( String key, String value )
    {
        properties.put( key, value );
        return this;
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
