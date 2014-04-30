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

package org.hibersap.execution;

import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Convert;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.annotations.Table;
import org.hibersap.annotations.ThrowExceptionOnError;
import org.hibersap.conversion.ConversionException;
import org.hibersap.conversion.Converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Name must not be Test* or *Test, because Surefire then tries to run it as a
 * test case.
 *
 * @author Carsten Erker
 */
@Bapi( "bapiName" )
@ThrowExceptionOnError
class MyTestBapi {

    @Import
    @Parameter( "intParam" )
    Integer intParam;
    @Import
    @Parameter( value = "structureParamWithConverter", type = ParameterType.STRUCTURE )
    @Convert( converter = TestStructureToStringConverter.class )
    String structureParamWithConverter;
    @Export
    @Parameter( value = "structureParam", type = ParameterType.STRUCTURE )
    TestStructure structureParam;
    @Export
    @Parameter( "intParamWithConverter" )
    @Convert( converter = IntegerToStringConverter.class )
    String intParamWithConverter;
    @Table
    @Parameter( "tableParam" )
    List<TestStructure> tableParam;
    @Table
    @Parameter( "tableParamWithConverter" )
    @Convert( converter = TestStructureTableToStringConverter.class )
    String tableParamWithConverter;

    @SuppressWarnings( {"UnusedDeclaration"} ) // called by Hibersap using reflection
    private MyTestBapi() {
    }

    MyTestBapi( Integer intParam, String intParamWithConverter, TestStructure structureParam,
                String structureParamWithConverter, List<TestStructure> tableParam, String tableParamWithConverter ) {
        this.intParam = intParam;
        this.intParamWithConverter = intParamWithConverter;
        this.structureParam = structureParam;
        this.structureParamWithConverter = structureParamWithConverter;
        this.tableParam = tableParam;
        this.tableParamWithConverter = tableParamWithConverter;
    }

    @BapiStructure
    static class TestStructure {

        @Parameter( "charParam" )
        char charParam;

        @SuppressWarnings( "unused" )
        private TestStructure() {
            // for Hibersap
        }

        public TestStructure( char charParam ) {
            this.charParam = charParam;
        }
    }

    private static class IntegerToStringConverter implements Converter<String, Integer> {

        public String convertToJava( Integer sapValue ) throws ConversionException {
            return sapValue.toString();
        }

        public Integer convertToSap( String javaValue ) throws ConversionException {
            return Integer.valueOf( javaValue );
        }
    }

    private static class TestStructureTableToStringConverter
            implements Converter<String, Collection<Map<String, Object>>> {

        public String convertToJava( Collection<Map<String, Object>> sapValue ) throws ConversionException {
            StringBuilder result = new StringBuilder();
            for ( Map<String, Object> row : sapValue ) {
                result.append( row.get( "charParam" ) );
            }
            return result.toString();
        }

        public Collection<Map<String, Object>> convertToSap( String javaValue ) throws ConversionException {
            ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

            for ( char aChar : javaValue.toCharArray() ) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put( "charParam", aChar );
                list.add( map );
            }

            return list;
        }
    }

    private static class TestStructureToStringConverter implements Converter<String, Map<String, Object>> {

        public String convertToJava( Map<String, Object> sapValue ) throws ConversionException {
            return "" + sapValue.get( "charParam" );
        }

        public Map<String, Object> convertToSap( String javaValue ) throws ConversionException {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put( "charParam", javaValue.charAt( 0 ) );
            return map;
        }
    }
}
