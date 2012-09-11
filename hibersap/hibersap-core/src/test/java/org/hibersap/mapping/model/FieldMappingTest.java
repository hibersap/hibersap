package org.hibersap.mapping.model;

import org.hibersap.conversion.ConversionException;
import org.hibersap.conversion.Converter;
import org.hibersap.conversion.ConverterCache;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

public class FieldMappingTest
{
    private final FieldMapping mapping = new FieldMapping( String.class, "sapName", "javaName",
            FailingConverterStub.class );
    private final ConverterCache converterCache = new ConverterCache();

    @Test
    public void getUnconvertedValueToJavaReturnsIdenticalValue() throws Exception
    {
        Object sapValue = new Object();
        Object javaValue = mapping.getUnconvertedValueToJava( sapValue, converterCache );

        assertThat(javaValue).isSameAs( sapValue );
    }

    @Test
    public void getUnconvertedValueToSapReturnsIdenticalValue() throws Exception
    {
        Object javaValue = new Object();
        Object sapValue = mapping.getUnconvertedValueToSap( javaValue, converterCache );

        assertThat(sapValue).isSameAs( javaValue );
    }

    private static class FailingConverterStub implements Converter<Object, Object>
    {
        public Object convertToJava( Object sapValue ) throws ConversionException
        {
            throw fail( "convertToJava() should not be called" );
        }

        public Object convertToSap( Object javaValue ) throws ConversionException
        {
            throw fail( "convertToSap() should not be called" );
        }
    }
}
