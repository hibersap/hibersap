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

package org.hibersap.mapping.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.hibersap.conversion.Converter;
import org.hibersap.conversion.ConverterCache;
import org.hibersap.execution.UnsafeCastHelper;
import org.hibersap.mapping.ReflectionHelper;

/**
 * @author Carsten Erker
 */
public final class StructureMapping extends ParameterMapping {

    private static final long serialVersionUID = 2930405767657861801L;

    private final Set<ParameterMapping> parameters = new HashSet<ParameterMapping>();

    public StructureMapping(final Class<?> associatedClass, final String sapName, final String javaName, final Class<? extends Converter> converterClass) {
        super(associatedClass, sapName, javaName, converterClass);
    }

    public void addParameter(final ParameterMapping param) {
        parameters.add(param);
    }

    public Set<ParameterMapping> getParameters() {
        return parameters;
    }

    @Override
    public ParamType getParamType() {
        return ParamType.STRUCTURE;
    }

    @Override
    protected Object getUnconvertedValueToJava(final Object fieldMap, final ConverterCache converterCache) {
        Map<String, Object> subMap = UnsafeCastHelper.castToMap(fieldMap);
        Object subBean = ReflectionHelper.newInstance(getAssociatedType());

        for (ParameterMapping parameter : parameters) {
            Object fieldValue = subMap.get(parameter.getSapName());

            if (fieldValue != null) {
                Object value = parameter.mapToJava(fieldValue, converterCache);
                ReflectionHelper.setFieldValue(subBean, parameter.getJavaName(), value);
            }
        }

        return subBean;
    }

    @Override
    protected Object getUnconvertedValueToSap(final Object bapiStructure, final ConverterCache converterCache) {
        HashMap<String, Object> functionMap = new HashMap<String, Object>();

        for (ParameterMapping parameter : parameters) {
            Object fieldValue = ReflectionHelper.getFieldValue(bapiStructure, parameter.getJavaName());

            if (fieldValue != null) {
                Object value = parameter.mapToSap(fieldValue, converterCache);
                functionMap.put(parameter.getSapName(), value);
            }
        }

        return functionMap;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        StructureMapping that = (StructureMapping) o;

        //noinspection RedundantIfStatement
        if (parameters != null ? !parameters.equals(that.parameters) : that.parameters != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        return result;
    }
}
