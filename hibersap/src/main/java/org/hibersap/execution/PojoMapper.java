package org.hibersap.execution;

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
import java.util.Set;

import org.hibersap.conversion.Converter;
import org.hibersap.conversion.ConverterCache;
import org.hibersap.mapping.ReflectionHelper;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.mapping.model.FieldMapping;
import org.hibersap.mapping.model.ObjectMapping;
import org.hibersap.mapping.model.ParameterMapping;
import org.hibersap.mapping.model.StructureMapping;
import org.hibersap.mapping.model.TableMapping;
import org.hibersap.mapping.model.ParameterMapping.ParamType;

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
        Map<String, Object> imports = UnsafeCastHelper.castToMap( functionMap.get( "IMPORT" ) );
        Set<ObjectMapping> importMappings = bapiMapping.getImportParameters();
        mapToPojo( bapi, imports, importMappings );

        Map<String, Object> exports = UnsafeCastHelper.castToMap( functionMap.get( "EXPORT" ) );
        Set<ObjectMapping> exportMappings = bapiMapping.getExportParameters();
        mapToPojo( bapi, exports, exportMappings );

        Map<String, Object> tables = UnsafeCastHelper.castToMap( functionMap.get( "TABLE" ) );
        Set<TableMapping> tableMappings = bapiMapping.getTableParameters();
        mapToPojo( bapi, tables, tableMappings );
    }

    public Map<String, Object> mapPojoToFunctionMap( Object bapi, BapiMapping bapiMapping )
    {
        Map<String, Object> functionMap = new HashMap<String, Object>();

        Set<ObjectMapping> imports = bapiMapping.getImportParameters();
        functionMap.put( "IMPORT", pojoToMap( bapi, imports ) );

        Set<ObjectMapping> exports = bapiMapping.getExportParameters();
        functionMap.put( "EXPORT", pojoToMap( bapi, exports ) );

        Set<TableMapping> tables = bapiMapping.getTableParameters();
        functionMap.put( "TABLE", pojoToMap( bapi, tables ) );

        return functionMap;
    }

    private void mapToPojo( Object bean, Map<String, Object> map, Set<? extends ParameterMapping> mappings )
    {
        for ( ParameterMapping paramMapping : mappings )
        {
            ParamType paramType = paramMapping.getParamType();
            String fieldNameJava = paramMapping.getJavaName();
            String fieldNameSap = paramMapping.getSapName();
            Object value = map.get( fieldNameSap );

            if ( paramType == ParamType.FIELD )
            {
                FieldMapping fieldMapping = (FieldMapping) paramMapping;
                Converter converter = converterCache.getConverter( fieldMapping.getConverter() );
                Object convertedValue = converter.convertToJava( value );
                ReflectionHelper.setFieldValue( bean, fieldNameJava, convertedValue );
            }
            else if ( paramType == ParamType.STRUCTURE )
            {
                Set<FieldMapping> fieldMappings = ( (StructureMapping) paramMapping ).getParameters();
                Map<String, Object> subMap = UnsafeCastHelper.castToMap( value );
                Object subBean = ReflectionHelper.newInstance( paramMapping.getAssociatedType() );
                mapToPojo( subBean, subMap, fieldMappings );
                ReflectionHelper.setFieldValue( bean, fieldNameJava, subBean );
            }
            else
            {
                TableMapping tableMapping = (TableMapping) paramMapping;
                Collection<Object> collection = ReflectionHelper.newCollectionInstance( tableMapping
                    .getCollectionType() );
                ReflectionHelper.setFieldValue( bean, fieldNameJava, collection );

                Collection<Map<String, Object>> rows = UnsafeCastHelper.castToCollectionOfMaps( value );

                for ( Map<String, Object> tableMap : rows )
                {
                    Object elementBean = ReflectionHelper.newInstance( tableMapping.getAssociatedType() );
                    mapToPojo( elementBean, tableMap, tableMapping.getComponentParameter().getParameters() );
                    collection.add( elementBean );
                }

                if ( tableMapping.getFieldType().isArray() )
                {
                    ReflectionHelper.setFieldValue( bean, fieldNameJava, collection.toArray() );
                }
                else
                {
                    ReflectionHelper.setFieldValue( bean, fieldNameJava, collection );
                }
            }
        }
    }

    private Map<String, Object> pojoToMap( Object pojo, Set<? extends ParameterMapping> mappings )
    {
        Map<String, Object> map = new HashMap<String, Object>();

        for ( ParameterMapping paramMapping : mappings )
        {
            String fieldNameJava = paramMapping.getJavaName();
            Object value = ReflectionHelper.getFieldValue( pojo, fieldNameJava );

            if ( value != null )
            {
                ParamType paramType = paramMapping.getParamType();
                String fieldNameSap = paramMapping.getSapName();

                if ( paramType == ParamType.FIELD )
                {
                    FieldMapping fieldMapping = (FieldMapping) paramMapping;
                    Converter converter = converterCache.getConverter( fieldMapping.getConverter() );
                    map.put( fieldNameSap, converter.convertToSap( value ) );
                }
                else if ( paramType == ParamType.STRUCTURE )
                {
                    Set<FieldMapping> fieldMappings = ( (StructureMapping) paramMapping ).getParameters();
                    map.put( fieldNameSap, pojoToMap( value, fieldMappings ) );
                }
                else
                {
                    List<Map<String, Object>> valueMaps = new ArrayList<Map<String, Object>>();
                    Set<FieldMapping> fieldMappings = ( (TableMapping) paramMapping ).getComponentParameter()
                        .getParameters();

                    // TODO check: value my be null - esp. when table acts as import parameter?
                    Collection<Object> tableElements = UnsafeCastHelper.castToCollection( value );
                    for ( Object tableElement : tableElements )
                    {
                        valueMaps.add( pojoToMap( tableElement, fieldMappings ) );
                    }
                    map.put( fieldNameSap, valueMaps );
                }
            }
        }
        return map;
    }
}
