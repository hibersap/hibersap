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

import org.hibersap.HibersapException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ConverterCacheTest {

    private ConverterCache cache = new ConverterCache();

    @Test
    public void testCreatesOneInstanceOfEachClass() {
        cache.getConverter(CharConverter.class);
        assertEquals(1, cache.getSize());

        cache.getConverter(CharConverter.class);
        assertEquals(1, cache.getSize());

        cache.getConverter(BooleanConverter.class);
        assertEquals(2, cache.getSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThrowsExceptionOnNullArgument() {
        cache.getConverter(null);
    }

    @Test(expected = HibersapException.class)
    public void testThrowsHibersapExceptionIfNotInstantiable() {
        cache.getConverter(NotInstantiableConverter.class);
    }

    private static abstract class NotInstantiableConverter implements Converter<Object, Object> {
        // never created
    }
}
