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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class BooleanConverterTest {

    private BooleanConverter converter = new BooleanConverter();

    @Test
    public void testConvertToJava() {
        assertEquals( true, converter.convertToJava( "X" ) );
        assertEquals( true, converter.convertToJava( "x" ) );
        assertEquals( true, converter.convertToJava( " X " ) );

        assertEquals( false, converter.convertToJava( "" ) );
        assertEquals( false, converter.convertToJava( " " ) );

        assertConversionExceptionToJava( "Y" );
        assertConversionExceptionToJava( null );
    }

    @Test
    public void testConvertToSap() {
        assertEquals( "X", converter.convertToSap( true ) );
        assertEquals( "", converter.convertToSap( false ) );
        assertConversionExceptionToSap( null );
    }

    private void assertConversionExceptionToJava( String value ) {
        try {
            converter.convertToJava( value );
            fail();
        } catch ( ConversionException e ) {
            // expected
        }
    }

    private void assertConversionExceptionToSap( Boolean value ) {
        try {
            converter.convertToSap( value );
            fail();
        } catch ( ConversionException e ) {
            // expected
        }
    }
}
