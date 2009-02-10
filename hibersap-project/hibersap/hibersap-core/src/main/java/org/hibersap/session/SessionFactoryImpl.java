package org.hibersap.session;

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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hibersap.configuration.Configuration;
import org.hibersap.configuration.Settings;
import org.hibersap.configuration.xml.SessionFactoryConfig;
import org.hibersap.conversion.ConverterCache;
import org.hibersap.mapping.model.BapiMapping;

/**
 * Implementation of the SessionFactory. A client uses the SessionFactory to create Hibernate
 * Sessions.
 * 
 * @author Carsten Erker
 */
public class SessionFactoryImpl
    implements SessionFactory, SessionFactoryImplementor, Serializable
{
    private static final long serialVersionUID = 1L;

    final SessionFactoryConfig properties;

    private final Settings settings;

    private final Map<Class<?>, BapiMapping> bapiMappings;

    // TODO exists for each SessionFactory instance, should be global to remove redundancies
    private final ConverterCache converterCache;

    private final List<ExecutionInterceptor> interceptors;

    public SessionFactoryImpl( Configuration configuration, Settings settings )
    {
        this.settings = settings;
        this.converterCache = new ConverterCache();
        this.properties = configuration.getConfig();
        bapiMappings = Collections.unmodifiableMap( configuration.getBapiMappings() );
        interceptors = configuration.getInterceptors();
    }

    /**
     * {@inheritDoc}
     */
    public void reset()
    {
        settings.getContext().reset();
    }

    /**
     * {@inheritDoc}
     */
    public Map<Class<?>, BapiMapping> getBapiMappings()
    {
        return bapiMappings;
    }

    /**
     * {@inheritDoc}
     */
    public ConverterCache getConverterCache()
    {
        return this.converterCache;
    }

    /**
     * {@inheritDoc}
     */
    public SessionFactoryConfig getConfig()
    {
        return properties;
    }

    /**
     * {@inheritDoc}
     */
    public Settings getSettings()
    {
        return settings;
    }

    /**
     * {@inheritDoc}
     */
    public Session openSession()
    {
        return new SessionImpl( this );
    }

    /**
     * {@inheritDoc}
     */
    public List<ExecutionInterceptor> getInterceptors()
    {
        return interceptors;
    }
}
