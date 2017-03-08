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

package org.hibersap.generation.bapi;

import java.util.List;
import java.util.Map;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.annotations.Table;
import org.hibersap.mapping.AnnotationBapiMapper;
import org.hibersap.mapping.model.BapiMapping;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BapiClassFormatterTest {

    private final BapiClassFormatter formatter = new BapiClassFormatter();

    private final AnnotationBapiMapper mapper = new AnnotationBapiMapper();

    @Test
    public void createClass()
            throws Exception {
        BapiMapping bapiMapping = mapper.mapBapi(BapiTest.class);
        Map<String, String> classes = formatter.createClasses(bapiMapping, "org.hibersap.generated.test");

        assertNotNull( classes );
        assertEquals( 4, classes.size() );

        assertTrue( classes.containsKey( "BapiTest" ) );
        assertTrue( classes.containsKey( "TestStructure" ) );
        assertTrue( classes.containsKey( "TestTable" ) );
        assertTrue( classes.containsKey( "TestSubStructure" ) );
    }

    @SuppressWarnings("unused")
    @Bapi("BAPI_TEST")
    private class BapiTest {

        @Import
        @Parameter("TEST_STRING")
        private String _testString;

        @Import
        @Parameter("TEST_INT")
        private int _testInt;

        @Export
        @Parameter(value = "TEST_STRUCTURE", type = ParameterType.STRUCTURE)
        private TestStructure _testStructure;

        @Table
        @Parameter("TEST_TABLE")
        private List<TestTable> _testTable;
    }

    @SuppressWarnings("unused")
    @BapiStructure
    private class TestStructure {

        @Parameter("STRUCT_STRING")
        private String _structString;

        @Parameter("STRUCT_INT")
        private int _structInt;

        @Parameter( value = "TEST_SUB_STRUCTURE", type = ParameterType.STRUCTURE )
        private TestSubStructure _subStruct;
    }

    @SuppressWarnings("unused")
    @BapiStructure
    private class TestTable {

        @Parameter("TABLE_STRING")
        private String _tableString;

        @Parameter("TABLE_INT")
        private int _tableInt;
    }

    @SuppressWarnings( "unused" )
    @BapiStructure
    private class TestSubStructure {

        @Parameter( "STRUCT_STRING" )
        private String _structString;

        @Parameter( "STRUCT_INT" )
        private int _structInt;
    }

}
