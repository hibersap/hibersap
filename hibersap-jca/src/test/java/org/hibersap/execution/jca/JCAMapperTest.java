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

package org.hibersap.execution.jca;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.resource.ResourceException;
import javax.resource.cci.IndexedRecord;
import javax.resource.cci.MappedRecord;
import javax.resource.cci.RecordFactory;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.mapping.model.ErrorHandling;
import org.hibersap.mapping.model.FieldMapping;
import org.hibersap.mapping.model.StructureMapping;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hibersap.execution.UnsafeCastHelper.castToMap;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JCAMapperTest {

    private final JCAMapper mapper = new JCAMapper();

    private final RecordFactory recordFactory = mock(RecordFactory.class);

    @Before
    public void setUp()
            throws ResourceException {
        when(recordFactory.createIndexedRecord(any())).thenAnswer(new IndexedRecordAnswer());
        when(recordFactory.createMappedRecord(any())).thenAnswer(new MappedRecordAnswer());
    }

    @Test
    public void mapToFunctionMap() {
        Map<String, Object> functionMap = createFunctionMap();
        Map<String, Object> resultRecord = new HashMap<>();

        addExportAndTableParameters(resultRecord);

        // map
        mapper.mapRecordToFunctionMap(functionMap, resultRecord, createBapiMapping());

        assertEquals(4, functionMap.size());

        // check import parameters still there?
        assertEquals(3, castToMap(functionMap.get("IMPORT")).size());

        // check export parameters
        Map<String, Object> exportParams = castToMap(functionMap.get("EXPORT"));
        assertEquals(3, exportParams.size());

        Map<String, Object> exportParam1 = castToMap(exportParams.get("EXPORT_PARAM1"));
        assertEquals("structField1", exportParam1.get("STRUCT_FIELD1"));
        assertEquals(2, exportParam1.get("STRUCT_FIELD2"));
        assertEquals("exportParam2", exportParams.get("EXPORT_PARAM2"));
        assertEquals(new Date(3), exportParams.get("EXPORT_PARAM3"));

        // check changing parameters
        Map<String, Object> changingParams = castToMap(functionMap.get("CHANGING"));
        assertEquals(2, changingParams.size());
        assertEquals("changingParam1-changed", changingParams.get("CHANGING_PARAM1"));
        assertEquals(22, changingParams.get("CHANGING_PARAM2"));

        // check table parameters
        Map<String, Object> tableParams = castToMap(functionMap.get("TABLE"));
        assertEquals(2, tableParams.size());

        List<?> table2 = (List<?>) tableParams.get("TABLE_PARAM2");
        assertEquals(2, table2.size());

        Map<String, Object> row1 = castToMap(table2.get(0));
        assertEquals("tableField1_1", row1.get("TABLE_FIELD1"));
        assertEquals(12, row1.get("TABLE_FIELD2"));

        Map<String, Object> row2 = castToMap(table2.get(1));
        assertEquals("tableField2_1", row2.get("TABLE_FIELD1"));
        assertEquals(22, row2.get("TABLE_FIELD2"));
    }

    @Test
    public void mapToMappedRecord()
            throws Exception {
        // map
        Map<String, Object> functionMap = createFunctionMap();
        MappedRecord mappedRecord = mapper.mapFunctionMapValuesToMappedRecord("BAPI_NAME", recordFactory, functionMap);

        assertEquals("BAPI_NAME", mappedRecord.getRecordName());

        // check import parameters
        String importParam1 = (String) mappedRecord.get("IMPORT_PARAM2");
        assertEquals("importParam2", importParam1);

        int param3 = (Integer) mappedRecord.get("IMPORT_PARAM3");
        assertEquals(3, param3);

        MappedRecord importStruct = (MappedRecord) mappedRecord.get("IMPORT_PARAM1");
        assertEquals("IMPORT_PARAM1", importStruct.getRecordName());
        assertEquals(1, importStruct.get("STRUCT_FIELD1"));
        assertEquals("structField2", importStruct.get("STRUCT_FIELD2"));

        // check changing parameters
        assertThat(mappedRecord.get("CHANGING_PARAM1")).isEqualTo("changingParam1");
        assertThat(mappedRecord.get("CHANGING_PARAM2")).isEqualTo(2);

        // check table parameters
        IndexedRecord tableParam1 = (IndexedRecord) mappedRecord.get("TABLE_PARAM1");
        assertEquals("TABLE_PARAM1", tableParam1.getRecordName());

        MappedRecord row1 = (MappedRecord) tableParam1.get(0);
        assertEquals("tableField1", row1.get("TABLE_FIELD1"));
        assertEquals(new Date(1), row1.get("TABLE_FIELD2"));
        assertEquals(1, row1.get("TABLE_FIELD3"));

        MappedRecord row2 = (MappedRecord) tableParam1.get(1);
        assertEquals("tableField2", row2.get("TABLE_FIELD1"));
        assertEquals(new Date(2), row2.get("TABLE_FIELD2"));
        assertEquals(2, row2.get("TABLE_FIELD3"));
    }

    private Map<String, Object> createFunctionMap() {
        Map<String, Object> functionMap = new HashMap<>();
        Map<String, Object> importMap = new HashMap<>();
        Map<String, Object> exportMap = new HashMap<>();
        Map<String, Object> changingMap = new HashMap<>();
        Map<String, Object> tableMap = new HashMap<>();

        functionMap.put("IMPORT", importMap);
        functionMap.put("EXPORT", exportMap);
        functionMap.put("CHANGING", changingMap);
        functionMap.put("TABLE", tableMap);

        // create import parameters
        Map<String, Object> importStructMap = new HashMap<>();
        importStructMap.put("STRUCT_FIELD1", 1);
        importStructMap.put("STRUCT_FIELD2", "structField2");

        importMap.put("IMPORT_PARAM1", importStructMap);
        importMap.put("IMPORT_PARAM2", "importParam2");
        importMap.put("IMPORT_PARAM3", 3);

        // create changing parameter
        changingMap.put("CHANGING_PARAM1", "changingParam1");
        changingMap.put("CHANGING_PARAM2", 2);

        // create table parameters
        List<Map<String, Object>> table1 = new ArrayList<>();
        table1.add(getTableRow("tableField1", new Date(1), 1));
        table1.add(getTableRow("tableField2", new Date(2), 2));

        tableMap.put("TABLE_PARAM1", table1);
        return functionMap;
    }

    @SuppressWarnings("unchecked")
    private void addExportAndTableParameters(Map<String, Object> resultRecord) {
        // add export parameters
        MyMappedRecord exportStruct1 = new MyMappedRecord("EXPORT_PARAM1");
        exportStruct1.put("STRUCT_FIELD1", "structField1");
        exportStruct1.put("STRUCT_FIELD2", 2);

        resultRecord.put("EXPORT_PARAM1", exportStruct1);
        resultRecord.put("EXPORT_PARAM2", "exportParam2");
        resultRecord.put("EXPORT_PARAM3", new Date(3));

        // add table parameter
        MyMappedRecord tableRowRecord1 = new MyMappedRecord("TABLE_PARAM2:row:0");
        tableRowRecord1.put("TABLE_FIELD1", "tableField1_1");
        tableRowRecord1.put("TABLE_FIELD2", 12);

        MyMappedRecord tableRowRecord2 = new MyMappedRecord("TABLE_PARAM2:row:1");
        tableRowRecord2.put("TABLE_FIELD1", "tableField2_1");
        tableRowRecord2.put("TABLE_FIELD2", 22);

        MyIndexedRecord tableRecord = new MyIndexedRecord("TABLE_PARAM2");
        tableRecord.add(tableRowRecord1);
        tableRecord.add(tableRowRecord2);

        resultRecord.put("TABLE_PARAM2", tableRecord);

        // change changing parameters
        resultRecord.put("CHANGING_PARAM1", "changingParam1-changed");
        resultRecord.put("CHANGING_PARAM2", 22);
    }

    private BapiMapping createBapiMapping() {
        // needed to detect if a field is an export or a changing parameter
        BapiMapping bapiMapping = new BapiMapping(Object.class, "BAPI_NAME", new ErrorHandling("/EXPORT/RETURN", new String[0]));
        // Export parameters
        StructureMapping struct1 = new StructureMapping(Object.class, "EXPORT_PARAM1", "", null);
        struct1.addParameter(new FieldMapping(String.class, "STRUCT_FIELD1", "", null));
        struct1.addParameter(new FieldMapping(int.class, "STRUCT_FIELD2", "", null));
        bapiMapping.addExportParameter(struct1);
        bapiMapping.addExportParameter(new FieldMapping(String.class, "EXPORT_PARAM2", "", null));
        bapiMapping.addExportParameter(new FieldMapping(Date.class, "EXPORT_PARAM3", "", null));

        // Changing parameters
        bapiMapping.addChangingParameter(new FieldMapping(String.class, "CHANGING_PARAM1", "", null));
        bapiMapping.addChangingParameter(new FieldMapping(int.class, "CHANGING_PARAM2", "", null));

        return bapiMapping;
    }

    private Map<String, Object> getTableRow(String param1, Date param2, int param3) {
        Map<String, Object> row = new HashMap<>();
        row.put("TABLE_FIELD1", param1);
        row.put("TABLE_FIELD2", param2);
        row.put("TABLE_FIELD3", param3);
        return row;
    }

    static final class IndexedRecordAnswer implements Answer<IndexedRecord> {

        @Override
        public IndexedRecord answer(InvocationOnMock invocation) {
            String name = invocation.getArgument(0);
            return new MyIndexedRecord(name);
        }
    }

    static final class MappedRecordAnswer implements Answer<MappedRecord> {

        @Override
        public MappedRecord answer(InvocationOnMock invocation) {
            String name = invocation.getArgument(0);
            return new MyMappedRecord(name);
        }
    }
}
