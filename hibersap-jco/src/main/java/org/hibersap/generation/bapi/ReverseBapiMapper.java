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

package org.hibersap.generation.bapi;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFieldIterator;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoFunctionTemplate;
import com.sap.conn.jco.JCoParameterFieldIterator;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoRecord;
import java.util.List;
import java.util.Set;
import org.hibersap.HibersapException;
import org.hibersap.InternalHiberSapException;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.mapping.model.FieldMapping;
import org.hibersap.mapping.model.ParameterMapping;
import org.hibersap.mapping.model.StructureMapping;
import org.hibersap.mapping.model.TableMapping;
import org.hibersap.session.SessionManager;

@SuppressWarnings("PackageAccessibility")
public class ReverseBapiMapper {

    public BapiMapping map(final String bapiName, final SessionManager sessionManager) {
        JCoDestination destination;
        try {
            String sfName = sessionManager.getConfig().getName();
            destination = JCoDestinationManager.getDestination(sfName);

            JCoFunctionTemplate ft = destination.getRepository().getFunctionTemplate(bapiName);
            JCoFunction function = ft.getFunction();

            BapiMapping mapping = new BapiMapping(null, function.getName(), null);

            mapFields(mapping.getImportParameters(), function.getImportParameterList());
            mapFields(mapping.getExportParameters(), function.getExportParameterList());
            mapFields(mapping.getChangingParameters(), function.getChangingParameterList());
            mapFields(mapping.getTableParameters(), function.getTableParameterList());

            return mapping;
        } catch (JCoException e) {
            throw new HibersapException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private void mapFields(final Set<? extends ParameterMapping> set, final JCoParameterList jcoParams) {
        if (jcoParams == null) {
            return;
        }

        JCoParameterFieldIterator iter = jcoParams.getParameterFieldIterator();
        while (iter.hasNextField()) {
            JCoField field = iter.nextField();
            ParameterMapping param = getParameterMapping(field);

            if (ParameterMapping.ParamType.FIELD == param.getParamType()) {
                ((Set<FieldMapping>) set).add((FieldMapping) param);
            } else if (ParameterMapping.ParamType.STRUCTURE == param.getParamType()) {
                ((Set<StructureMapping>) set).add((StructureMapping) param);
            } else if (ParameterMapping.ParamType.TABLE == param.getParamType()) {
                ((Set<TableMapping>) set).add((TableMapping) param);
            }
        }
    }

    private ParameterMapping getParameterMapping(final JCoField field) {
        String javaFieldName = BapiFormatHelper.getCamelCaseSmall(field.getName());

        if (field.isStructure()) {
            StructureMapping structureMapping = new StructureMapping(null, field.getName(), javaFieldName, null);
            addFieldMappings(structureMapping, field.getStructure());
            return structureMapping;
        }
        if (field.isTable()) {
            StructureMapping structureMapping = new StructureMapping(null, field.getName(), javaFieldName, null);
            addFieldMappings(structureMapping, field.getTable());
            return new TableMapping(List.class, null, field.getName(), javaFieldName, structureMapping, null);
        }

        return new FieldMapping(getAssociatedClass(field), field.getName(), javaFieldName, null);
    }

    /**
     * Determins the Java type of the JCoField's value, i.e. of the value that is created by JCo when mapping
     * a parameter of a certain ABAP data type to the corresponding Java type.
     * The JCoField interface returns the canonical class name of the Java type, which is in the given context
     * mostly the same as the type returned by Class.getName(), however, the ABAP types X (raw byte field) and
     * XSTRING (variable length byte field) are mapped to byte[], where the canonical name ("byte[]") differs
     * from the class name ("[B").
     *
     * @param field The JCoField.
     * @return The class representing the field type.
     */
    private Class<?> getAssociatedClass(final JCoField field) {
        String canonicalClassName = field.getClassNameOfValue();

        if (byte[].class.getCanonicalName().equals(canonicalClassName)) {
            return byte[].class;
        }

        try {
            return Class.forName(canonicalClassName);
        } catch (ClassNotFoundException e) {
            throw new InternalHiberSapException("Can not get class for class name " + canonicalClassName, e);
        }
    }

    private void addFieldMappings(final StructureMapping structureMapping, final JCoRecord jCoRecord) {
        final JCoFieldIterator iter = jCoRecord.getFieldIterator();

        while (iter.hasNextField()) {
            ParameterMapping fieldParam = getParameterMapping(iter.nextField());
            structureMapping.addParameter(fieldParam);
        }
    }
}
