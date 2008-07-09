package org.hibersap.configuration;

/*
 * Copyright (C) 2008 akquinet tech@spree GmbH
 * 
 * This file is part of Hibersap.
 *
 * Hibersap is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hibersap is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Hibersap.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.Serializable;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.HibersapException;
import org.hibersap.execution.Executor;
import org.hibersap.execution.jco.JCoExecutor;


/**
 * @author Carsten Erker
 */
public class SettingsFactory
    implements Serializable
{
    private static final Log LOG = LogFactory.getLog( SettingsFactory.class );

    public static Settings create( Properties props )
    {
        Settings settings = new Settings();

        Class<? extends Executor> executorClass = getExecutorClass( props );
        settings.setExecutorClass( executorClass );

        return settings;
    }

    private static Class<? extends Executor> getExecutorClass( Properties props )
    {
        String executorClassName = props.getProperty( Environment.EXECUTOR );
        Class<? extends Executor> executorClass;

        if ( StringUtils.isEmpty( executorClassName ) )
        {
            Class<JCoExecutor> defaultExecutor = JCoExecutor.class;
            LOG.info( "No executor class specified in properties. Default executor " + defaultExecutor.getName()
                + " will be used" );
            executorClass = defaultExecutor;
        }
        else
        {
            executorClass = getExecutorClassForName( executorClassName );
        }
        return executorClass;
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends Executor> getExecutorClassForName( String executorClassName )
    {
        Class<? extends Executor> executorClass;
        try
        {
            Class<?> clazz = Class.forName( executorClassName );
            executorClass = (Class<? extends Executor>) clazz;
        }
        catch ( ClassNotFoundException e )
        {
            throw new HibersapException( "Executor class " + executorClassName + " not found in classpath.", e );
        }
        catch ( ClassCastException e )
        {
            throw new HibersapException( "The executor class specified with property " + Environment.EXECUTOR
                + " must implement " + Executor.class.getName(), e );
        }
        return executorClass;
    }
}
