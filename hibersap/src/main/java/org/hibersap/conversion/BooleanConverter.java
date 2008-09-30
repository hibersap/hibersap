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

/**
 * Converts between Java boolean and SAP CHAR type. A Java value of true will be
 * converted to "X", A Java value of false will be converted to an empty String and vice
 * versa.
 * 
 * @author Carsten Erker
 */
public class BooleanConverter
    implements Converter
{
    public Object convertToJava( Object sapValue )
        throws ConversionException
    {
        if ( sapValue == null )
        {
            throw new ConversionException( "SAP returned null" );
        }
        if ( !String.class.isInstance( sapValue ) )
        {
            throw new ConversionException( "Expected: " + String.class.getName() + " but was: "
                + sapValue.getClass().getName() );
        }
        String value = ( (String) sapValue ).trim();
        if ( "X".equalsIgnoreCase( value ) )
        {
            return Boolean.TRUE;
        }
        else if ( value.length() == 0 )
        {
            return Boolean.FALSE;
        }
        else
        {
            throw new ConversionException( "Expected 'X' or '', but SAP returned '" + value + "'" );
        }
    }

    public Object convertToSap( Object javaValue )
        throws ConversionException
    {
        if ( javaValue == null )
        {
            throw new ConversionException( "Java value is null" );
        }
        if ( !Boolean.class.isInstance( javaValue ) )
        {
            throw new ConversionException( "Expected: " + Boolean.class.getName() + " but was: "
                + javaValue.getClass().getName() );
        }
        boolean value = ( (Boolean) javaValue ).booleanValue();
        if ( value )
        {
            return "X";
        }
        else
        {
            return "";
        }
    }
}
