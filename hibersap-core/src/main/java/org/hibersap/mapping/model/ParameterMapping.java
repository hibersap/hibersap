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

import java.io.Serializable;

/**
 * @author Carsten Erker
 */
public abstract class ParameterMapping implements Serializable
{
    private static final long serialVersionUID = -2858494641560482982L;

    public enum ParamType
    {
        FIELD, STRUCTURE, TABLE
    }

    private final Class<?> associatedType;

    private final String sapName;

    private final String javaName;

    private final Class<? extends Converter> converterClass;

    public ParameterMapping( Class<?> associatedType, String sapName, String javaName,
                             Class<? extends Converter> converterClass )
    {
        this.associatedType = associatedType;
        this.sapName = sapName;
        this.javaName = javaName;
        this.converterClass = converterClass;
    }

    public Class<?> getAssociatedType()
    {
        return associatedType;
    }

    public String getJavaName()
    {
        return this.javaName;
    }

    public abstract ParamType getParamType();

    public Class<? extends Converter> getConverterClass()
    {
        return this.converterClass;
    }

    public boolean hasConverter()
    {
        return this.converterClass != null;
    }

    protected final Object getConvertedValueToJava( Object value, ConverterCache converterCache )
    {
        Converter converter = converterCache.getConverter( getConverterClass() );
        //noinspection unchecked
        return converter.convertToJava( value );
    }

    protected final Object getConvertedValueToSap( Object value, ConverterCache converterCache )
    {
        Converter converter = converterCache.getConverter( getConverterClass() );
        //noinspection unchecked
        return converter.convertToSap( value );
    }

    protected abstract Object getUnconvertedValueToJava( Object value, ConverterCache converterCache );

    protected abstract Object getUnconvertedValueToSap( Object value, ConverterCache converterCache );

    public final Object mapToJava( Object fieldMap, ConverterCache converterCache )
    {
        if ( hasConverter() )
        {
            return getConvertedValueToJava( fieldMap, converterCache );
        }
        else
        {
            return getUnconvertedValueToJava( fieldMap, converterCache );
        }
    }

    /**
     * @param value          A plain value if the parameter is a simple one,
     *                       a bapi structure instance if the parameter is a structure,
     *                       a list of bapi structure instances if the parameter is a table.
     * @param converterCache Needed for conversion of parameters
     * @return A plain value if the parameter is a simple one,
     *         a Map (SAP structure parameter name to plain values) if the parameter is a structure,
     *         a List of Maps (SAP structure parameter name to plain values) if the parameter is a table.
     */
    public Object mapToSap( Object value, ConverterCache converterCache )
    {
        return hasConverter() ?
               getConvertedValueToSap( value, converterCache ) :
               getUnconvertedValueToSap( value, converterCache );
    }

    public String getSapName()
    {
        return sapName;
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

        ParameterMapping that = ( ParameterMapping ) o;

        if ( associatedType != null ? !associatedType.equals( that.associatedType ) : that.associatedType != null )
        {
            return false;
        }
        if ( converterClass != null ? !converterClass.equals( that.converterClass ) : that.converterClass != null )
        {
            return false;
        }
        if ( javaName != null ? !javaName.equals( that.javaName ) : that.javaName != null )
        {
            return false;
        }
        //noinspection RedundantIfStatement
        if ( sapName != null ? !sapName.equals( that.sapName ) : that.sapName != null )
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = associatedType != null ? associatedType.hashCode() : 0;
        result = 31 * result + ( sapName != null ? sapName.hashCode() : 0 );
        result = 31 * result + ( javaName != null ? javaName.hashCode() : 0 );
        result = 31 * result + ( converterClass != null ? converterClass.hashCode() : 0 );
        return result;
    }
}