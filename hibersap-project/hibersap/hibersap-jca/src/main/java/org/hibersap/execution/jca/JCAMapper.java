package org.hibersap.execution.jca;

/*
 * Copyright (C) 2008 akquinet tech@spree GmbH
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.resource.cci.IndexedRecord;
import javax.resource.cci.MappedRecord;
import javax.resource.cci.Record;

import net.sf.sapbapijca.adapter.cci.IndexedRecordImpl;
import net.sf.sapbapijca.adapter.cci.MappedRecordImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.bapi.BapiConstants;
import org.hibersap.execution.UnsafeCastHelper;

/**
 * @author M. Dahm
 */
public class JCAMapper
{
    private static final Log LOG = LogFactory.getLog( JCAMapper.class );

    public MappedRecord mapFunctionMapValuesToMappedRecord( final String functionName,
                                                            final Map<String, Object> functionMap )
    {
        LOG.info( "mapFunctionMapValuesToMappedRecord() functionMap=" + functionMap );

        final MappedRecord record = new MappedRecordImpl( functionName );
        final Map<String, Object> importMap = UnsafeCastHelper.castToMap( functionMap.get( BapiConstants.IMPORT ) );
        mapToMappedRecord( record, importMap );
        final Map<String, Object> tableMap = UnsafeCastHelper.castToMap( functionMap.get( BapiConstants.TABLE ) );
        mapToMappedRecord( record, tableMap );

        LOG.info( "mapFunctionMapValuesToMappedRecord() record=" + record );

        return record;
    }

    public void mapRecordToFunctionMap( final Map<String, Object> functionMap, final Map<String, Object> resultRecordMap )
    {
        LOG.info( "mapRecordToFunctionMap() recordMap=" + resultRecordMap );

        for ( final Entry<String, Object> entry : resultRecordMap.entrySet() )
        {
            final Object recordValue = entry.getValue();
            final String recordKey = entry.getKey();

            LOG.debug( "mapping " + recordValue.getClass().getName() + ": " + recordKey + "=" + recordValue );

            if ( recordValue instanceof MappedRecord )
            {
                final MappedRecord mappedResultRecord = (MappedRecord) recordValue;
                final Map<String, Object> resultMap = new HashMap<String, Object>();
                resultMap.put( mappedResultRecord.getRecordName(), mappedResultRecord );
                Map<String, Object> export = UnsafeCastHelper.castToMap( functionMap.get( BapiConstants.EXPORT ) );
                export.put( recordKey, recordValue );
            }
            else if ( recordValue instanceof IndexedRecord )
            {
                final IndexedRecord indexedResultRecord = (IndexedRecord) recordValue;
                List<Map<String, Object>> table = new ArrayList<Map<String, Object>>();

                for ( Object object : indexedResultRecord )
                {
                    MappedRecord mr = (MappedRecord) object;
                    final Map<String, Object> line = new HashMap<String, Object>();

                    @SuppressWarnings("unchecked")
                    Set<String> keys = mr.keySet();

                    for ( String key : keys )
                    {
                        line.put( key, mr.get( key ) );
                    }
                    table.add( line );
                }

                Map<String, Object> tables = UnsafeCastHelper.castToMap( functionMap.get( BapiConstants.TABLE ) );
                tables.put( indexedResultRecord.getRecordName(), table );
            }
            else
            {
                Map<String, Object> export = UnsafeCastHelper.castToMap( functionMap.get( BapiConstants.EXPORT ) );
                export.put( recordKey, recordValue );
            }
        }
        LOG.info( "mapRecordToFunctionMap() functionMap=" + functionMap );
    }

    @SuppressWarnings("unchecked")
    private void appendToRecord( final Record record, final String fieldName, final Object value )
    {
        if ( record instanceof IndexedRecord )
        {
            ( (IndexedRecord) record ).add( value );
        }
        else
        {
            ( (MappedRecord) record ).put( fieldName, value );
        }
    }

    @SuppressWarnings("unchecked")
    private void mapToMappedRecord( final Record record, final Map<String, Object> map )
    {
        for ( final String fieldName : map.keySet() )
        {
            final Object value = map.get( fieldName );

            if ( Map.class.isAssignableFrom( value.getClass() ) )
            {
                final Map<String, Object> structureMap = UnsafeCastHelper.castToMap( value );
                final Record structure = new MappedRecordImpl( fieldName );

                appendToRecord( record, fieldName, structure );

                mapToMappedRecord( structure, structureMap );
            }
            else if ( Collection.class.isAssignableFrom( value.getClass() ) )
            {
                final Collection<Map<String, Object>> tableMap = UnsafeCastHelper.castToCollectionOfMaps( value );
                final IndexedRecord table = new IndexedRecordImpl( fieldName );

                appendToRecord( record, fieldName, table );

                int i = 0;
                for ( final Map<String, Object> row : tableMap )
                {
                    MappedRecordImpl rowRecord = new MappedRecordImpl( fieldName + ":row:" + i );
                    mapToMappedRecord( rowRecord, row );
                    table.add( rowRecord );
                    i++;
                }
            }
            else
            {
                appendToRecord( record, fieldName, value );
            }
        }
    }
}