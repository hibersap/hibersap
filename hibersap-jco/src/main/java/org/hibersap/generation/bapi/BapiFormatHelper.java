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

package org.hibersap.generation.bapi;

public class BapiFormatHelper
{
    private BapiFormatHelper()
    {
        // should not be instantiated
    }

    public static String getCamelCaseSmall( String sapName )
    {
        StringBuffer result = new StringBuffer( "_" );

        if ( sapName == null )
        {
            return result.toString();
        }

        String[] parts = sapName.split( "_" );
        for ( int i = 0; i < parts.length; i++ )
        {
            if ( i == 0 )
            {
                result.append( parts[i].toLowerCase() );
            }
            else
            {
                result.append( parts[i].substring( 0, 1 ).toUpperCase() );
                result.append( parts[i].substring( 1 ).toLowerCase() );
            }
        }
        return result.toString();
    }

    public static String getCamelCaseBig( String sapName )
    {
        StringBuffer result = new StringBuffer( "" );

        if ( sapName == null )
        {
            return result.toString();
        }

        String[] parts = sapName.split( "_" );
        for ( int i = 0; i < parts.length; i++ )
        {
            if ( parts[i].length() > 0 )
            {
                result.append( parts[i].substring( 0, 1 ).toUpperCase() );
                result.append( parts[i].substring( 1 ).toLowerCase() );
            }
        }
        return result.toString();
    }

}
