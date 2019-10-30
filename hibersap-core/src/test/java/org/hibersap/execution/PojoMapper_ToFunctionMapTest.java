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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hibersap.conversion.ConverterCache;
import org.hibersap.execution.MyTestBapi.TestStructure;
import org.hibersap.mapping.AnnotationBapiMapper;
import org.hibersap.mapping.model.BapiMapping;
import org.junit.Before;
import org.junit.Test;
import static java.util.Collections.singletonMap;
import static org.fest.assertions.Assertions.assertThat;
import static org.hibersap.execution.UnsafeCastHelper.castToCollectionOfMaps;
import static org.hibersap.execution.UnsafeCastHelper.castToMap;

public class PojoMapper_ToFunctionMapTest {

    private final PojoMapper pojoMapper = new PojoMapper(new ConverterCache());
    private AnnotationBapiMapper bapiMapper = new AnnotationBapiMapper();

    private Map<String, Object> functionMap;

    @Before
    public void setUp() throws Exception {
        MyTestBapi bapi = createTestBapi();
        BapiMapping bapiMapping = bapiMapper.mapBapi(MyTestBapi.class);

        functionMap = pojoMapper.mapPojoToFunctionMap(bapi, bapiMapping);
    }

    @Test
    public void mapsAllImportParameters() {
        Map<String, Object> importParams = castToMap(functionMap.get("IMPORT"));

        assertThat(importParams).hasSize(3);
    }

    @Test
    public void mapsAllExportParameters() {
        Map<String, Object> exportParams = castToMap(functionMap.get("EXPORT"));

        assertThat(exportParams).hasSize(3);
    }

    @Test
    public void mapsAllChangingParameters() {
        Map<String, Object> changingParams = castToMap(functionMap.get("CHANGING"));

        assertThat(changingParams).hasSize(2);
    }

    @Test
    public void mapsChangingDateParameter() {
        Map<String, Object> changingParams = castToMap(functionMap.get("CHANGING"));

        assertThat(changingParams.get("changingDateParam")).isEqualTo(new Date(0));
    }

    @Test
    public void mapsChangingStructureParameter() {
        Map<String, Object> changingParams = castToMap(functionMap.get("CHANGING"));
        Map<String, Object> structure = castToMap(changingParams.get("changingParamWithStructure"));
        Object parameter = structure.get("charParam");

        assertThat(parameter).isEqualTo('d');
    }

    @Test
    public void mapsAllTableParameters() {
        Map<String, Object> tableParams = castToMap(functionMap.get("TABLE"));

        assertThat(tableParams).hasSize(2);
    }

    @Test
    public void mapsSimpleParameterWithoutConverter() {
        Map<String, Object> importParams = castToMap(functionMap.get("IMPORT"));

        assertThat(importParams.get("intParam")).isEqualTo(4711);
    }

    @Test
    public void mapsSimpleParameterWithConverter() {
        Map<String, Object> exportParams = castToMap(functionMap.get("EXPORT"));
        Object parameter = exportParams.get("intParamWithConverter");

        assertThat(parameter).isInstanceOf(Integer.class);
        assertThat(parameter).isEqualTo(4712);
    }

    @Test
    public void mapsStructureParameterWithConverter() {
        Map<String, Object> importParams = castToMap(functionMap.get("IMPORT"));
        Map<String, Object> structure = castToMap(importParams.get("structureParamWithConverter"));
        Object parameter = structure.get("charParam");

        assertThat(parameter).isInstanceOf(Character.class);
        assertThat(parameter).isEqualTo('c');
    }

    @Test
    public void mapsStructureParameterWithoutConverter() {
        Map<String, Object> exportParams = castToMap(functionMap.get("EXPORT"));
        Object parameter = exportParams.get("structureParam");
        Map<String, Object> structureMap = castToMap(parameter);

        assertThat(structureMap).hasSize(1);
        assertThat(structureMap.get("charParam")).isEqualTo('c');
    }

    @Test
    public void mapsTableParameterWithoutConverter() {
        Map<String, Object> tableParams = castToMap(functionMap.get("TABLE"));
        Collection<Map<String, Object>> tableParam = castToCollectionOfMaps(tableParams.get("tableParam"));

        assertThat(tableParam).containsOnly(singletonMap("charParam", '1'), singletonMap("charParam", '2'));
    }

    @Test
    public void mapsImportTableParameterWithoutConverter() {
        Map<String, Object> importParams = castToMap(functionMap.get("IMPORT"));
        Collection<Map<String, Object>> tableParam = castToCollectionOfMaps(importParams.get("importTable"));

        assertThat(tableParam).containsOnly(singletonMap("charParam", '3'), singletonMap("charParam", '4'));
    }

    @Test
    public void mapsExportTableParameterWithoutConverter() {
        Map<String, Object> exportParams = castToMap(functionMap.get("EXPORT"));
        Collection<Map<String, Object>> tableParam = castToCollectionOfMaps(exportParams.get("exportTable"));

        assertThat(tableParam).containsOnly(singletonMap("charParam", '5'), singletonMap("charParam", '6'));
    }

    @Test
    public void mapsTableParameterWithConverter() {
        Map<String, Object> tableParams = castToMap(functionMap.get("TABLE"));
        Collection<Map<String, Object>> tableParam = castToCollectionOfMaps(tableParams.get("tableParamWithConverter"));

        assertThat(tableParam).containsOnly(singletonMap("charParam", '3'), singletonMap("charParam", '4'));
    }

    @Test
    public void doesNotMapParametersWithNullValues() {
        MyTestBapi bapi = new MyTestBapi(null, null, null, null, null, null, null, null, null, null);
        BapiMapping bapiMapping = bapiMapper.mapBapi(MyTestBapi.class);

        Map<String, Object> functionMap = pojoMapper.mapPojoToFunctionMap(bapi, bapiMapping);

        Map<String, Object> importParams = castToMap(functionMap.get("IMPORT"));
        Map<String, Object> exportParams = castToMap(functionMap.get("EXPORT"));
        Map<String, Object> tableParams = castToMap(functionMap.get("TABLE"));
        assertThat(importParams).hasSize(0);
        assertThat(exportParams).hasSize(0);
        assertThat(tableParams).hasSize(0);
    }

    private MyTestBapi createTestBapi() {
        TestStructure structure = new TestStructure('c');
        TestStructure changingStructure = new TestStructure('d');
        TestStructure tableStructure1 = new TestStructure('1');
        TestStructure tableStructure2 = new TestStructure('2');
        List<TestStructure> table = new ArrayList<TestStructure>();
        table.add(tableStructure1);
        table.add(tableStructure2);

        List<TestStructure> importTable = new ArrayList<TestStructure>();
        importTable.add(new TestStructure('3'));
        importTable.add(new TestStructure('4'));

        List<TestStructure> exportTable = new ArrayList<TestStructure>();
        exportTable.add(new TestStructure('5'));
        exportTable.add(new TestStructure('6'));

        return new MyTestBapi(4711, "4712", structure, "c", table, "34", changingStructure, new Date(0), importTable, exportTable);
    }
}
