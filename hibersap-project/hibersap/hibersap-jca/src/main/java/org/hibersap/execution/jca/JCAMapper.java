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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.resource.cci.IndexedRecord;
import javax.resource.cci.MappedRecord;
import javax.resource.cci.Record;

import net.sf.sapbapijca.adapter.cci.IndexedRecordImpl;
import net.sf.sapbapijca.adapter.cci.MappedRecordImpl;

import org.hibersap.BapiConstants;
import org.hibersap.HibersapException;
import org.hibersap.execution.UnsafeCastHelper;

/**
 * @author M. Dahm
 */
public class JCAMapper
{
    public MappedRecord mapFunctionMapValuesToMappedRecord( final Map<String, Object> functionMap )
    {
        final MappedRecord record = new MappedRecordImpl( BapiConstants.IMPORT );
        final Map<String, Object> importMap = UnsafeCastHelper.castToMap( functionMap.get( BapiConstants.IMPORT ) );
        mapToMappedRecord( record, importMap );
        final Map<String, Object> tableMap = UnsafeCastHelper.castToMap( functionMap.get( BapiConstants.TABLE ) );
        mapToMappedRecord( record, tableMap );
        return record;
    }

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
                final Record table = new IndexedRecordImpl( fieldName );

                appendToRecord( record, fieldName, table );

                for ( final Map<String, Object> structureMap : tableMap )
                {
                    mapToMappedRecord( table, structureMap );
                }
            }
            else
            {
                appendToRecord( record, fieldName, value );
            }
        }
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

    public void mapRecordToFunctionMap( final Map<String, Object> functionMap, final Map<String, Object> resultRecordMap )
    {
        for ( final Entry<String, Object> entry : resultRecordMap.entrySet() )
        {
            final Object record = entry.getValue();
            final String key = entry.getKey();

            if ( record instanceof MappedRecord )
            {
                final MappedRecord mappedResultRecord = (MappedRecord) record;
                final Map<String, Object> resultMap = new HashMap<String, Object>();
                resultMap.put( mappedResultRecord.getRecordName(), mappedResultRecord );
                functionMap.put( BapiConstants.EXPORT, resultMap );
            }
            else if ( record instanceof IndexedRecord )
            {
                final IndexedRecord indexedResultRecord = (IndexedRecord) record;
                final Map<String, Object> tableMap = new HashMap<String, Object>();
                tableMap.put( indexedResultRecord.getRecordName(), indexedResultRecord );

                functionMap.put( BapiConstants.TABLE, tableMap );
            }
            else
            {
                throw new HibersapException( "Cannot handle result value: " + key + ":" + record );
            }
        }
    }

}