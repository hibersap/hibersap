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

package org.hibersap.execution.jca;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.resource.ResourceException;
import javax.resource.cci.IndexedRecord;
import javax.resource.cci.MappedRecord;
import javax.resource.cci.RecordFactory;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.hibersap.execution.UnsafeCastHelper;
import org.junit.Before;
import org.junit.Test;

public class JCAMapperTest
{
    final class IndexedRecordAnswer
        implements IAnswer<IndexedRecord>
    {
        public IndexedRecord answer()
            throws Throwable
        {
            Object[] args = EasyMock.getCurrentArguments();
            return new MyIndexedRecord( (String) args[0] );
        }
    }

    final class MappedRecordAnswer
        implements IAnswer<MappedRecord>
    {
        public MappedRecord answer()
            throws Throwable
        {
            Object[] args = EasyMock.getCurrentArguments();
            return new MyMappedRecord( (String) args[0] );
        }
    }

    private final JCAMapper mapper = new JCAMapper();

    private final RecordFactory recordFactory = EasyMock.createMock( RecordFactory.class );

    @SuppressWarnings("unchecked")
    @Test
    public void mapToFunctionMap()
        throws Exception
    {
        Map<String, Object> functionMap = createFunctionMap();
        Map<String, Object> resultRecord = new HashMap<String, Object>();

        addExportAndTableParameters( resultRecord );

        // map
        mapper.mapRecordToFunctionMap( functionMap, resultRecord );

        assertEquals( 3, functionMap.size() );

        // check import parameters still there?
        assertEquals( 3, UnsafeCastHelper.castToMap( functionMap.get( "IMPORT" ) ).size() );

        // check export parameters
        Map<String, Object> exportParams = UnsafeCastHelper.castToMap( functionMap.get( "EXPORT" ) );
        assertEquals( 3, exportParams.size() );

        Map<String, Object> exportParam1 = UnsafeCastHelper.castToMap( exportParams.get( "EXPORT_PARAM1" ) );
        assertEquals( "structField1", exportParam1.get( "STRUCT_FIELD1" ) );
        assertEquals( 2, exportParam1.get( "STRUCT_FIELD2" ) );
        assertEquals( "exportParam2", exportParams.get( "EXPORT_PARAM2" ) );
        assertEquals( new Date( 3 ), exportParams.get( "EXPORT_PARAM3" ) );

        // check table parameters
        Map<String, Object> tableParams = UnsafeCastHelper.castToMap( functionMap.get( "TABLE" ) );
        assertEquals( 2, tableParams.size() );

        List table2 = (List) tableParams.get( "TABLE_PARAM2" );
        assertEquals( 2, table2.size() );

        Map<String, Object> row1 = UnsafeCastHelper.castToMap( table2.get( 0 ) );
        assertEquals( "tableField1_1", row1.get( "TABLE_FIELD1" ) );
        assertEquals( 12, row1.get( "TABLE_FIELD2" ) );

        Map<String, Object> row2 = UnsafeCastHelper.castToMap( table2.get( 1 ) );
        assertEquals( "tableField2_1", row2.get( "TABLE_FIELD1" ) );
        assertEquals( 22, row2.get( "TABLE_FIELD2" ) );
    }

    @Test
    public void mapToMappedRecord()
        throws Exception
    {
        // map
        Map<String, Object> functionMap = createFunctionMap();
        MappedRecord record = mapper.mapFunctionMapValuesToMappedRecord( "BAPI_NAME", recordFactory, functionMap );

        assertEquals( "BAPI_NAME", record.getRecordName() );

        // check import parameters
        String importParam1 = (String) record.get( "IMPORT_PARAM2" );
        assertEquals( "importParam2", importParam1 );

        int param3 = (Integer) record.get( "IMPORT_PARAM3" );
        assertEquals( 3, param3 );

        MappedRecord importStruct = (MappedRecord) record.get( "IMPORT_PARAM1" );
        assertEquals( "IMPORT_PARAM1", importStruct.getRecordName() );
        assertEquals( 1, importStruct.get( "STRUCT_FIELD1" ) );
        assertEquals( "structField2", importStruct.get( "STRUCT_FIELD2" ) );

        // check table parameters
        IndexedRecord tableParam1 = (IndexedRecord) record.get( "TABLE_PARAM1" );
        assertEquals( "TABLE_PARAM1", tableParam1.getRecordName() );

        MappedRecord row1 = (MappedRecord) tableParam1.get( 0 );
        assertEquals( "tableField1", row1.get( "TABLE_FIELD1" ) );
        assertEquals( new Date( 1 ), row1.get( "TABLE_FIELD2" ) );
        assertEquals( 1, row1.get( "TABLE_FIELD3" ) );

        MappedRecord row2 = (MappedRecord) tableParam1.get( 1 );
        assertEquals( "tableField2", row2.get( "TABLE_FIELD1" ) );
        assertEquals( new Date( 2 ), row2.get( "TABLE_FIELD2" ) );
        assertEquals( 2, row2.get( "TABLE_FIELD3" ) );
    }

