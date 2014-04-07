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

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * Converts between Java boolean and SAP CHAR type. A Java value of true will be converted to "X", A
 * Java value of false will be converted to an empty String and vice versa.
 *
 * @author Carsten Erker
 */
@SuppressWarnings( "ConstantConditions" )
public class BooleanConverter implements Converter<Boolean, String> {

    /**
     * {@inheritDoc}
     */
    public Boolean convertToJava( String sapValue )
            throws ConversionException {
        if ( sapValue == null ) {
            throw new ConversionException( "SAP returned null" );
        }
        String value = sapValue.trim();
        if ( "X".equalsIgnoreCase( value ) ) {
            return TRUE;
        } else if ( value.length() == 0 ) {
            return FALSE;
        } else {
            throw new ConversionException( "Expected 'X' or '', but SAP returned '" + value + "'" );
        }
    }

    /**
     * {@inheritDoc}
     */
    public String convertToSap( Boolean javaValue )
            throws ConversionException {
        if ( javaValue == null ) {
            throw new ConversionException( "Java value is null" );
        }
        if ( !Boolean.class.isInstance( javaValue ) ) {
            throw new ConversionException( "Expected: " + Boolean.class.getName() + " but was: "
                                                   + javaValue.getClass().getName() );
        }
        return TRUE.equals( javaValue ) ? "X" : "";
    }
}
