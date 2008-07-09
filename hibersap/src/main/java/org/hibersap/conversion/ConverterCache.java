package org.hibersap.conversion;

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

import java.util.HashMap;
import java.util.Map;

import org.hibersap.mapping.ReflectionHelper;


/**
 * @author Carsten Erker
 */
public class ConverterCache
{
    private final Map<Class<? extends Converter>, Converter> converterForClass = new HashMap<Class<? extends Converter>, Converter>();

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
}
