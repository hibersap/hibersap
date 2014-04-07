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

package org.hibersap.mapping.model;

import org.hibersap.MappingException;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;
import org.hibersap.conversion.BooleanConverter;
import org.hibersap.conversion.ConverterCache;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import static java.util.Collections.singletonMap;
import static org.fest.assertions.Assertions.assertThat;

public class TableMappingTest
{
    private StructureMapping structureMapping;
    private TableMapping tableMapping;

    @Before
    public void setUp() throws Exception
    {
        structureMapping = new StructureMapping( TestStructureBean.class, "sapStructureName", "javaStructureName",
                null );
        structureMapping.addParameter( new FieldMapping( Integer.class, "sapField1", "javaField1", null ) );
    }

    @Test
    public void destinationTypeIsArrayListByDefaultWhenFieldTypeIsList() throws Exception
    {
        tableMapping = new TableMapping( List.class, Object.class, "sapName", "javaName", structureMapping, null );

        Class destinationType = tableMapping.getDestinationType();

        assertThat( destinationType ).isSameAs( ArrayList.class );
    }

    @Test
    public void destinationTypeIsHashSetByDefaultWhenFieldTypeIsSet() throws Exception
    {
        tableMapping = new TableMapping( Set.class, Object.class, "sapName", "javaName", structureMapping, null );

        Class destinationType = tableMapping.getDestinationType();

        assertThat( destinationType ).isSameAs( HashSet.class );
    }

    @Test
    public void destinationTypeIsArrayListByDefaultWhenFieldTypeIsCollection() throws Exception
    {
        tableMapping = new TableMapping( Collection.class, Object.class, "sapName", "javaName", structureMapping,
                null );

        Class destinationType = tableMapping.getDestinationType();

        assertThat( destinationType ).isSameAs( ArrayList.class );
    }

    @Test
    public void destinationTypeIsSameAsFieldTypeWhenFieldTypeIsConcreteCollection() throws Exception
    {
        tableMapping = new TableMapping( TreeSet.class, Object.class, "sapName", "javaName", structureMapping, null );

        Class destinationType = tableMapping.getDestinationType();

        assertThat( destinationType ).isSameAs( TreeSet.class );
    }

    @Test
    public void destinationTypeIsArrayListWhenFieldTypeIsArray() throws Exception
    {
        tableMapping = new TableMapping( Object[].class, Object.class, "sapName", "javaName", structureMapping, null );

        Class destinationType = tableMapping.getDestinationType();

        assertThat( destinationType ).isSameAs( ArrayList.class );
    }

    @Test
    public void destinationTypeIsSameAsFieldTypeWhenFieldHasConverter() throws Exception
    {
        tableMapping = new TableMapping( Boolean.class, Boolean.class, "sapName", "javaName", structureMapping,
                BooleanConverter.class );

        Class destinationType = tableMapping.getDestinationType();

        assertThat( destinationType ).isSameAs( Boolean.class );
    }

    @Test( expected = MappingException.class )
    public void constructorThrowsMappingExceptionWhenFieldTypeIsUnsupportedCollection() throws Exception
    {
        new TableMapping( Queue.class, Object.class, "sapName", "javaName", structureMapping, null );
    }

    @Test( expected = MappingException.class )
    public void constructorThrowsMappingExceptionWhenFieldTypeIsNoCollectionOrArrayButConverterIsAttached()
    {
        new TableMapping( Object.class, Object.class, "sapName", "javaName", structureMapping, null );
    }

    @Test
    public void getUnconvertedValueReturnsListOfStructureBeansWithCorrectValues() throws Exception
    {
        tableMapping = new TableMapping( List.class, Integer.class, "sapName", "javaName", structureMapping, null );
        List<Map<String, ?>> tableMap = new ArrayList<Map<String, ?>>();
        tableMap.add( singletonMap( "sapField1", 1 ) );
        tableMap.add( singletonMap( "sapField1", 2 ) );

        Object value = tableMapping.getUnconvertedValueToJava( tableMap, new ConverterCache() );

        assertThat( value ).isInstanceOf( ArrayList.class );

        @SuppressWarnings( {"unchecked"} )
        List<TestStructureBean> structureBeans = ( List<TestStructureBean> ) value;
        assertThat( structureBeans ).hasSize( 2 );
        assertThat( structureBeans.get( 0 ).javaField1 ).isEqualTo( 1 );
        assertThat( structureBeans.get( 1 ).javaField1 ).isEqualTo( 2 );
    }

    @Test
    public void getUnconvertedValueReturnsArrayOfStructureBeansWhenDestinationTypeIsArray() throws Exception
    {
        tableMapping = new TableMapping( TestStructureBean[].class, TestStructureBean.class, "sapName", "javaName",
                structureMapping, null );
        List<Map<String, Integer>> tableMap = new ArrayList<Map<String, Integer>>();
        tableMap.add( singletonMap( "sapField1", 1 ) );
        tableMap.add( singletonMap( "sapField1", 2 ) );

        Object value = tableMapping.getUnconvertedValueToJava( tableMap, new ConverterCache() );

        assertThat( value ).isInstanceOf( TestStructureBean[].class );

        @SuppressWarnings( {"unchecked"} )
        TestStructureBean[] structureBeans = ( TestStructureBean[] ) value;
        assertThat( structureBeans ).hasSize( 2 );
        assertThat( structureBeans[0].javaField1 ).isEqualTo( 1 );
        assertThat( structureBeans[1].javaField1 ).isEqualTo( 2 );
    }

    @BapiStructure
    private static class TestStructureBean
    {
        @Parameter( "sapField1" )
        private Integer javaField1;

        private TestStructureBean()
        {
        }
    }
}
