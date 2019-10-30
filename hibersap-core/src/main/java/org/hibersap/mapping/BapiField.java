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

package org.hibersap.mapping;

import java.lang.reflect.Field;
import java.util.Collection;
import org.hibersap.annotations.Changing;
import org.hibersap.annotations.Convert;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.annotations.Table;
import org.hibersap.conversion.Converter;

/**
 * @author Carsten Erker
 */
class BapiField {

    private static final Class<Parameter> PARAM = Parameter.class;

    private static final Class<Import> IMPORT = Import.class;

    private static final Class<Export> EXPORT = Export.class;

    private static final Class<Changing> CHANGING = Changing.class;

    private static final Class<Table> TABLE = Table.class;

    private static final Class<Convert> CONVERT = Convert.class;

    private final Field field;

    public BapiField(final Field field) {
        this.field = field;
    }

    public Class<?> getArrayType() {
        Class<?> associatedType = field.getType();
        return ReflectionHelper.getArrayType(associatedType);
    }

    /**
     * @return The type.
     */
    public Class<?> getAssociatedType() {
        if (field.getType().isArray()) {
            return getArrayType();
        }
        if (Collection.class.isAssignableFrom(field.getType())) {
            return getGenericType();
        }
        return getType();
    }

    public Class<? extends Converter> getConverter() {
        if (field.isAnnotationPresent(CONVERT)) {
            Convert convert = field.getAnnotation(CONVERT);
            return convert.converter();
        }
        return null;
    }

    public Class<?> getGenericType() {
        return ReflectionHelper.getGenericType(field);
    }

    public String getName() {
        return field.getName();
    }

    private Parameter getParameterAnnotation() {
        return field.getAnnotation(PARAM);
    }

    public String getSapName() {
        return getParameterAnnotation().value();
    }

    public Class<?> getType() {
        return field.getType();
    }

    public boolean isExport() {
        return field.isAnnotationPresent(EXPORT);
    }

    public boolean isChanging() {
        return field.isAnnotationPresent(CHANGING);
    }

    public boolean isImport() {
        return field.isAnnotationPresent(IMPORT);
    }

    public boolean isParameter() {
        return field.isAnnotationPresent(PARAM);
    }

    public boolean isStructure() {
        return isParameter() && getParameterAnnotation().type() == ParameterType.STRUCTURE;
    }

    /**
     * Check if the field's type equals to {@link org.hibersap.annotations.ParameterType#TABLE_STRUCTURE}.
     *
     * @return true if the field is of the type {@link org.hibersap.annotations.ParameterType#TABLE_STRUCTURE}
     */
    public boolean isTableStructure() {
        return isParameter() && getParameterAnnotation().type() == ParameterType.TABLE_STRUCTURE;
    }

    public boolean isTable() {
        return field.isAnnotationPresent(TABLE);
    }
}
