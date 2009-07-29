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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.hibersap.configuration.Configuration;
import org.hibersap.configuration.Settings;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.conversion.ConverterCache;
import org.hibersap.mapping.model.BapiMapping;

/*
 * Implementation of the SessionManager. A client uses the SessionManager to create Hibernate
 * Sessions.
 * 
 * @author Carsten Erker
 */
public final class SessionManagerImpl
    implements SessionManager, SessionManagerImplementor, Serializable
{
    private final SessionManagerConfig config;

    private final Settings settings;

    private final Map<Class<?>, BapiMapping> bapiMappings = new HashMap<Class<?>, BapiMapping>();

    // TODO exists for each SessionManager instance, should be global to remove redundancies
    private final ConverterCache converterCache;

    private final Set<ExecutionInterceptor> interceptors;

    public SessionManagerImpl( Configuration configuration, Settings settings )
    {
        this.settings = settings;
        this.converterCache = new ConverterCache();
        this.config = configuration.getSessionManagerConfig();
        bapiMappings.putAll( configuration.getBapiMappings() );
        interceptors = configuration.getInterceptors();
    }

    /*
     * {@inheritDoc}
     */
    public void reset()
    {
        settings.getContext().reset();
    }

    /*
     * {@inheritDoc}
     */
    public Map<Class<?>, BapiMapping> getBapiMappings()
    {
        return Collections.unmodifiableMap( bapiMappings );
    }

    /*
     * {@inheritDoc}
     */
    public ConverterCache getConverterCache()
    {
        return this.converterCache;
    }

    /*
     * {@inheritDoc}
     */
    public SessionManagerConfig getConfig()
    {
        return config;
    }

    /*
     * {@inheritDoc}
     */
    public Settings getSettings()
    {
        return settings;
    }

    /*
     * {@inheritDoc}
     */
    public Session openSession()
    {
        return new SessionImpl( this );
    }

    /*
     * {@inheritDoc}
     */
    public Session openSession( Credentials credentials )
    {
        return new SessionImpl( this, credentials );
    }

    /*
     * {@inheritDoc}
     */
    public Set<ExecutionInterceptor> getInterceptors()
    {
        return Collections.unmodifiableSet( interceptors );
    }

    @Override
    public String toString()
    {
        String format = "SessionManagerImpl[Config=[%s], ContextClass=[%s], Converters=[%s], Interceptors=[%s], BapiMappings=[%s]]";
        return String.format( format, config.toString(), settings.getContext(), converterCache.toString(),
                              interceptors, bapiMappings );
    }
}
