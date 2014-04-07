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

package org.hibersap.execution;

import java.util.Collection;
import java.util.Map;

public final class UnsafeCastHelper
{
    private UnsafeCastHelper()
    {
        // use static methods
    }

    @SuppressWarnings("unchecked")
    public static Collection<Object> castToCollection( Object value )
    {
        return (Collection<Object>) value;
    }

    @SuppressWarnings("unchecked")
    public static Collection<Map<String, Object>> castToCollectionOfMaps( Object value )
    {
        return (Collection<Map<String, Object>>) value;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> castToMap( Object value )
    {
        return Map.class.cast( value );
    }
}
