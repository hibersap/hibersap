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

package org.hibersap.session;

import org.hibersap.HibersapException;
import org.hibersap.configuration.ConfigurationData;
import org.hibersap.configuration.ConfigurationHelper;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.conversion.ConverterCache;
import org.hibersap.interceptor.BapiInterceptor;
import org.hibersap.interceptor.ExecutionInterceptor;
import org.hibersap.mapping.model.BapiMapping;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of the SessionManager. A client uses the SessionManager to create Hibernate
 * Sessions.
 *
 * @author Carsten Erker
 */
public final class SessionManagerImpl implements SessionManager, SessionManagerImplementor
{
    private static final long serialVersionUID = -541810809624063050L;

    private boolean closed;

    private final SessionManagerConfig config;

    private Map<Class<?>, BapiMapping> bapiMappings;

    private transient Context context;

    private transient ConverterCache converterCache;

    private transient Set<ExecutionInterceptor> executionInterceptors;

    private transient Set<BapiInterceptor> bapiInterceptors = new HashSet<BapiInterceptor>();

    public SessionManagerImpl( ConfigurationData data, Context context )
    {
        closed = false;
        config = data.getSessionManagerConfig();
        bapiMappings = new HashMap<Class<?>, BapiMapping>( data.getBapiMappingsForClass() );
        initializeTransientFields( data, context );
    }

    private void initializeTransientFields( ConfigurationData data, Context context )
    {
        this.context = context;
        converterCache = new ConverterCache();
        executionInterceptors = new HashSet<ExecutionInterceptor>( data.getExecutionInterceptors() );
        bapiInterceptors = new HashSet<BapiInterceptor>( data.getBapiInterceptors() );
    }

    /*
     * {@inheritDoc}
     */
    public void close()
    {
        if ( !closed )
        {
            closed = true;
            context.close();
            bapiMappings.clear();
            converterCache.clear();
            executionInterceptors.clear();
            bapiInterceptors.clear();
        }
    }

    /*
    * {@inheritDoc}
    */
    public boolean isClosed()
    {
        return closed;
    }

    /*
    * {@inheritDoc}
    */
    public Map<Class<?>, BapiMapping> getBapiMappings()
    {
        assertNotClosed();
        return Collections.unmodifiableMap( bapiMappings );
    }

    /*
     * {@inheritDoc}
     */
    public ConverterCache getConverterCache()
    {
        assertNotClosed();
        return this.converterCache;
    }

    /*
     * {@inheritDoc}
     */
    public SessionManagerConfig getConfig()
    {
        assertNotClosed();
        return config;
    }

    /*
     * {@inheritDoc}
     */
    public Context getContext()
    {
        assertNotClosed();
        return context;
    }

    /*
     * {@inheritDoc}
     */
    public Session openSession()
    {
        assertNotClosed();
        return new SessionImpl( this );
    }

    /*
     * {@inheritDoc}
     */
    public Session openSession( Credentials credentials )
    {
        assertNotClosed();
        return new SessionImpl( this, credentials );
    }

    /*
     * {@inheritDoc}
     */
    public Set<ExecutionInterceptor> getExecutionInterceptors()
    {
        assertNotClosed();
        return executionInterceptors;
    }

    /*
     * {@inheritDoc}
     */
    public Set<BapiInterceptor> getBapiInterceptors()
    {
        assertNotClosed();
        return bapiInterceptors;
    }

    private void assertNotClosed()
    {
        if ( closed )
        {
            throw new HibersapException( "The SessionManager has been closed, it must not be used anymore" );
        }
    }

    @Override
    public String toString()
    {
        String format = "SessionManagerImpl[Config=[%s], ContextClass=[%s], Converters=[%s], Interceptors=[%s], BapiMappings=[%s]]";
        return String.format( format, config.toString(), context.toString(), converterCache.toString(),
                executionInterceptors, bapiMappings );
    }

    private void readObject( ObjectInputStream stream ) throws ClassNotFoundException, IOException
    {
        stream.defaultReadObject();

        context = ConfigurationHelper.createContext( config );
        converterCache = new ConverterCache();
        bapiInterceptors = ConfigurationHelper.createBapiInterceptors( config );
        executionInterceptors = ConfigurationHelper.createExecutionInterceptors( config );
    }
}
