package org.hibersap.conversion;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CharConverterTest
{
    private CharConverter converter = new CharConverter();

    @Test
    public void convertToJava()
    {
        assertEquals( ' ', converter.convertToJava( null ) );
        assertEquals( ' ', converter.convertToJava( " " ) );
        assertEquals( 'A', converter.convertToJava( "A" ) );
        assertEquals( 'A', converter.convertToJava( "AB" ) );
    }

    @Test
    public void convertToSap()
    {
        assertEquals( "", converter.convertToSap( null ) );
        assertEquals( " ", converter.convertToSap( ' ' ) );
        assertEquals( "A", converter.convertToSap( 'A' ) );
    }
}
