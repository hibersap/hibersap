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

import org.apache.commons.lang.StringUtils;

/**
 * Converts between SAP character fields of length 1 and Java char fields.
 * 
 * @author Carsten Erker
 */
public class CharConverter
    implements Converter
{
    /**
     * {@inheritDoc}
     */
    public Object convertToJava( Object sapValue )
        throws ConversionException
    {
        String valueStr = (String) sapValue;
        if ( StringUtils.isEmpty( valueStr ) )
        {
            return ' ';
        }
        return valueStr.charAt( 0 );
    }

    /**
     * {@inheritDoc}
     */
    public Object convertToSap( Object javaValue )
        throws ConversionException
    {
        if ( javaValue == null )
        {
            return "";
        }
        Character valueChar = (Character) javaValue;
        return "" + valueChar;
    }
}
