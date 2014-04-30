/*
 * Copyright (c) 2008-2014 akquinet tech@spree GmbH
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

package org.hibersap.generation.bapi;

public final class BapiFormatHelper {

    private BapiFormatHelper() {
        // should not be instantiated
    }

    public static String getCamelCaseSmall( final String sapName ) {
        StringBuilder result = new StringBuilder( "_" );

        if ( sapName == null ) {
            return result.toString();
        }

        String[] parts = sapName.split( "_" );
        for ( int i = 0; i < parts.length; i++ ) {
            if ( i == 0 ) {
                result.append( parts[i].toLowerCase() );
            } else {
                result.append( parts[i].substring( 0, 1 ).toUpperCase() );
                result.append( parts[i].substring( 1 ).toLowerCase() );
            }
        }
        return result.toString();
    }

    public static String getCamelCaseBig( final String sapName ) {
        StringBuilder result = new StringBuilder( "" );

        if ( sapName == null ) {
            return result.toString();
        }

        String[] parts = sapName.split( "_" );
        for ( String part : parts ) {
            if ( part.length() > 0 ) {
                result.append( part.substring( 0, 1 ).toUpperCase() );
                result.append( part.substring( 1 ).toLowerCase() );
            }
        }
        return result.toString();
    }
}
