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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import org.hibersap.MappingException;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;
import org.hibersap.conversion.BooleanConverter;
import org.hibersap.conversion.ConverterCache;
import org.junit.Before;
import org.junit.Test;
import static java.util.Collections.singletonMap;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TableMappingTest {

    private StructureMapping structureMapping;
    private TableMapping tableMapping;

    @Before
    public void setUp() throws Exception {
        structureMapping = new StructureMapping(TestStructureBean.class, "sapStructureName", "javaStructureName",
                null);
        structureMapping.addParameter(new FieldMapping(Integer.class, "sapField", "javaField", null));
    }

    @Test
    public void destinationTypeIsArrayListByDefaultWhenFieldTypeIsList() throws Exception {
        tableMapping = new TableMapping(List.class, Object.class, "sapName", "javaName", structureMapping, null);

        Class destinationType = tableMapping.getDestinationType();

        assertThat(destinationType).isSameAs(ArrayList.class);
    }

    @Test
    public void destinationTypeIsHashSetByDefaultWhenFieldTypeIsSet() throws Exception {
        tableMapping = new TableMapping(Set.class, Object.class, "sapName", "javaName", structureMapping, null);

        Class destinationType = tableMapping.getDestinationType();

        assertThat(destinationType).isSameAs(HashSet.class);
    }

    @Test
    public void destinationTypeIsArrayListByDefaultWhenFieldTypeIsCollection() throws Exception {
        tableMapping = new TableMapping(Collection.class, Object.class, "sapName", "javaName", structureMapping,
                null);

        Class destinationType = tableMapping.getDestinationType();

        assertThat(destinationType).isSameAs(ArrayList.class);
    }

    @Test
    public void destinationTypeIsSameAsFieldTypeWhenFieldTypeIsConcreteCollection() throws Exception {
        tableMapping = new TableMapping(TreeSet.class, Object.class, "sapName", "javaName", structureMapping, null);

        Class destinationType = tableMapping.getDestinationType();

        assertThat(destinationType).isSameAs(TreeSet.class);
    }

    @Test
    public void destinationTypeIsArrayListWhenFieldTypeIsArray() throws Exception {
        tableMapping = new TableMapping(Object[].class, Object.class, "sapName", "javaName", structureMapping, null);

        Class destinationType = tableMapping.getDestinationType();

        assertThat(destinationType).isSameAs(ArrayList.class);
    }

    @Test
    public void destinationTypeIsSameAsFieldTypeWhenFieldHasConverter() throws Exception {
        tableMapping = new TableMapping(Boolean.class, Boolean.class, "sapName", "javaName", structureMapping,
                BooleanConverter.class);

        Class destinationType = tableMapping.getDestinationType();

        assertThat(destinationType).isSameAs(Boolean.class);
    }

    @Test(expected = MappingException.class)
    public void constructorThrowsMappingExceptionWhenFieldTypeIsUnsupportedCollection() throws Exception {
        new TableMapping(Queue.class, Object.class, "sapName", "javaName", structureMapping, null);
    }

    @Test(expected = MappingException.class)
    public void constructorThrowsMappingExceptionWhenFieldTypeIsNoCollectionOrArrayButConverterIsAttached() {
        new TableMapping(Object.class, Object.class, "sapName", "javaName", structureMapping, null);
    }

    @Test
    public void getUnconvertedValueReturnsListOfStructureBeansWithCorrectValues() throws Exception {
        tableMapping = new TableMapping(List.class, Integer.class, "sapName", "javaName", structureMapping, null);
        List<Map<String, ?>> tableMap = new ArrayList<Map<String, ?>>();
        tableMap.add(singletonMap("sapField", 1));
        tableMap.add(singletonMap("sapField", 2));

        Object value = tableMapping.getUnconvertedValueToJava(tableMap, new ConverterCache());

        assertThat(value).isInstanceOf(ArrayList.class);

        @SuppressWarnings({"unchecked"})
        List<TestStructureBean> structureBeans = (List<TestStructureBean>) value;
        assertThat(structureBeans).hasSize(2);
        assertThat(structureBeans.get(0).javaField).isEqualTo(1);
        assertThat(structureBeans.get(1).javaField).isEqualTo(2);
    }

    @Test
    public void getUnconvertedValueReturnsArrayOfStructureBeansWhenDestinationTypeIsArray() throws Exception {
        tableMapping = new TableMapping(TestStructureBean[].class, TestStructureBean.class, "sapName", "javaName",
                structureMapping, null);
        List<Map<String, Integer>> tableMap = new ArrayList<Map<String, Integer>>();
        tableMap.add(singletonMap("sapField", 1));
        tableMap.add(singletonMap("sapField", 2));

        Object value = tableMapping.getUnconvertedValueToJava(tableMap, new ConverterCache());

        assertThat(value).isInstanceOf(TestStructureBean[].class);

        @SuppressWarnings({"unchecked"})
        TestStructureBean[] structureBeans = (TestStructureBean[]) value;
        assertThat(structureBeans).hasSize(2);
        assertThat(structureBeans[0].javaField).isEqualTo(1);
        assertThat(structureBeans[1].javaField).isEqualTo(2);
    }

    @Test
    public void getUnconvertedValueReturnsListOfStructureBeansWhenTableParameterIsProvidedAsResultSetByRA() throws Exception {
        tableMapping = new TableMapping(List.class, Integer.class, "sapName", "javaName", structureMapping, null);
        String[] columnNames = {"sapField"};
        Object[][] data = {{1}, {2}};
        ResultSet resultSet = resultSet(columnNames, data);

        Object value = tableMapping.getUnconvertedValueToJava(resultSet, new ConverterCache());

        assertThat(value).isInstanceOf(ArrayList.class);

        @SuppressWarnings({"unchecked"})
        List<TestStructureBean> structureBeans = (List<TestStructureBean>) value;
        assertThat(structureBeans).hasSize(2);
        assertThat(structureBeans.get(0).javaField).isEqualTo(1);
        assertThat(structureBeans.get(1).javaField).isEqualTo(2);
    }

    private ResultSet resultSet(String[] columnNames, Object[][] data) throws SQLException {
        return (ResultSet) Proxy.newProxyInstance(
                ResultSet.class.getClassLoader(),
                new Class[]{ResultSet.class},
                new ResultSetInvocationHandler(columnNames, data));
    }

    @BapiStructure
    private static class TestStructureBean {

        @Parameter("sapField")
        private Integer javaField;

        private TestStructureBean() {
        }
    }

    /**
     * Needed to create a {@link ResultSet}
     */
    private static class ResultSetInvocationHandler implements InvocationHandler {

        private final Object[][] data;

        private int currentRow = -1;

        private ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        private ResultSetInvocationHandler(String[] columnNames, Object[][] data) throws SQLException {
            when(metaData.getColumnCount()).thenReturn(columnNames.length);
            for (int i = 0; i < columnNames.length; i++) {
                when(metaData.getColumnName(i + 1)).thenReturn(columnNames[i]);
            }
            this.data = data;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ("next".equals(method.getName())) {
                return next();
            }
            if ("getObject".equals(method.getName())) {
                return getObject((Integer) args[0]);
            }
            if ("getMetaData".equals(method.getName())) {
                return metaData;
            }
            return null;
        }

        private Object getObject(int column) {
            return data[currentRow][column - 1];
        }

        private boolean next() {
            if (data.length > currentRow + 1) {
                currentRow++;
                return true;
            }
            return false;
        }
    }
}
