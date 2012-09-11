package org.hibersap.execution;

import org.hibersap.conversion.ConverterCache;
import org.hibersap.mapping.AnnotationBapiMapper;
import org.hibersap.mapping.model.BapiMapping;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static org.fest.assertions.Assertions.assertThat;

public class PojoMapper_ToPojoTest
{
    private MyTestBapi bapi;

    @Before
    public void setUp() throws Exception
    {
        AnnotationBapiMapper bapiMapper = new AnnotationBapiMapper();
        bapi = createEmptyBapiObject();
        Map<String, Object> functionMap = createFunctionMapForBapi();
        BapiMapping bapiMapping = bapiMapper.mapBapi( MyTestBapi.class );
        PojoMapper pojoMapper = new PojoMapper( new ConverterCache() );

        pojoMapper.mapFunctionMapToPojo( bapi, functionMap, bapiMapping );
    }

    @Test
    public void mapsSimpleParameterWithoutConverter()
    {
        assertThat( bapi.intParam ).isEqualTo( 4711 );
    }

    @Test
    public void mapsSimpleParameterWithConverter()
    {
        assertThat( bapi.intParamWithConverter ).isEqualTo( "4712" );
    }

    @Test
    public void mapsStructureParameterWithConverter()
    {
        assertThat( bapi.structureParamWithConverter ).isEqualTo( "c" );
    }

    @Test
    public void mapsStructureParameterWithoutConverter()
    {
        assertThat( bapi.structureParam.charParam ).isEqualTo( 'd' );
    }

    @Test
    public void mapsTableParameterWithoutConverter()
    {
        assertThat( bapi.tableParam ).hasSize( 2 );
        assertThat( bapi.tableParam.get( 0 ).charParam ).isEqualTo( '1' );
        assertThat( bapi.tableParam.get( 1 ).charParam ).isEqualTo( '2' );
    }

    @Test
    public void mapsTableParameterWithConverter()
    {
        assertThat( bapi.tableParamWithConverter).isEqualTo( "34" );
    }

    @SuppressWarnings( {"unchecked"} )
    private Map<String, Object> createFunctionMapForBapi()
    {
        Map<String, Object> functionMap = createMap();

        Map<String, Object> importsMap = createMap();
        importsMap.put( "intParam", 4711 );
        importsMap.put( "structureParamWithConverter", singletonMap( "charParam", 'c' ) );
        functionMap.put( "IMPORT", importsMap );

        Map<String, Object> exportsMap = createMap();
        exportsMap.put( "structureParam", singletonMap( "charParam", 'd' ) );
        exportsMap.put( "intParamWithConverter", 4712 );
        functionMap.put( "EXPORT", exportsMap );

        Map<String, Object> tablesMap = createMap();
        List<Map<String, Character>> tableWithoutConverter = asList(
                singletonMap( "charParam", '1' ),
                singletonMap( "charParam", '2' ) );
        List<Map<String, Character>> tableWithConverter = asList(
                singletonMap( "charParam", '3' ),
                singletonMap( "charParam", '4' ) );
        tablesMap.put( "tableParam", tableWithoutConverter );
        tablesMap.put( "tableParamWithConverter", tableWithConverter );
        functionMap.put( "TABLE", tablesMap );
        return functionMap;
    }


    private Map<String, Object> createMap()
    {
        return new HashMap<String, Object>();
    }

    private MyTestBapi createEmptyBapiObject()
    {
        return new MyTestBapi( null, null, null, null, null, null );
    }
}
