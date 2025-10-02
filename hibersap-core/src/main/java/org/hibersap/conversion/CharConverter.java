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

import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Converts between SAP character fields of length 1 and Java char fields.
 *
 * @author Carsten Erker
 */
@NullMarked
public class CharConverter implements Converter<Character, String> {

    /**
     * {@inheritDoc}
     */
    public Character convertToJava(@Nullable String sapValue) {
        if (StringUtils.isEmpty(sapValue)) {
            return ' ';
        }
        return sapValue.charAt(0);
    }

    /**
     * {@inheritDoc}
     */
    public String convertToSap(@Nullable Character javaValue) {
        if (javaValue == null) {
            return "";
        }
        return "" + javaValue;
    }
}
