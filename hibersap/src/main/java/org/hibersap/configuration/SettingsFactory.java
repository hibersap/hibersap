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

    public static Settings create( Properties props )
    {
        Settings settings = new Settings();

        // init Context
        Class<? extends Context> contextClass = getContextClass( props );
        Context context = getNewInstance( contextClass );
        context.configure( props );
        settings.setContext( context );

        return settings;
    }

    private static Context getNewInstance( Class<? extends Context> clazz )
    {
        try
        {
            return clazz.newInstance();
        }
        catch ( InstantiationException e )
        {
            throw new HibersapException( "The class " + clazz
                + " must be accessible and must have a public default constructor." );
        }
        catch ( IllegalAccessException e )
        {
            throw new HibersapException( "The class " + clazz + " must hava a public default constructor." );
        }
    }

    private static Class<? extends Context> getContextClass( Properties props )
    {
        String contextClassName = props.getProperty( Environment.CONTEXT_CLASS );
        Class<? extends Context> contextClass;
        if ( StringUtils.isEmpty( contextClassName ) )
        {
            Class<JCoContext> defaultContext = JCoContext.class;
            LOG.info( "No context class specified in properties. Default class " + defaultContext.getName()
                + " will be used" );
            contextClass = defaultContext;
        }
        else
        {
            contextClass = getContextClassForName( contextClassName );
        }
        return contextClass;
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends Context> getContextClassForName( String contextClassName )
    {
        Class<? extends Context> contextClass;
        try
        {
            Class<?> clazz = Class.forName( contextClassName );
            contextClass = (Class<? extends Context>) clazz;
        }
        catch ( ClassNotFoundException e )
        {
            throw new HibersapException( "Context class " + contextClassName + " not found in classpath.", e );
        }
        catch ( ClassCastException e )
        {
            throw new HibersapException( "The context class specified with property " + Environment.CONTEXT_CLASS
                + " must implement " + Context.class.getName(), e );
        }
        return contextClass;
    }
}
