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
class MyTestBapi
{
    @SuppressWarnings( {"UnusedDeclaration"} ) // called by Hibersap using reflection
    private MyTestBapi()
    {
    }

    MyTestBapi( Integer intParam, String intParamWithConverter, TestStructure structureParam,
                String structureParamWithConverter, List<TestStructure> tableParam, String tableParamWithConverter )
    {
        this.intParam = intParam;
        this.intParamWithConverter = intParamWithConverter;
        this.structureParam = structureParam;
        this.structureParamWithConverter = structureParamWithConverter;
        this.tableParam = tableParam;
        this.tableParamWithConverter = tableParamWithConverter;
    }

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

    @BapiStructure
    static class TestStructure
    {
        @Parameter( "charParam" )
        char charParam;

        @SuppressWarnings( "unused" )
        private TestStructure()
        {
            // for Hibersap
        }

        public TestStructure( char charParam )
        {
            this.charParam = charParam;
        }
    }

    private static class IntegerToStringConverter implements Converter<String, Integer>
    {
        public String convertToJava( Integer sapValue ) throws ConversionException
        {
            return sapValue.toString();
        }

        public Integer convertToSap( String javaValue ) throws ConversionException
        {
            return Integer.valueOf( javaValue );
        }
    }

    private static class TestStructureTableToStringConverter
            implements Converter<String, Collection<Map<String, Object>>>
    {
        public String convertToJava( Collection<Map<String, Object>> sapValue ) throws ConversionException
        {
            StringBuilder result = new StringBuilder();
            for ( Map<String, Object> row : sapValue )
            {
                result.append( row.get( "charParam" ) );
            }
            return result.toString();
        }

        public Collection<Map<String, Object>> convertToSap( String javaValue ) throws ConversionException
        {
            ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

            for ( char aChar : javaValue.toCharArray() )
            {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put( "charParam", aChar );
                list.add( map );
            }

            return list;
        }
    }

    private static class TestStructureToStringConverter implements Converter<String, Map<String, Object>>
    {
        public String convertToJava( Map<String, Object> sapValue ) throws ConversionException
        {
            return "" + sapValue.get( "charParam" );
        }

        public Map<String, Object> convertToSap( String javaValue ) throws ConversionException
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put( "charParam", javaValue.charAt( 0 ) );
            return map;
        }
    }
}
