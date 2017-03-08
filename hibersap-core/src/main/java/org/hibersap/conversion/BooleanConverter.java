/*
 * Copyright (c) 2008-2017 akquinet tech@spree GmbH
 *
 * This file is part of Hibersap.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
    public Boolean convertToJava( final String sapValue )
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
    public String convertToSap( final Boolean javaValue )
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
