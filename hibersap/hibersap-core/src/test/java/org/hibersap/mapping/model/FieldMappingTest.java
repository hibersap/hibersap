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
