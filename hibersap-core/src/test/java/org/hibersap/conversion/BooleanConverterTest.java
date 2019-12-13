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

package org.hibersap.conversion;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class BooleanConverterTest {

    private BooleanConverter converter = new BooleanConverter();

    @Test
    public void testConvertToJava() {
        assertEquals(true, converter.convertToJava("X"));
        assertEquals(true, converter.convertToJava("x"));
        assertEquals(true, converter.convertToJava(" X "));

        assertEquals(false, converter.convertToJava(""));
        assertEquals(false, converter.convertToJava(" "));

        assertConversionExceptionToJava("Y");
        assertConversionExceptionToJava(null);
    }

    @Test
    public void testConvertToSap() {
        assertEquals("X", converter.convertToSap(true));
        assertEquals("", converter.convertToSap(false));
        assertConversionExceptionToSap(null);
    }

    private void assertConversionExceptionToJava(String value) {
        try {
            converter.convertToJava(value);
            fail();
        } catch (ConversionException e) {
            // expected
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void assertConversionExceptionToSap(Boolean value) {
        try {
            converter.convertToSap(value);
            fail();
        } catch (ConversionException e) {
            // expected
        }
    }
}
