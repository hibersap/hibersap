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

package org.hibersap.execution.jca;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.resource.ResourceException;
import javax.resource.cci.IndexedRecord;
import javax.resource.cci.MappedRecord;
import javax.resource.cci.Record;
import javax.resource.cci.RecordFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.bapi.BapiConstants;
import org.hibersap.execution.UnsafeCastHelper;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.mapping.model.ParameterMapping;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hibersap.bapi.BapiConstants.CHANGING;
import static org.hibersap.bapi.BapiConstants.EXPORT;
import static org.hibersap.bapi.BapiConstants.TABLE;

/**
 * @author M. Dahm
 */
public class JCAMapper {

    private static final Log LOG = LogFactory.getLog(JCAMapper.class);

    public MappedRecord mapFunctionMapValuesToMappedRecord(final String bapiName,
                                                           final RecordFactory recordFactory,
                                                           final Map<String, Object> functionMap) throws ResourceException {
        LOG.debug("mapFunctionMapValuesToMappedRecord() functionMap=" + functionMap);

        MappedRecord mappedInputRecord = recordFactory.createMappedRecord(bapiName);

        final Map<String, Object> importMap = UnsafeCastHelper.castToMap(functionMap.get(BapiConstants.IMPORT));
        mapToMappedRecord(recordFactory, mappedInputRecord, importMap);

        final Map<String, Object> changingMap = UnsafeCastHelper.castToMap(functionMap.get(BapiConstants.CHANGING));
        mapToMappedRecord(recordFactory, mappedInputRecord, changingMap);

        final Map<String, Object> tableMap = UnsafeCastHelper.castToMap(functionMap.get(TABLE));
        mapToMappedRecord(recordFactory, mappedInputRecord, tableMap);

        LOG.debug("mapFunctionMapValuesToMappedRecord() record=" + mappedInputRecord);

        return mappedInputRecord;
    }

    public void mapRecordToFunctionMap(final Map<String, Object> functionMap,
                                       final Map<String, Object> resultRecordMap,
                                       final BapiMapping bapiMapping) {
        LOG.debug("mapRecordToFunctionMap() recordMap=" + resultRecordMap);

        for (final Entry<String, Object> entry : resultRecordMap.entrySet()) {
            final Object recordValue = entry.getValue();
            final String recordKey = entry.getKey();

            if (recordValue != null) {
                LOG.debug("mapping " + recordValue.getClass().getName() + ": " + recordKey + "=" + recordValue);
            }

            if (recordValue instanceof IndexedRecord) {
                // Table parameter
                final IndexedRecord indexedResultRecord = (IndexedRecord) recordValue;
                List<Map<String, Object>> table = new ArrayList<>();

                for (Object object : indexedResultRecord) {
                    MappedRecord mr = (MappedRecord) object;
                    final Map<String, Object> line = new HashMap<>();

                    @SuppressWarnings("unchecked")
                    Set<String> keys = mr.keySet();

                    for (String key : keys) {
                        line.put(key, mr.get(key));
                    }
                    table.add(line);
                }

                Map<String, Object> tables = UnsafeCastHelper.castToMap(functionMap.get(TABLE));
                tables.put(indexedResultRecord.getRecordName(), table);
            } else {
                // Simple parameter or structure parameter (the latter is a MappedRecord which luckily implements Map)
                String parameterType = getParameterType(recordKey, bapiMapping);
                if (isNotBlank(parameterType)) {
                    Map<String, Object> export = UnsafeCastHelper.castToMap(functionMap.get(parameterType));
                    export.put(recordKey, recordValue);
                } else {
                    LOG.debug("Skipping unmapped parameter: " + recordKey);
                }
            }
        }
        LOG.debug("mapRecordToFunctionMap() functionMap=" + functionMap);
    }

    private String getParameterType(String parameterName, BapiMapping bapiMapping) {
        for (ParameterMapping exportParameter : bapiMapping.getExportParameters()) {
            if (exportParameter.getSapName().equalsIgnoreCase(parameterName)) {
                return EXPORT;
            }
        }
        for (ParameterMapping changingParameter : bapiMapping.getChangingParameters()) {
            if (changingParameter.getSapName().equalsIgnoreCase(parameterName)) {
                return CHANGING;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void appendToRecord(final Record mappedInputRecord, final String fieldName, final Object value) {
        if (mappedInputRecord instanceof IndexedRecord) {
            ((IndexedRecord) mappedInputRecord).add(value);
        } else {
            ((MappedRecord) mappedInputRecord).put(fieldName, value);
        }
    }

    @SuppressWarnings("unchecked")
    private void mapToMappedRecord(final RecordFactory recordFactory, final Record mappedInputRecord,
            final Map<String, Object> map) throws ResourceException {
        for (final String fieldName : map.keySet()) {
            final Object value = map.get(fieldName);

            if (Map.class.isAssignableFrom(value.getClass())) {
                final Map<String, Object> structureMap = UnsafeCastHelper.castToMap(value);
                final Record structure = recordFactory.createMappedRecord(fieldName);

                appendToRecord(mappedInputRecord, fieldName, structure);

                mapToMappedRecord(recordFactory, structure, structureMap);
            } else if (Collection.class.isAssignableFrom(value.getClass())) {
                final Collection<Map<String, Object>> tableMap = UnsafeCastHelper.castToCollectionOfMaps(value);
                final IndexedRecord table = recordFactory.createIndexedRecord(fieldName);

                appendToRecord(mappedInputRecord, fieldName, table);

                int i = 0;
                for (final Map<String, Object> row : tableMap) {
                    MappedRecord rowRecord = recordFactory.createMappedRecord(fieldName + ":row:" + i);
                    mapToMappedRecord(recordFactory, rowRecord, row);
                    table.add(rowRecord);
                    i++;
                }
            } else {
                appendToRecord(mappedInputRecord, fieldName, value);
            }
        }
    }
}
