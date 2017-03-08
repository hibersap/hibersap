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

import java.io.Serializable;
import org.hibersap.conversion.Converter;
import org.hibersap.conversion.ConverterCache;

/**
 * @author Carsten Erker
 */
public abstract class ParameterMapping implements Serializable {

    private static final long serialVersionUID = -2858494641560482982L;
    private final Class<?> associatedType;
    private final String sapName;
    private final String javaName;
    private final Class<? extends Converter> converterClass;

    public ParameterMapping(final Class<?> associatedType, final String sapName, final String javaName, final Class<? extends Converter> converterClass) {
        this.associatedType = associatedType;
        this.sapName = sapName;
        this.javaName = javaName;
        this.converterClass = converterClass;
    }

    public Class<?> getAssociatedType() {
        return associatedType;
    }

    public String getJavaName() {
        return this.javaName;
    }

    public abstract ParamType getParamType();

    public Class<? extends Converter> getConverterClass() {
        return this.converterClass;
    }

    public boolean hasConverter() {
        return this.converterClass != null;
    }

    protected final Object getConvertedValueToJava(final Object value, final ConverterCache converterCache) {
        Converter converter = converterCache.getConverter(getConverterClass());
        //noinspection unchecked
        return converter.convertToJava(value);
    }

    protected final Object getConvertedValueToSap(final Object value, final ConverterCache converterCache) {
        Converter converter = converterCache.getConverter(getConverterClass());
        //noinspection unchecked
        return converter.convertToSap(value);
    }

    protected abstract Object getUnconvertedValueToJava(final Object value, final ConverterCache converterCache);

    protected abstract Object getUnconvertedValueToSap(final Object value, final ConverterCache converterCache);

    public final Object mapToJava(final Object fieldMap, final ConverterCache converterCache) {
        if (hasConverter()) {
            return getConvertedValueToJava(fieldMap, converterCache);
        } else {
            return getUnconvertedValueToJava(fieldMap, converterCache);
        }
    }

    /**
     * @param value A plain value if the parameter is a simple one,
     * a bapi structure instance if the parameter is a structure,
     * a list of bapi structure instances if the parameter is a table.
     * @param converterCache Needed for conversion of parameters
     * @return A plain value if the parameter is a simple one,
     * a Map (SAP structure parameter name to plain values) if the parameter is a structure,
     * a List of Maps (SAP structure parameter name to plain values) if the parameter is a table.
     */
    public Object mapToSap(final Object value, final ConverterCache converterCache) {
        return hasConverter() ?
                getConvertedValueToSap(value, converterCache) :
                getUnconvertedValueToSap(value, converterCache);
    }

    public String getSapName() {
        return sapName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ParameterMapping that = (ParameterMapping) o;

        if (associatedType != null ? !associatedType.equals(that.associatedType) : that.associatedType != null) {
            return false;
        }
        if (converterClass != null ? !converterClass.equals(that.converterClass) : that.converterClass != null) {
            return false;
        }
        if (javaName != null ? !javaName.equals(that.javaName) : that.javaName != null) {
            return false;
        }
        //noinspection RedundantIfStatement
        if (sapName != null ? !sapName.equals(that.sapName) : that.sapName != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = associatedType != null ? associatedType.hashCode() : 0;
        result = 31 * result + (sapName != null ? sapName.hashCode() : 0);
        result = 31 * result + (javaName != null ? javaName.hashCode() : 0);
        result = 31 * result + (converterClass != null ? converterClass.hashCode() : 0);
        return result;
    }

    public enum ParamType {
        FIELD,
        STRUCTURE,
        TABLE
    }
}