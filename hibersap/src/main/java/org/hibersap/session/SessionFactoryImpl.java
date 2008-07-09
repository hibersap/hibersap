package org.hibersap.session;

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

import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import org.hibersap.configuration.Configuration;
import org.hibersap.configuration.Settings;
import org.hibersap.conversion.ConverterCache;
import org.hibersap.mapping.model.BapiMapping;


/**
 * @author Carsten Erker
 */
public class SessionFactoryImpl
    implements SessionFactory
{
    final Properties properties;

    private final Settings settings;

    private Map<Class<?>, BapiMapping> bapiMappings;

    // TODO exists for each SessionFactory instance, should be global to remove redundancies
    ConverterCache converterCache;

    public SessionFactoryImpl( Configuration configuration, Settings settings )
    {
        this.settings = settings;
        properties = new Properties();
        properties.putAll( configuration.getProperties() );
        bapiMappings = Collections.unmodifiableMap( configuration.getBapiMappings() );
        this.converterCache = new ConverterCache();
    }

    public Map<Class<?>, BapiMapping> getBapiMappings()
    {
        return bapiMappings;
    }

    public ConverterCache getConverterCache()
    {
        return this.converterCache;
    }

    public Session getCurrentSession()
    {
        // TODO implement current session context strategies
        return null;
    }

    public Properties getProperties()
    {
        return properties;
    }

    public Settings getSettings()
    {
        return settings;
    }

    public boolean isClosed()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public Session openSession()
    {
        return new SessionImpl( this );
    }
}