    @Before
    public void setUp()
        throws ResourceException
    {
        EasyMock.expect( recordFactory.createIndexedRecord( (String) EasyMock.notNull() ) )
            .andAnswer( new IndexedRecordAnswer() ).anyTimes();
        EasyMock.expect( recordFactory.createMappedRecord( (String) EasyMock.notNull() ) )
            .andAnswer( new MappedRecordAnswer() ).anyTimes();
        EasyMock.replay( recordFactory );
    }

    @SuppressWarnings("unchecked")
    private void addExportAndTableParameters( Map<String, Object> resultRecord )
    {
        // add export parameters
        MyMappedRecord exportStruct1 = new MyMappedRecord( "EXPORT_PARAM1" );
        exportStruct1.put( "STRUCT_FIELD1", "structField1" );
        exportStruct1.put( "STRUCT_FIELD2", 2 );

        resultRecord.put( "EXPORT_PARAM1", exportStruct1 );
        resultRecord.put( "EXPORT_PARAM2", "exportParam2" );
        resultRecord.put( "EXPORT_PARAM3", new Date( 3 ) );

        // add table parameter
        MyMappedRecord tableRowRecord1 = new MyMappedRecord( "TABLE_PARAM2:row:0" );
        tableRowRecord1.put( "TABLE_FIELD1", "tableField1_1" );
        tableRowRecord1.put( "TABLE_FIELD2", 12 );

        MyMappedRecord tableRowRecord2 = new MyMappedRecord( "TABLE_PARAM2:row:1" );
        tableRowRecord2.put( "TABLE_FIELD1", "tableField2_1" );
        tableRowRecord2.put( "TABLE_FIELD2", 22 );

        MyIndexedRecord tableRecord = new MyIndexedRecord( "TABLE_PARAM2" );
        tableRecord.add( tableRowRecord1 );
        tableRecord.add( tableRowRecord2 );

        resultRecord.put( "TABLE_PARAM2", tableRecord );
    }

    private Map<String, Object> createFunctionMap()
    {
        Map<String, Object> functionMap = new HashMap<String, Object>();
        Map<String, Object> importMap = new HashMap<String, Object>();
        Map<String, Object> exportMap = new HashMap<String, Object>();
        Map<String, Object> tableMap = new HashMap<String, Object>();

        functionMap.put( "IMPORT", importMap );
        functionMap.put( "EXPORT", exportMap );
        functionMap.put( "TABLE", tableMap );

        // create import parameters
        Map<String, Object> importStructMap = new HashMap<String, Object>();
        importStructMap.put( "STRUCT_FIELD1", 1 );
        importStructMap.put( "STRUCT_FIELD2", "structField2" );

        importMap.put( "IMPORT_PARAM1", importStructMap );
        importMap.put( "IMPORT_PARAM2", "importParam2" );
        importMap.put( "IMPORT_PARAM3", 3 );

        // create table parameters
        List<Map<String, Object>> table1 = new ArrayList<Map<String, Object>>();
        table1.add( getTableRow( "tableField1", new Date( 1 ), 1 ) );
        table1.add( getTableRow( "tableField2", new Date( 2 ), 2 ) );

        tableMap.put( "TABLE_PARAM1", table1 );
        return functionMap;
    }

    private Map<String, Object> getTableRow( String param1, Date param2, int param3 )
    {
        Map<String, Object> row = new HashMap<String, Object>();
        row.put( "TABLE_FIELD1", param1 );
        row.put( "TABLE_FIELD2", param2 );
        row.put( "TABLE_FIELD3", param3 );
        return row;
    }
}
