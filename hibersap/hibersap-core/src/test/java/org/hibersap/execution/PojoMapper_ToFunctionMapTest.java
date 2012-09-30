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

import org.hibersap.conversion.ConverterCache;
import org.hibersap.execution.MyTestBapi.TestStructure;
import org.hibersap.mapping.AnnotationBapiMapper;
import org.hibersap.mapping.model.BapiMapping;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.fest.assertions.Assertions.assertThat;
import static org.hibersap.execution.UnsafeCastHelper.castToCollectionOfMaps;
import static org.hibersap.execution.UnsafeCastHelper.castToMap;

public class PojoMapper_ToFunctionMapTest
{
    private final PojoMapper pojoMapper = new PojoMapper( new ConverterCache() );
    private AnnotationBapiMapper bapiMapper = new AnnotationBapiMapper();

    private Map<String, Object> functionMap;

    @Before
    public void setUp() throws Exception
    {
        MyTestBapi bapi = createTestBapi();
        BapiMapping bapiMapping = bapiMapper.mapBapi( MyTestBapi.class );

        functionMap = pojoMapper.mapPojoToFunctionMap( bapi, bapiMapping );
    }

    @Test
    public void mapsAllImportParameters()
    {
        Map<String, Object> importParams = castToMap( functionMap.get( "IMPORT" ) );

        assertThat( importParams ).hasSize( 2 );
    }

    @Test
    public void mapsAllExportParameters()
    {
        Map<String, Object> exportParams = castToMap( functionMap.get( "EXPORT" ) );

        assertThat( exportParams ).hasSize( 2 );
    }

    @Test
    public void mapsAllTableParameters()
    {
        Map<String, Object> tableParams = castToMap( functionMap.get( "TABLE" ) );

        assertThat( tableParams ).hasSize( 2 );
    }

    @Test
    public void mapsSimpleParameterWithoutConverter()
    {
        Map<String, Object> importParams = castToMap( functionMap.get( "IMPORT" ) );

        assertThat( importParams.get( "intParam" ) ).isEqualTo( 4711 );
    }

    @Test
    public void mapsSimpleParameterWithConverter()
    {
        Map<String, Object> exportParams = castToMap( functionMap.get( "EXPORT" ) );
        Object parameter = exportParams.get( "intParamWithConverter" );

        assertThat( parameter ).isInstanceOf( Integer.class );
        assertThat( parameter ).isEqualTo( 4712 );
    }

    @Test
    public void mapsStructureParameterWithConverter()
    {
        Map<String, Object> importParams = castToMap( functionMap.get( "IMPORT" ) );
        Map<String, Object> structure = castToMap( importParams.get( "structureParamWithConverter" ) );
        Object parameter = structure.get( "charParam" );

        assertThat( parameter ).isInstanceOf( Character.class );
        assertThat( parameter ).isEqualTo( 'c' );
    }

    @Test
    public void mapsStructureParameterWithoutConverter()
    {
        Map<String, Object> exportParams = castToMap( functionMap.get( "EXPORT" ) );
        Object parameter = exportParams.get( "structureParam" );
        Map<String, Object> structureMap = castToMap( parameter );

        assertThat( structureMap ).hasSize( 1 );
        assertThat( structureMap.get( "charParam" ) ).isEqualTo( 'c' );
    }

    @Test
    public void mapsTableParameterWithoutConverter()
    {
        Map<String, Object> tableParams = castToMap( functionMap.get( "TABLE" ) );
        Collection<Map<String, Object>> tableParam = castToCollectionOfMaps( tableParams.get( "tableParam" ) );

        assertThat( tableParam ).containsOnly( singletonMap( "charParam", '1' ), singletonMap( "charParam", '2' ) );
    }

    @Test
    public void mapsTableParameterWithConverter()
    {
        Map<String, Object> tableParams = castToMap( functionMap.get( "TABLE" ) );
        Collection<Map<String, Object>> tableParam = castToCollectionOfMaps( tableParams.get( "tableParamWithConverter" ) );

        assertThat( tableParam ).containsOnly( singletonMap( "charParam", '3' ), singletonMap( "charParam", '4' ) );
    }

    @Test
    public void doesNotMapParametersWithNullValues()
    {
        MyTestBapi bapi = new MyTestBapi( null, null, null, null, null, null );
        BapiMapping bapiMapping = bapiMapper.mapBapi( MyTestBapi.class );

        Map<String, Object> functionMap = pojoMapper.mapPojoToFunctionMap( bapi, bapiMapping );

        Map<String, Object> importParams = castToMap( functionMap.get( "IMPORT" ) );
        Map<String, Object> exportParams = castToMap( functionMap.get( "EXPORT" ) );
        Map<String, Object> tableParams = castToMap( functionMap.get( "TABLE" ) );
        assertThat( importParams ).hasSize( 0 );
        assertThat( exportParams ).hasSize( 0 );
        assertThat( tableParams ).hasSize( 0 );
    }

    private MyTestBapi createTestBapi()
    {
        TestStructure structure = new TestStructure( 'c' );
        TestStructure tableStructure1 = new TestStructure( '1' );
        TestStructure tableStructure2 = new TestStructure( '2' );
        List<TestStructure> table = new ArrayList<TestStructure>();
        table.add( tableStructure1 );
        table.add( tableStructure2 );
        return new MyTestBapi( 4711, "4712", structure, "c", table, "34" );
    }
}
