/*
 * Copyright (c) 2008-2012 akquinet tech@spree GmbH
 *
 * This file is part of Hibersap.
 *
 * Hibersap is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Hibersap is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Hibersap. If
 * not, see <http://www.gnu.org/licenses/>.
 */

package org.hibersap.generation.bapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

public class BapiClassFormatterTest
{
    private final BapiClassFormatter formatter = new BapiClassFormatter();

    private final AnnotationBapiMapper mapper = new AnnotationBapiMapper();

    @Test
    public void createClass()
        throws Exception
    {
        BapiMapping bapiMapping = mapper.mapBapi( BapiTest.class );
        Map<String, String> classes = formatter.createClasses( bapiMapping, "org.hibersap.generated.test" );

        assertNotNull( classes );
        assertEquals( 3, classes.size() );

        assertTrue( classes.containsKey( "BapiTest" ) );
        assertTrue( classes.containsKey( "TestStructure" ) );
        assertTrue( classes.containsKey( "TestTable" ) );
    }

    @SuppressWarnings("unused")
    @Bapi("BAPI_TEST")
    private class BapiTest
    {
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
    private class TestStructure
    {
        @Parameter("STRUCT_STRING")
        private String _structString;

        @Parameter("STRUCT_INT")
        private int _structInt;
    }

    @SuppressWarnings("unused")
    @BapiStructure
    private class TestTable
    {
        @Parameter("TABLE_STRING")
        private String _tableString;

        @Parameter("TABLE_INT")
        private int _tableInt;
    }
}
