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

package org.hibersap.mapping.model;

import org.hibersap.conversion.Converter;
import org.hibersap.conversion.ConverterCache;
import org.hibersap.execution.UnsafeCastHelper;
import org.hibersap.mapping.ReflectionHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Carsten Erker
 */
public final class StructureMapping extends ParameterMapping
{
    private static final long serialVersionUID = 2930405767657861801L;

    private final Set<FieldMapping> parameters;

    public StructureMapping( Class<?> associatedClass, String sapName, String javaName,
                             Class<? extends Converter> converterClass )
    {
        super( associatedClass, sapName, javaName, converterClass );
        parameters = new HashSet<FieldMapping>();
    }

    public void addParameter( FieldMapping fieldParam )
    {
        parameters.add( fieldParam );
    }

    public Set<FieldMapping> getParameters()
    {
        return parameters;
    }

    @Override
    public ParamType getParamType()
    {
        return ParamType.STRUCTURE;
    }

    @Override
    protected Object getUnconvertedValueToJava( Object fieldMap, ConverterCache converterCache )
    {
        Map<String, Object> subMap = UnsafeCastHelper.castToMap( fieldMap );
        Object subBean = ReflectionHelper.newInstance( getAssociatedType() );

        for ( FieldMapping parameter : parameters )
        {
            Object fieldValue = subMap.get( parameter.getSapName() );

            if ( fieldValue != null )
            {
                Object value = parameter.mapToJava( fieldValue, converterCache );
                ReflectionHelper.setFieldValue( subBean, parameter.getJavaName(), value );
            }
        }

        return subBean;
    }

    @Override
    protected Object getUnconvertedValueToSap( Object bapiStructure, ConverterCache converterCache )
    {
        HashMap<String, Object> functionMap = new HashMap<String, Object>();

        for ( FieldMapping parameter : parameters )
        {
            Object fieldValue = ReflectionHelper.getFieldValue( bapiStructure, parameter.getJavaName() );

            if ( fieldValue != null )
            {
                Object value = parameter.mapToSap( fieldValue, converterCache );
                functionMap.put( parameter.getSapName(), value );
            }
        }

        return functionMap;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }
        if ( !super.equals( o ) )
        {
            return false;
        }

        StructureMapping that = ( StructureMapping ) o;

        //noinspection RedundantIfStatement
        if ( parameters != null ? !parameters.equals( that.parameters ) : that.parameters != null )
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + ( parameters != null ? parameters.hashCode() : 0 );
        return result;
    }
}
