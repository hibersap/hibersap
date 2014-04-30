/*
 * Copyright (c) 2008-2014 akquinet tech@spree GmbH
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

import org.hibersap.annotations.Parameter;
import org.hibersap.conversion.ConverterCache;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static org.hibersap.execution.UnsafeCastHelper.castToMap;

public class StructureMappingTest {

    private final ConverterCache converterCache = new ConverterCache();

    private StructureMapping structureMapping;

    @Before
    public void createStructureMapping() throws Exception {
        structureMapping = new StructureMapping( TestStructureBean.class, "sapName", "javaName",
                                                 null );
        structureMapping.addParameter( new FieldMapping( Integer.class, "sapField1", "javaField1", null ) );
        structureMapping.addParameter( new FieldMapping( float.class, "sapField2", "javaField2", null ) );
    }

    @Test
    public void getUnconvertedValueToJavaMapsFields() throws Exception {
        HashMap<String, Object> fieldMap = new HashMap<String, Object>();
        fieldMap.put( "sapField1", Integer.MAX_VALUE );
        fieldMap.put( "sapField2", Float.MAX_VALUE );

        Object value = structureMapping.getUnconvertedValueToJava( fieldMap, converterCache );

        assertThat( value ).isInstanceOf( TestStructureBean.class );
        TestStructureBean bean = (TestStructureBean) value;
        assertThat( bean.javaField1 ).isEqualTo( Integer.MAX_VALUE );
        assertThat( bean.javaField2 ).isEqualTo( Float.MAX_VALUE );
    }

    @Test
    public void getUnconvertedValueToSapMapsFields() throws Exception {
        TestStructureBean bean = new TestStructureBean();
        bean.javaField1 = Integer.MAX_VALUE;
        bean.javaField2 = Float.MAX_VALUE;

        Object value = structureMapping.getUnconvertedValueToSap( bean, converterCache );

        assertThat( value ).isInstanceOf( Map.class );
        Map<String, Object> map = castToMap( value );
        assertThat( map.get( "sapField1" ) ).isEqualTo( Integer.MAX_VALUE );
        assertThat( map.get( "sapField2" ) ).isEqualTo( Float.MAX_VALUE );
    }

    private static class TestStructureBean {

        @Parameter( "sapField1" )
        private Integer javaField1;

        @Parameter( "sapField2" )
        private float javaField2;

        private TestStructureBean() {
        }
    }
}
