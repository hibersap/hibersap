/*
 * Copyright (c) 2008-2025 tech@spree GmbH
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

import org.hibersap.conversion.Converter;
import org.hibersap.conversion.ConverterCache;

/**
 * @author Carsten Erker
 */
public class FieldMapping extends ParameterMapping {

    private static final long serialVersionUID = -7542970603293850477L;

    public FieldMapping(final Class<?> associatedClass,
                        final String sapName,
                        final String javaName,
                        final Class<? extends Converter<?, ?>> converter) {
        super(associatedClass, sapName, javaName, converter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParamType getParamType() {
        return ParamType.FIELD;
    }

    @Override
    protected Object getUnconvertedValueToJava(final Object value, final ConverterCache converterCache) {
        return value;
    }

    @Override
    protected Object getUnconvertedValueToSap(final Object value, final ConverterCache converterCache) {
        return value;
    }
}
