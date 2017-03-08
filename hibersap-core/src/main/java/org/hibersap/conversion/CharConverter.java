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

import org.apache.commons.lang.StringUtils;

/**
 * Converts between SAP character fields of length 1 and Java char fields.
 *
 * @author Carsten Erker
 */
public class CharConverter implements Converter<Character, String> {

    /**
     * {@inheritDoc}
     */
    public Character convertToJava( String sapValue ) throws ConversionException {
        if ( StringUtils.isEmpty( sapValue ) ) {
            return ' ';
        }
        return sapValue.charAt( 0 );
    }

    /**
     * {@inheritDoc}
     */
    public String convertToSap( Character javaValue ) throws ConversionException {
        if ( javaValue == null ) {
            return "";
        }
        return "" + javaValue;
    }
}
