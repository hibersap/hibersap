package org.hibersap.execution;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.hibersap.conversion.ConverterCache;
import org.hibersap.mapping.AnnotationBapiMapper;
import org.hibersap.mapping.model.BapiMapping;
import org.junit.Test;

public class PojoMapper_ToPojoTest
{
    private ConverterCache cache = new ConverterCache();

    private PojoMapper pojoMapper = new PojoMapper( cache );

    private AnnotationBapiMapper bapiMapper = new AnnotationBapiMapper();

    @Test
    public void testMapFunctionMapToPojo()
    {
        Map<String, Object> functionMap = createMap();

        Map<String, Object> importsMap = createMap();
        importsMap.put( "intParam", -1 );
        functionMap.put( "IMPORT", importsMap );

        Map<String, Object> exportsMap = createMap();
        Map<String, Object> structureParamMap = createMap();
        structureParamMap.put( "charParam", 'c' );
        exportsMap.put( "structureParam", structureParamMap );
        functionMap.put( "EXPORT", exportsMap );

        Map<String, Object> tablesMap = createMap();
        Map<String, Object> tableRow1Map = createMap();
        tableRow1Map.put( "charParam", '1' );
        Map<String, Object> tableRow2Map = createMap();
        tableRow2Map.put( "charParam", '2' );
        ArrayList<Map<String, Object>> table = new ArrayList<Map<String, Object>>();
        table.add( tableRow1Map );
        table.add( tableRow2Map );
        tablesMap.put( "tableParam", table );
        functionMap.put( "TABLE", tablesMap );

        BapiMapping bapiMapping = bapiMapper.mapBapi( MyTestBapi.class );

        MyTestBapi bapi = new MyTestBapi( null, null, null );
        pojoMapper.mapFunctionMapToPojo( bapi, functionMap, bapiMapping );

        // check import parameter
        assertEquals( new Integer( -1 ), bapi.intParam );

        // check export parameter
        assertEquals( 'c', bapi.structureParam.charParam );

        // check table parameter; the order of the table rows shall be kept.
        assertEquals( 2, bapi.tableParam.size() );
        assertEquals( '1', bapi.tableParam.get( 0 ).charParam );
        assertEquals( '2', bapi.tableParam.get( 1 ).charParam );
    }

    private Map<String, Object> createMap()
    {
        return new HashMap<String, Object>();
    }
}
