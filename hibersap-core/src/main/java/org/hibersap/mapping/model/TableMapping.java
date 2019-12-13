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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Generated;
import org.hibersap.InternalHiberSapException;
import org.hibersap.MappingException;
import org.hibersap.conversion.Converter;
import org.hibersap.conversion.ConverterCache;
import org.hibersap.mapping.ReflectionHelper;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hibersap.execution.UnsafeCastHelper.castToCollectionOfMaps;
import static org.hibersap.mapping.ReflectionHelper.newCollectionInstance;

/**
 * @author Carsten Erker
 */
public final class TableMapping extends ParameterMapping {

    private static final long serialVersionUID = 6134694196341208013L;

    private final StructureMapping componentParameter;

    private final Class<?> fieldType;

    private final Class<?> destinationType;

    /**
     * @param fieldType          The type of the field in the bean; may be a Collection interface like List,
     *                           Set, Collection, a concrete class that implements Collection or an array.
     *                           If there is a Converter specified on the field, it may be a Pojo class.
     * @param associatedType     The type of the elements, i.e. a Pojo class.
     * @param sapName            The table's name in SAP.
     * @param javaName           The Java field name of the Collection or array.
     * @param componentParameter A StructureMapping containing the table's fields.
     * @param converterClass     The Class of the table field's converter, if defined.
     */
    public TableMapping(final Class<?> fieldType,
                        final Class<?> associatedType,
                        final String sapName,
                        final String javaName,
                        final StructureMapping componentParameter,
                        final Class<? extends Converter<?, ?>> converterClass) {
        super(associatedType, sapName, javaName, converterClass);
        this.componentParameter = componentParameter;
        this.fieldType = fieldType;
        this.destinationType = determineDestinationType();
    }

    private Class<?> determineDestinationType() {
        Class<?> resultingType;

        if (isDestinationTypeCollection()) {
            if (fieldType.isInterface()) {
                if (List.class.equals(fieldType)) {
                    resultingType = ArrayList.class;
                } else if (Set.class.equals(fieldType)) {
                    resultingType = HashSet.class;
                } else if (Collection.class.equals(fieldType)) {
                    resultingType = ArrayList.class;
                } else {
                    throw new MappingException(
                            "Collection of type " + fieldType.getName() + " not supported. See Field "
                                    + getJavaName()
                    );
                }
            } else {
                resultingType = fieldType;
            }
        } else if (fieldType.isArray()) {
            resultingType = ArrayList.class;
        } else {
            if (hasConverter()) {
                resultingType = fieldType;
            } else {
                throw new MappingException("The field " + getJavaName() + " must be an array or a "
                        + "Collection or have a Converter, but is: " + fieldType.getName());
            }
        }

        return resultingType;
    }

    private boolean isDestinationTypeCollection() {
        return Collection.class.isAssignableFrom(fieldType);
    }

    public Class<?> getDestinationType() {
        return this.destinationType;
    }

    public StructureMapping getComponentParameter() {
        return componentParameter;
    }

    public Class<?> getFieldType() {
        return this.fieldType;
    }

    @Override
    public ParamType getParamType() {
        return ParamType.TABLE;
    }

    @Override
    public Object getUnconvertedValueToJava(final Object fieldMapCollection, final ConverterCache converterCache) {
        if (!hasConverter()) {
            // must be Collection, since there is no Converter
            @SuppressWarnings({"unchecked"})
            Class<? extends Collection<Object>> destinationType = (Class<? extends Collection<Object>>) getDestinationType();

            Collection<Object> collection = newCollectionInstance(destinationType);

            Collection<Map<String, Object>> rows;

            if (fieldMapCollection instanceof ResultSet) {
                try {
                    rows = resultSetToArrayList((ResultSet) fieldMapCollection);
                } catch (SQLException e) {
                    throw new InternalHiberSapException("Could not convert ResultSet");
                }
            } else {
                rows = castToCollectionOfMaps(fieldMapCollection);
            }

            if (rows != null) {
                for (Map<String, Object> tableMap : rows) {
                    Object elementBean = getComponentParameter().mapToJava(tableMap, converterCache);
                    collection.add(elementBean);
                }
            }

            if (getFieldType().isArray()) {
                return ReflectionHelper.newArrayFromCollection(collection, getAssociatedType());
            } else {
                return collection;
            }
        } else {
            throw new InternalHiberSapException(
                    "This method should only be called by the framework " +
                            "when the corresponding table field has a converter attached"
            );
        }
    }

    @SuppressWarnings(value = "all")
    private List<Map<String, Object>> resultSetToArrayList(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();

        int columns = metaData.getColumnCount();
        if (metaData == null || columns == 0) {

            return emptyList();
        }

        List<Map<String, Object>> list = new ArrayList();

        while (resultSet.next()) {
            HashMap row = new HashMap(columns);
            for (int i = 1; i <= columns; ++i) {

                row.put(metaData.getColumnName(i), resultSet.getObject(i));
            }
            list.add(row);
        }
        resultSet.close();

        return list;
    }

    @Override
    protected Object getUnconvertedValueToSap(final Object value, final ConverterCache converterCache) {
        @SuppressWarnings("unchecked") Collection<Object> bapiStructures
                = getFieldType().isArray() ? singletonList(value) : (Collection<Object>) value;
        List<Map<String, Object>> tableRows = new ArrayList<>();

        if (bapiStructures != null) {
            for (Object bapiStructure : bapiStructures) {
                @SuppressWarnings({"unchecked"})
                Map<String, Object> paramMap =
                        (Map<String, Object>) getComponentParameter().mapToSap(bapiStructure, converterCache);
                tableRows.add(paramMap);
            }
        }

        return tableRows;
    }

    @Override
    @Generated("")
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

        TableMapping that = (TableMapping) o;

        if (destinationType != null ? !destinationType.equals(that.destinationType) : that.destinationType != null) {
            return false;
        }
        if (componentParameter != null ? !componentParameter.equals(that.componentParameter) :
                that.componentParameter != null) {
            return false;
        }
        //noinspection RedundantIfStatement
        if (fieldType != null ? !fieldType.equals(that.fieldType) : that.fieldType != null) {
            return false;
        }

        return true;
    }

    @Override
    @Generated("")
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (componentParameter != null ? componentParameter.hashCode() : 0);
        result = 31 * result + (fieldType != null ? fieldType.hashCode() : 0);
        result = 31 * result + (destinationType != null ? destinationType.hashCode() : 0);
        return result;
    }
}
