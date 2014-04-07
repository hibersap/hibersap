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

import org.hibersap.bapi.BapiConstants;
import org.hibersap.conversion.ConverterCache;
import org.hibersap.mapping.ReflectionHelper;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.mapping.model.ParameterMapping;
import org.hibersap.mapping.model.TableMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Carsten Erker
 */
public class PojoMapper
{
    private final ConverterCache converterCache;

    public PojoMapper( ConverterCache converterCache )
    {
        this.converterCache = converterCache;
    }

    public void mapFunctionMapToPojo( Object bapi, Map<String, Object> functionMap, BapiMapping bapiMapping )
    {
        Map<String, Object> map = getMapForAllParamTypes( functionMap );
        Set<ParameterMapping> paramMappings = bapiMapping.getAllParameters();
        functionMapToPojo( bapi, map, paramMappings );
    }

    public Map<String, Object> mapPojoToFunctionMap( Object bapi, BapiMapping bapiMapping )
    {
        Map<String, Object> functionMap = new HashMap<String, Object>();

        Set<ParameterMapping> imports = bapiMapping.getImportParameters();
        functionMap.put( "IMPORT", pojoToMap( bapi, imports ) );

        Set<ParameterMapping> exports = bapiMapping.getExportParameters();
        functionMap.put( "EXPORT", pojoToMap( bapi, exports ) );

        Set<TableMapping> tables = bapiMapping.getTableParameters();
        functionMap.put( "TABLE", pojoToMap( bapi, tables ) );

        return functionMap;
    }

    private void functionMapToPojo( Object bapi, Map<String, Object> functionMap, Set<ParameterMapping> paramMappings )
    {
        for ( ParameterMapping paramMapping : paramMappings )
        {
            String fieldNameSap = paramMapping.getSapName();
            Object value = functionMap.get( fieldNameSap );

            if ( value != null )
            {
                Object mappedValue = paramMapping.mapToJava( value, converterCache );
                ReflectionHelper.setFieldValue( bapi, paramMapping.getJavaName(), mappedValue );
            }
        }
    }

    private Map<String, Object> pojoToMap( Object bapi, Set<? extends ParameterMapping> paramMappings )
    {
        Map<String, Object> functionMap = new HashMap<String, Object>();

        for ( ParameterMapping paramMapping : paramMappings )
        {
            String fieldNameJava = paramMapping.getJavaName();
            Object value = ReflectionHelper.getFieldValue( bapi, fieldNameJava );

            if ( value != null )
            {
                Object subMap = paramMapping.mapToSap( value, converterCache );
                functionMap.put( paramMapping.getSapName(), subMap );
            }
        }
        return functionMap;
    }


    private Map<String, Object> getMapForAllParamTypes( Map<String, Object> functionMap )
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.putAll( getMapNullSafe( functionMap, BapiConstants.IMPORT ) );
        map.putAll( getMapNullSafe( functionMap, BapiConstants.EXPORT ) );
        map.putAll( getMapNullSafe( functionMap, BapiConstants.TABLE ) );
        return map;
    }

    private Map<String, Object> getMapNullSafe( Map<String, Object> functionMap, final String paramName )
    {
        Map<String, Object> map = UnsafeCastHelper.castToMap( functionMap.get( paramName ) );
        return map == null ? new HashMap<String, Object>() : map;
    }
}
