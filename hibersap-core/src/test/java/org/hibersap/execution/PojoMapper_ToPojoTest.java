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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibersap.conversion.ConverterCache;
import org.hibersap.mapping.AnnotationBapiMapper;
import org.hibersap.mapping.model.BapiMapping;
import org.junit.Before;
import org.junit.Test;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static org.fest.assertions.Assertions.assertThat;

public class PojoMapper_ToPojoTest {

    private MyTestBapi bapi;

    @Before
    public void setUp() throws Exception {
        AnnotationBapiMapper bapiMapper = new AnnotationBapiMapper();
        bapi = createEmptyBapiObject();
        Map<String, Object> functionMap = createFunctionMapForBapi();
        BapiMapping bapiMapping = bapiMapper.mapBapi(MyTestBapi.class);
        PojoMapper pojoMapper = new PojoMapper(new ConverterCache());

        pojoMapper.mapFunctionMapToPojo(bapi, functionMap, bapiMapping);
    }

    @Test
    public void mapsSimpleParameterWithoutConverter() {
        assertThat(bapi.importIntParam).isEqualTo(4711);
    }

    @Test
    public void mapsSimpleParameterWithConverter() {
        assertThat(bapi.exportIntParamWithConverter).isEqualTo("4712");
    }

    @Test
    public void mapsStructureParameterWithConverter() {
        assertThat(bapi.importStructureParamWithConverter).isEqualTo("c");
    }

    @Test
    public void mapsStructureParameterWithoutConverter() {
        assertThat(bapi.exportStructureParam.charParam).isEqualTo('d');
    }

    @Test
    public void mapsTableParameterWithoutConverter() {
        assertThat(bapi.tableParam).hasSize(2);
        assertThat(bapi.tableParam.get(0).charParam).isEqualTo('1');
        assertThat(bapi.tableParam.get(1).charParam).isEqualTo('2');
    }

    @Test
    public void mapsImportTableParameterWithoutConverter() {
        assertThat(bapi.importTableStructure).hasSize(2);
        assertThat(bapi.importTableStructure.get(0).charParam).isEqualTo('5');
        assertThat(bapi.importTableStructure.get(1).charParam).isEqualTo('6');
    }

    @Test
    public void mapsExportTableParameterWithoutConverter() {
        assertThat(bapi.exportTableStructure).hasSize(2);
        assertThat(bapi.exportTableStructure.get(0).charParam).isEqualTo('7');
        assertThat(bapi.exportTableStructure.get(1).charParam).isEqualTo('8');
    }

    @Test
    public void mapsChangingParameters() throws Exception {
        assertThat(bapi.changingDateParam).isEqualTo(new Date(0));
        assertThat(bapi.changingStructureParam.charParam).isEqualTo('e');
    }

    @Test
    public void mapsTableParameterWithConverter() {
        assertThat(bapi.tableParamWithConverter).isEqualTo("34");
    }

    @SuppressWarnings({"unchecked"})
    private Map<String, Object> createFunctionMapForBapi() {
        Map<String, Object> functionMap = createMap();

        Map<String, Object> importsMap = createMap();
        importsMap.put("intParam", 4711);
        importsMap.put("structureParamWithConverter", singletonMap("charParam", 'c'));

        List<Map<String, Character>> importTable = asList(
                singletonMap("charParam", '5'),
                singletonMap("charParam", '6'));
        importsMap.put("importTable", importTable);
        functionMap.put("IMPORT", importsMap);

        Map<String, Object> exportsMap = createMap();
        exportsMap.put("structureParam", singletonMap("charParam", 'd'));
        exportsMap.put("intParamWithConverter", 4712);

        List<Map<String, Character>> exportTable = asList(
                singletonMap("charParam", '7'),
                singletonMap("charParam", '8'));
        importsMap.put("exportTable", exportTable);
        functionMap.put("EXPORT", exportsMap);

        Map<String, Object> changingMap = createMap();
        changingMap.put("changingDateParam", new Date(0));
        changingMap.put("changingParamWithStructure", singletonMap("charParam", 'e'));
        functionMap.put("CHANGING", changingMap);

        Map<String, Object> tablesMap = createMap();
        List<Map<String, Character>> tableWithoutConverter = asList(
                singletonMap("charParam", '1'),
                singletonMap("charParam", '2'));
        List<Map<String, Character>> tableWithConverter = asList(
                singletonMap("charParam", '3'),
                singletonMap("charParam", '4'));
        tablesMap.put("tableParam", tableWithoutConverter);
        tablesMap.put("tableParamWithConverter", tableWithConverter);
        functionMap.put("TABLE", tablesMap);
        return functionMap;
    }

    private Map<String, Object> createMap() {
        return new HashMap<String, Object>();
    }

    private MyTestBapi createEmptyBapiObject() {
        return new MyTestBapi(null, null, null, null, null, null, null, null, null, null);
    }
}
