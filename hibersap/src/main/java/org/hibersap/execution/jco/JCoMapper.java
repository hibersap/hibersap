package org.hibersap.execution.jco;

/*
 * Copyright (C) 2008 akquinet tech@spree GmbH
 * 
 * This file is part of Hibersap.
 *
 * Hibersap is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hibersap is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Hibersap.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibersap.HibersapException;

import com.sap.mw.jco.JCO.Field;
import com.sap.mw.jco.JCO.Function;
import com.sap.mw.jco.JCO.Record;
import com.sap.mw.jco.JCO.Table;


/**
 * @author Carsten Erker
 */
public class JCoMapper
{
    private void checkTypes( Object value, Field jcoField )
    {
        try
        {
            if ( value != null && !Class.forName( jcoField.getClassNameOfValue() ).isAssignableFrom( value.getClass() ) )
            {
                throw new HibersapException( "JCo field " + jcoField.getName() + " has type "
                    + jcoField.getClassNameOfValue() + " while value to set has type " + value.getClass().getName() );
            }
        }
        catch ( ClassNotFoundException e )
        {
            throw new HibersapException( "Class check of JCo field failed, class " + jcoField.getClassNameOfValue()
                + " not found", e );
        }
    }

    private void mapToJCo( Record record, Map<String, Object> map )
    {
        for ( String fieldName : map.keySet() )
        {
            Object value = map.get( fieldName );
            Field jcoField = record.getField( fieldName );
            if ( jcoField.isStructure() )
            {
                Map<String, Object> structureMap = (Map<String, Object>) value;
                mapToJCo( jcoField.getStructure(), structureMap );
            }
            else if ( jcoField.isTable() )
            {
                Collection<Map<String, Object>> tableMap = (Collection<Map<String, Object>>) value;
                Table table = jcoField.getTable();
                table.clear();
                for ( Map<String, Object> structureMap : tableMap )
                {
                    table.appendRow();
                    mapToJCo( table, structureMap );
                }
            }
            else
            {
                checkTypes( value, jcoField );
                jcoField.setValue( value );
            }
        }
    }

    private Map<String, Object> mapToMap( Record record )
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if ( record == null )
            return map;
        for ( int i = 0; i < record.getNumFields(); i++ )
        {
            Field jcoField = record.getField( i );
            String sapFieldName = jcoField.getName();

            if ( jcoField.isStructure() )
            {
                map.put( sapFieldName, mapToMap( jcoField.getStructure() ) );
            }
            else if ( jcoField.isTable() )
            {
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                Table table = jcoField.getTable();
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

    void putFunctionMapValuesToFunction( Function function, Map<String, Object> functionMap )
    {
        mapToJCo( function.getImportParameterList(), (Map<String, Object>) functionMap.get( "IMPORT" ) );
        mapToJCo( function.getExportParameterList(), (Map<String, Object>) functionMap.get( "EXPORT" ) );
        mapToJCo( function.getTableParameterList(), (Map<String, Object>) functionMap.get( "TABLE" ) );
    }

    void putFunctionValuesToFunctionMap( Function function, Map<String, Object> map )
    {
        map.put( "IMPORT", mapToMap( function.getImportParameterList() ) );
        map.put( "EXPORT", mapToMap( function.getExportParameterList() ) );
        map.put( "TABLE", mapToMap( function.getTableParameterList() ) );
    }
}