package org.hibersap.conversion;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DefaultConverterTest
{
    private DefaultConverter converter = new DefaultConverter();

    @Test
    public void convertToJava()
    {
        assertEquals( null, converter.convertToJava( null ) );
        assertEquals( "", converter.convertToJava( "" ) );
        assertEquals( new Integer( -1 ), converter.convertToJava( new Integer( -1 ) ) );
        assertEquals( -1, converter.convertToJava( -1 ) );
    }

    @Test
    public void convertToSap()
    {
        assertEquals( null, converter.convertToSap( null ) );
        assertEquals( "", converter.convertToSap( "" ) );
        assertEquals( new Integer( -1 ), converter.convertToSap( new Integer( -1 ) ) );
        assertEquals( -1, converter.convertToSap( -1 ) );
    }
}
