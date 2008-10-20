package org.hibersap.execution.jco;

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

import org.hibersap.HibersapException;
import org.hibersap.execution.UnsafeCastHelper;

import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFieldIterator;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRecord;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;

/**
 * @author Carsten Erker
 */
public class JCoMapper
{
    void putFunctionMapValuesToFunction( JCoFunction function, Map<String, Object> functionMap )
    {
        Map<String, Object> importMap = UnsafeCastHelper.castToMap( functionMap.get( "IMPORT" ) );
        mapToJCo( function.getImportParameterList(), importMap );
        Map<String, Object> exportMap = UnsafeCastHelper.castToMap( functionMap.get( "EXPORT" ) );
        mapToJCo( function.getExportParameterList(), exportMap );
        Map<String, Object> tableMap = UnsafeCastHelper.castToMap( functionMap.get( "TABLE" ) );
        mapToJCo( function.getTableParameterList(), tableMap );
    }

    void putFunctionValuesToFunctionMap( JCoFunction function, Map<String, Object> map )
    {
        map.put( "IMPORT", mapToMap( function.getImportParameterList() ) );
        map.put( "EXPORT", mapToMap( function.getExportParameterList() ) );
        map.put( "TABLE", mapToMap( function.getTableParameterList() ) );
    }

    private void checkTypes( Object value, String classNameOfBapiField, String fieldName )
    {
        try
        {
            if ( value != null && !Class.forName( classNameOfBapiField ).isAssignableFrom( value.getClass() ) )
            {
                throw new HibersapException( "JCo field " + fieldName + " has type " + classNameOfBapiField
                    + " while value to set has type " + value.getClass().getName() );
            }
        }
        catch ( ClassNotFoundException e )
        {
            throw new HibersapException( "Class check of JCo field failed, class " + classNameOfBapiField
                + " not found", e );
        }
    }

    private void mapToJCo( JCoRecord record, Map<String, Object> map )
    {
        for ( String fieldName : map.keySet() )
        {
            Object value = map.get( fieldName );

            if ( Map.class.isAssignableFrom( value.getClass() ) )
            {
                Map<String, Object> structureMap = UnsafeCastHelper.castToMap( value );
                JCoStructure structure = record.getStructure( fieldName );
                mapToJCo( structure, structureMap );
            }
            else if ( Collection.class.isAssignableFrom( value.getClass() ) )
            {
                Collection<Map<String, Object>> tableMap = UnsafeCastHelper.castToCollectionOfMaps( value );
                JCoTable table = record.getTable( fieldName );
                table.clear();
                for ( Map<String, Object> structureMap : tableMap )
                {
                    table.appendRow();
                    mapToJCo( table, structureMap );
                }
            }
            else
            {
                checkTypes( value, record.getClassNameOfValue( fieldName ), fieldName );
                record.setValue( fieldName, value );
            }
        }
    }

    private Map<String, Object> mapToMap( JCoRecord record )
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if ( record == null )
            return map;

        JCoFieldIterator iter = record.getFieldIterator();

        while ( iter.hasNextField() )
        {
            JCoField jcoField = iter.nextField();

            String sapFieldName = jcoField.getName();

            if ( jcoField.isStructure() )
            {
                map.put( sapFieldName, mapToMap( jcoField.getStructure() ) );
            }
            else if ( jcoField.isTable() )
            {
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                JCoTable table = jcoField.getTable();
                for ( int j = 0; j < table.getNumRows(); j++ )
                {
                    table.setRow( j );
                    list.add( mapToMap( table ) );
                }
                map.put( sapFieldName, list );
            }
            else
            {
                Object value = jcoField.getValue();
                map.put( sapFieldName, value );
            }
        }
        return map;
    }
}