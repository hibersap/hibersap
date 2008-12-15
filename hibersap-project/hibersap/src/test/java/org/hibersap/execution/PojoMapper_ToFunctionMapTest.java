package org.hibersap.execution;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibersap.conversion.ConverterCache;
import org.hibersap.execution.MyTestBapi.TestStructure;
import org.hibersap.mapping.AnnotationBapiMapper;
import org.hibersap.mapping.model.BapiMapping;
import org.junit.Test;

public class PojoMapper_ToFunctionMapTest
{
    private ConverterCache cache = new ConverterCache();

    private PojoMapper pojoMapper = new PojoMapper( cache );

    private AnnotationBapiMapper bapiMapper = new AnnotationBapiMapper();

    @Test
    public void mapPojoToFunctionMap()
    {
        TestStructure structure = new TestStructure( 'c' );
        TestStructure tableStructure1 = new TestStructure( '1' );
        TestStructure tableStructure2 = new TestStructure( '2' );
        List<TestStructure> table = new ArrayList<TestStructure>();
        table.add( tableStructure1 );
        table.add( tableStructure2 );
        MyTestBapi bapi = new MyTestBapi( -1, structure, table );

        BapiMapping bapiMapping = bapiMapper.mapBapi( MyTestBapi.class );

        Map<String, Object> functionMap = pojoMapper.mapPojoToFunctionMap( bapi, bapiMapping );

        // check import parameter
        Map<String, Object> importParams = UnsafeCastHelper.castToMap( functionMap.get( "IMPORT" ) );
        assertEquals( 1, importParams.size() );
        assertEquals( -1, importParams.get( "intParam" ) );

        // check export parameter
        Map<String, Object> exportParams = UnsafeCastHelper.castToMap( functionMap.get( "EXPORT" ) );
        assertEquals( 1, exportParams.size() );
        Map<String, Object> structureParam = UnsafeCastHelper.castToMap( exportParams.get( "structureParam" ) );
        assertEquals( 1, structureParam.size() );
        assertEquals( 'c', structureParam.get( "charParam" ) );

        // check table parameter; the order of the table rows shall be kept.
        Map<String, Object> tableParams = UnsafeCastHelper.castToMap( functionMap.get( "TABLE" ) );
        assertEquals( 1, tableParams.size() );
        Collection<Map<String, Object>> tableParam = UnsafeCastHelper.castToCollectionOfMaps( tableParams
            .get( "tableParam" ) );
        assertEquals( 2, tableParam.size() );
        Iterator<Map<String, Object>> iterator = tableParam.iterator();
        Map<String, Object> tableRow = iterator.next();
        assertEquals( 1, tableRow.size() );
        assertEquals( '1', tableRow.get( "charParam" ) );
        tableRow = iterator.next();
        assertEquals( 1, tableRow.size() );
        assertEquals( '2', tableRow.get( "charParam" ) );
    }

    @Test
    public void mapWithNullValues()
    {
        MyTestBapi bapi = new MyTestBapi( null, null, null );

        BapiMapping bapiMapping = bapiMapper.mapBapi( MyTestBapi.class );

        Map<String, Object> functionMap = pojoMapper.mapPojoToFunctionMap( bapi, bapiMapping );

        // check import parameter
        Map<String, Object> importParams = UnsafeCastHelper.castToMap( functionMap.get( "IMPORT" ) );
        assertEquals( 0, importParams.size() );

        // check export parameter
        Map<String, Object> exportParams = UnsafeCastHelper.castToMap( functionMap.get( "EXPORT" ) );
        assertEquals( 0, exportParams.size() );

        // check table parameter
        Map<String, Object> tableParams = UnsafeCastHelper.castToMap( functionMap.get( "TABLE" ) );
        assertEquals( 0, tableParams.size() );
    }
}
