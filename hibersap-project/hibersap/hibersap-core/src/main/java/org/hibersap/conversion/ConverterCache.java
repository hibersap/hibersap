package org.hibersap.conversion;

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
import java.util.HashMap;
import java.util.Map;

import org.hibersap.mapping.ReflectionHelper;

/**
 * Holds instances of implementations of org.hibersap.conversion.Converter. The instances are
 * created lazily.
 * 
 * @author Carsten Erker
 */
public class ConverterCache
    implements Serializable
{
    private static final long serialVersionUID = 1L;

    private final Map<Class<? extends Converter>, Converter> converterForClass = new HashMap<Class<? extends Converter>, Converter>();

    /**
     * Called by the framework to get a Converter instance. If not yet in the cache, the instance
     * will be created lazily.
     * 
     * @param clazz The Coverter implementation class
     * @return The Converter instance
     */
    public Converter getConverter( Class<? extends Converter> clazz )
    {
        if ( clazz == null )
        {
            throw new IllegalArgumentException( "null" );
        }
        Converter converter = converterForClass.get( clazz );
        if ( converter == null )
        {
            converter = (Converter) ReflectionHelper.newInstance( clazz );
            converterForClass.put( clazz, converter );
        }
        return converter;
    }

    /**
     * Returns the current number of Converter instances in the cache.
     * 
     * @return The number of Converter instances
     */
    int getSize()
    {
        return converterForClass.size();
    }

}
