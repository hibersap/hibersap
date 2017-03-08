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

package org.hibersap.generation.bapi;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BapiFormatHelperTest {

    @Test
    public void getCamelCaseSmall()
            throws Exception {
        assertEquals("_", BapiFormatHelper.getCamelCaseSmall(null));
        assertEquals("_", BapiFormatHelper.getCamelCaseSmall(""));
        assertEquals("_x", BapiFormatHelper.getCamelCaseSmall("X"));
        assertEquals("_xY", BapiFormatHelper.getCamelCaseSmall("X_Y"));
        assertEquals("_xYZ", BapiFormatHelper.getCamelCaseSmall("X_Y_Z"));
        assertEquals("_myLittleField", BapiFormatHelper.getCamelCaseSmall("MY_LITTLE_FIELD"));
    }

    @Test
    public void getCamelCaseBig()
            throws Exception {
        assertEquals("", BapiFormatHelper.getCamelCaseBig(null));
        assertEquals("", BapiFormatHelper.getCamelCaseBig(""));
        assertEquals("X", BapiFormatHelper.getCamelCaseBig("X"));
        assertEquals("XY", BapiFormatHelper.getCamelCaseBig("X_Y"));
        assertEquals("XaYbZc", BapiFormatHelper.getCamelCaseBig("xa_yb_zc"));
        assertEquals("MyLittleClass", BapiFormatHelper.getCamelCaseBig("MY_LITTLE_CLASS"));
        assertEquals("MyLittleClass", BapiFormatHelper.getCamelCaseBig("_MY_LITTLE_CLASS_"));
    }
}
