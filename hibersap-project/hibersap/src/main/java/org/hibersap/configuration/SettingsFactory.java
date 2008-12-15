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
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.HibersapException;
import org.hibersap.execution.jco.JCoContext;
import org.hibersap.session.Context;

/**
 * @author Carsten Erker
 */
public class SettingsFactory
    implements Serializable
{
    private static final long serialVersionUID = 1L;

    private static final Log LOG = LogFactory.getLog( SettingsFactory.class );

    public static Settings create( final Properties props )
    {
        final Settings settings = new Settings();

        // init Context
        final Class<? extends Context> contextClass = getContextClass( props );
        final Context context = getNewInstance( contextClass );
        context.configure( props );
        settings.setContext( context );

        return settings;
    }

    private static Context getNewInstance( final Class<? extends Context> clazz )
    {
        try
        {
            return clazz.newInstance();
        }
        catch ( final InstantiationException e )
        {
            throw new HibersapException( "The class " + clazz
                + " must be accessible and must have a public default constructor." );
        }
        catch ( final IllegalAccessException e )
        {
            throw new HibersapException( "The class " + clazz + " must hava a public default constructor." );
        }
    }

    private static Class<? extends Context> getContextClass( final Properties props )
    {
        final String contextClassName = props.getProperty( Environment.CONTEXT_CLASS );
        Class<? extends Context> contextClass;
        if ( StringUtils.isEmpty( contextClassName ) )
        {
            final Class<JCoContext> defaultContext = JCoContext.class;
            LOG.info( "No context class specified in properties. Default class " + defaultContext.getName()
                + " will be used" );
            contextClass = defaultContext;
        }
        else
        {
            contextClass = getContextClassForName( contextClassName, Environment.CONTEXT_CLASS );
        }
        return contextClass;
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends Context> getContextClassForName( final String contextClassName,
                                                                    final String propertyName )
    {
        try
        {
            return (Class<? extends Context>) getClassForName( contextClassName, propertyName );
        }
        catch ( final ClassCastException e )
        {
            throw new HibersapException( "The context class specified with property " + propertyName
                + " must implement " + Context.class.getName(), e );
        }
    }

    public static Class<?> getClassForName( final String contextClassName, final String propertyName )
    {
        try
        {
            return Class.forName( contextClassName );
        }
        catch ( final ClassNotFoundException e )
        {
            throw new HibersapException( "Class " + contextClassName + " from property " + propertyName
                + " not found in classpath.", e );
        }
        catch ( final Exception e )
        {
            throw new HibersapException( "Class " + contextClassName + " from property " + propertyName
                + " could not be loaded", e );
        }
    }
}
