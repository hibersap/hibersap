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

import java.io.InputStream;

import org.hibersap.HibersapException;


/**
 * @author Carsten Erker
 */
public final class ConfigHelper
{
    public static InputStream getResourceAsStream( String resource )
    {
        String stripped = resource.startsWith( "/" ) ? resource.substring( 1 ) : resource;

        InputStream stream = null;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if ( classLoader != null )
        {
            stream = classLoader.getResourceAsStream( stripped );
        }
        if ( stream == null )
        {
            stream = Environment.class.getResourceAsStream( resource );
        }
        if ( stream == null )
        {
            stream = Environment.class.getClassLoader().getResourceAsStream( stripped );
        }
        if ( stream == null )
        {
            throw new HibersapException( resource + " not found" );
        }
        return stream;
    }

    private ConfigHelper()
    {
        // should not be instantiated
    }

}
