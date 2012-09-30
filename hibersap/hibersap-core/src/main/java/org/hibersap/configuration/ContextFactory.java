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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.ConfigurationException;
import org.hibersap.HibersapException;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.session.Context;

/**
 * TODO merge Reflection stuff with ReflectionHelper
 *
 * @author Carsten Erker
 */
public class ContextFactory
{
    private static final Log LOG = LogFactory.getLog( ContextFactory.class );

    private ContextFactory()
    {
        // should not be instantiated
    }

    public static Context create( final SessionManagerConfig config )
    {
        // init Context
        final Class<? extends Context> contextClass = getContextClass( config );
        final Context context = getNewInstance( contextClass );
        context.configure( config );

        return context;
    }

    private static Context getNewInstance( final Class<? extends Context> clazz )
    {
        try
        {
            return clazz.newInstance();
        }
        catch ( final IllegalAccessException e )
        {
            throw new ConfigurationException( "The class " + clazz
                    + " must be accessible.", e );
        }
        catch ( final InstantiationException e )
        {
            throw new ConfigurationException( "The class " + clazz
                    + " must be accessible and must have a public default constructor.", e );
        }
        catch ( final ConfigurationException e )
        {
            throw new HibersapException( "The class " + clazz + " must hava a public default constructor.", e );
        }
    }

    private static Class<? extends Context> getContextClass( final SessionManagerConfig config )
    {
        String contextClassName = config.getContext();

        if ( StringUtils.isEmpty( contextClassName ) )
        {
            contextClassName = "org.hibersap.execution.jco.JCoContext";
            LOG.info( "No context class specified in properties. Default class " + contextClassName + " will be used" );
        }
        return getContextClassForName( contextClassName );
    }

    @SuppressWarnings( "unchecked" )
    private static Class<? extends Context> getContextClassForName( final String contextClassName )
    {
        try
        {
            return ( Class<? extends Context> ) getClassForName( contextClassName );
        }
        catch ( final ClassCastException e )
        {
            throw new ConfigurationException( "The class " + contextClassName + " must implement "
                    + Context.class.getName() + " to act as a context class", e );
        }
    }

    public static Class<?> getClassForName( final String contextClassName )
    {
        try
        {
            return Class.forName( contextClassName, true, Thread.currentThread().getContextClassLoader() );
        }
        catch ( Exception exc )
        {
            try
            {
                return Class.forName( contextClassName );
            }
            catch ( final ClassNotFoundException e )
            {
                throw new ConfigurationException( "Class " + contextClassName + " not found.", e );
            }
            catch ( final Exception e )
            {
                throw new ConfigurationException( "Class " + contextClassName + " could not be loaded", e );
            }
        }
    }
}
