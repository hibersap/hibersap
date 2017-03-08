/*
 * Copyright (c) 2008-2017 akquinet tech@spree GmbH
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

public class CharConverterTest {

    private CharConverter converter = new CharConverter();

    @Test
    public void testConvertToJava() {
        assertEquals((Object) ' ', converter.convertToJava(null));
        assertEquals((Object) ' ', converter.convertToJava(" "));
        assertEquals((Object) 'A', converter.convertToJava("A"));
        assertEquals((Object) 'A', converter.convertToJava("AB"));
    }

    @Test
    public void testConvertToSap() {
        assertEquals("", converter.convertToSap(null));
        assertEquals(" ", converter.convertToSap(' '));
        assertEquals("A", converter.convertToSap('A'));
    }
}
