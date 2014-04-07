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

package org.hibersap.conversion;

import org.hibersap.mapping.ReflectionHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds instances of implementations of org.hibersap.conversion.Converter. The instances are
 * created lazily.
 *
 * @author Carsten Erker
 */
public class ConverterCache {

    private final Map<Class<? extends Converter>, Converter> converterForClass = new HashMap<Class<? extends Converter>, Converter>();

    /**
     * Called by the framework to get a Converter instance. If not yet in the cache, the instance
     * will be created lazily.
     *
     * @param clazz The Coverter implementation class
     * @return The Converter instance
     */
    public Converter getConverter( Class<? extends Converter> clazz ) {
        if ( clazz == null ) {
            throw new IllegalArgumentException( "null" );
        }
        Converter converter = converterForClass.get( clazz );
        if ( converter == null ) {
            converter = ReflectionHelper.newInstance( clazz );
            converterForClass.put( clazz, converter );
        }
        return converter;
    }

    /**
     * Returns the current number of Converter instances in the cache.
     *
     * @return The number of Converter instances
     */
    int getSize() {
        return converterForClass.size();
    }

    public void clear() {
        converterForClass.clear();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( converterForClass.hashCode() );
        return result;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        ConverterCache other = (ConverterCache) obj;
        //noinspection RedundantIfStatement
        if ( !converterForClass.keySet().equals( other.converterForClass.keySet() ) ) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + converterForClass;
    }
}
