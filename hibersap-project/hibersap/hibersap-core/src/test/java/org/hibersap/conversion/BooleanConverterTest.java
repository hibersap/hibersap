package org.hibersap.conversion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class BooleanConverterTest
{
    private BooleanConverter converter = new BooleanConverter();

    @Test
    public void testConvertToJava()
    {
        assertEquals( true, converter.convertToJava( "X" ) );
        assertEquals( true, converter.convertToJava( "x" ) );
        assertEquals( true, converter.convertToJava( " X " ) );

        assertEquals( false, converter.convertToJava( "" ) );
        assertEquals( false, converter.convertToJava( " " ) );

        assertConversionExceptionToJava( "Y" );
        assertConversionExceptionToJava( null );
        assertConversionExceptionToJava( new Integer( 0 ) );
    }

    @Test
    public void testConvertToSap()
    {
        assertEquals( "X", converter.convertToSap( true ) );
        assertEquals( "", converter.convertToSap( false ) );
        assertConversionExceptionToSap( null );
        assertConversionExceptionToSap( new Integer( 0 ) );
    }

    private void assertConversionExceptionToJava( Object value )
    {
        try
        {
            converter.convertToJava( value );
            fail();
        }
        catch ( ConversionException e )
        {
            // expected
        }
    }

    private void assertConversionExceptionToSap( Object value )
    {
        try
        {
            converter.convertToSap( value );
            fail();
        }
        catch ( ConversionException e )
        {
            // expected
        }
    }
}
