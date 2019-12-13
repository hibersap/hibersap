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

package org.hibersap.execution;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.hibersap.bapi.BapiConstants;
import org.hibersap.conversion.ConverterCache;
import org.hibersap.mapping.ReflectionHelper;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.mapping.model.ParameterMapping;
import org.hibersap.mapping.model.TableMapping;

/**
 * @author Carsten Erker
 */
public class PojoMapper {

    private final ConverterCache converterCache;

    public PojoMapper(final ConverterCache converterCache) {
        this.converterCache = converterCache;
    }

    public void mapFunctionMapToPojo(final Object bapi, final Map<String, Object> functionMap, final BapiMapping bapiMapping) {
        Map<String, Object> map = getMapForAllParamTypes(functionMap);
        Set<ParameterMapping> paramMappings = bapiMapping.getAllParameters();
        functionMapToPojo(bapi, map, paramMappings);
    }

    public Map<String, Object> mapPojoToFunctionMap(final Object bapi, final BapiMapping bapiMapping) {
        Map<String, Object> functionMap = new HashMap<>();

        Set<ParameterMapping> imports = bapiMapping.getImportParameters();
        functionMap.put("IMPORT", pojoToMap(bapi, imports));

        Set<ParameterMapping> exports = bapiMapping.getExportParameters();
        functionMap.put("EXPORT", pojoToMap(bapi, exports));

        Set<ParameterMapping> changing = bapiMapping.getChangingParameters();
        functionMap.put("CHANGING", pojoToMap(bapi, changing));

        Set<TableMapping> tables = bapiMapping.getTableParameters();
        functionMap.put("TABLE", pojoToMap(bapi, tables));

        return functionMap;
    }

    private void functionMapToPojo(final Object bapi,
                                   final Map<String, Object> functionMap,
                                   final Set<ParameterMapping> paramMappings) {
        for (ParameterMapping paramMapping : paramMappings) {
            String fieldNameSap = paramMapping.getSapName();
            Object value = functionMap.get(fieldNameSap);

            if (value != null) {
                Object mappedValue = paramMapping.mapToJava(value, converterCache);
                ReflectionHelper.setFieldValue(bapi, paramMapping.getJavaName(), mappedValue);
            }
        }
    }

    private Map<String, Object> pojoToMap(final Object bapi, final Set<? extends ParameterMapping> paramMappings) {
        Map<String, Object> functionMap = new HashMap<>();

        for (ParameterMapping paramMapping : paramMappings) {
            String fieldNameJava = paramMapping.getJavaName();
            Object value = ReflectionHelper.getFieldValue(bapi, fieldNameJava);

            if (value != null) {
                Object subMap = paramMapping.mapToSap(value, converterCache);
                functionMap.put(paramMapping.getSapName(), subMap);
            }
        }
        return functionMap;
    }

    private Map<String, Object> getMapForAllParamTypes(final Map<String, Object> functionMap) {
        HashMap<String, Object> map = new HashMap<>();
        map.putAll(getMapNullSafe(functionMap, BapiConstants.IMPORT));
        map.putAll(getMapNullSafe(functionMap, BapiConstants.EXPORT));
        map.putAll(getMapNullSafe(functionMap, BapiConstants.CHANGING));
        map.putAll(getMapNullSafe(functionMap, BapiConstants.TABLE));
        return map;
    }

    private Map<String, Object> getMapNullSafe(final Map<String, Object> functionMap, final String paramName) {
        Map<String, Object> map = UnsafeCastHelper.castToMap(functionMap.get(paramName));
        return map == null ? new HashMap<>() : map;
    }
}
