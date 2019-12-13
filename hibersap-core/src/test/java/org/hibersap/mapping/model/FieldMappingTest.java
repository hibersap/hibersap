/*
 * Copyright (c) 2008-2019 akquinet tech@spree GmbH
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

package org.hibersap.mapping.model;

import org.hibersap.conversion.ConversionException;
import org.hibersap.conversion.Converter;
import org.hibersap.conversion.ConverterCache;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class FieldMappingTest {

    private final FieldMapping mapping = new FieldMapping(String.class, "sapName", "javaName", FailingConverterStub.class);
    private final ConverterCache converterCache = new ConverterCache();

    @Test
    public void getUnconvertedValueToJavaReturnsIdenticalValue() {
        Object sapValue = new Object();
        Object javaValue = mapping.getUnconvertedValueToJava(sapValue, converterCache);

        assertThat(javaValue).isSameAs(sapValue);
    }

    @Test
    public void getUnconvertedValueToSapReturnsIdenticalValue() {
        Object javaValue = new Object();
        Object sapValue = mapping.getUnconvertedValueToSap(javaValue, converterCache);

        assertThat(sapValue).isSameAs(javaValue);
    }

    private static class FailingConverterStub implements Converter<Object, Object> {

        public Object convertToJava(Object sapValue) throws ConversionException {
            throw new AssertionError("convertToJava() should not be called");
        }

        public Object convertToSap(Object javaValue) throws ConversionException {
            throw new AssertionError("convertToSap() should not be called");
        }
    }
}
