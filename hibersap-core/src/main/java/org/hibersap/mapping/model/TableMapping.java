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

import org.hibersap.InternalHiberSapException;
import org.hibersap.MappingException;
import org.hibersap.conversion.Converter;
import org.hibersap.conversion.ConverterCache;
import org.hibersap.mapping.ReflectionHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.hibersap.execution.UnsafeCastHelper.castToCollectionOfMaps;
import static org.hibersap.mapping.ReflectionHelper.newCollectionInstance;

/**
 * @author Carsten Erker
 */
public final class TableMapping extends ParameterMapping
{
    private static final long serialVersionUID = 6134694196341208013L;

    private final StructureMapping componentParameter;

    private final Class<?> fieldType;

    private final Class<?> destinationType;

    /**
     * @param fieldType          The type of the field in the bean; may be a Collection interface like List,
     *                           Set, Collection, a concrete class that implements Collection or an array.
     *                           If there is a Converter specified on the field, it may be a Pojo class.
     * @param associatedType     The type of the elements, i.e. a Pojo class.
     * @param sapName            The table's name in SAP.
     * @param javaName           The Java field name of the Collection or array.
     * @param componentParameter A StructureMapping containing the table's fields.
     * @param converterClass     The Class of the table field's converter, if defined.
     */
    public TableMapping( Class<?> fieldType, Class<?> associatedType, String sapName, String javaName,
                         StructureMapping componentParameter, Class<? extends Converter> converterClass )
    {
        super( associatedType, sapName, javaName, converterClass );
        this.componentParameter = componentParameter;
        this.fieldType = fieldType;
        this.destinationType = determineDestinationType();
    }

    @SuppressWarnings( "unchecked" )
    private Class<?> determineDestinationType()
    {
        Class<?> resultingType;

        if ( isDestinationTypeCollection() )
        {
            if ( fieldType.isInterface() )
            {
                if ( List.class.equals( fieldType ) )
                {
                    resultingType = ArrayList.class;
                }
                else if ( Set.class.equals( fieldType ) )
                {
                    resultingType = HashSet.class;
                }
                else if ( Collection.class.equals( fieldType ) )
                {
                    resultingType = ArrayList.class;
                }
                else
                {
                    throw new MappingException(
                            "Collection of type " + fieldType.getName() + " not supported. See Field "
                                    + getJavaName() );
                }
            }
            else
            {
                resultingType = fieldType;
            }
        }
        else if ( fieldType.isArray() )
        {
            resultingType = ArrayList.class;
        }
        else
        {
            if ( hasConverter() )
            {
                resultingType = fieldType;
            }
            else
            {
                throw new MappingException( "The field " + getJavaName() + " must be an array or a "
                        + "Collection or have a Converter, but is: " + fieldType.getName() );
            }
        }

        return resultingType;
    }

    private boolean isDestinationTypeCollection()
    {
        return Collection.class.isAssignableFrom( fieldType );
    }

    @SuppressWarnings( "unchecked" )
    public Class<?> getDestinationType()
    {
        return this.destinationType;
    }

    public StructureMapping getComponentParameter()
    {
        return componentParameter;
    }

    public Class<?> getFieldType()
    {
        return this.fieldType;
    }

    @Override
    public ParamType getParamType()
    {
        return ParamType.TABLE;
    }

    @Override
    public Object getUnconvertedValueToJava( Object fieldMapCollection, ConverterCache converterCache )
    {
        if ( !hasConverter() )
        {
            @SuppressWarnings( {"unchecked"} ) // must be Collection, since there is no Converter
                    Class<? extends Collection> destinationType = ( Class<? extends Collection> ) getDestinationType();

            Collection<Object> collection = newCollectionInstance( destinationType );

            Collection<Map<String, Object>> rows = castToCollectionOfMaps( fieldMapCollection );

            if ( rows != null )
            {
                for ( Map<String, Object> tableMap : rows )
                {
                    Object elementBean = getComponentParameter().mapToJava( tableMap, converterCache );
                    collection.add( elementBean );
                }
            }

            if ( getFieldType().isArray() )
            {
                return ReflectionHelper.newArrayFromCollection( collection, getAssociatedType() );
            }
            else
            {
                return collection;
            }
        }
        else
        {
            throw new InternalHiberSapException(
                    "This method should only be called by the framework " +
                            "when the corresponding table field has a converter attached" );
        }
    }

    @Override
    protected Object getUnconvertedValueToSap( Object value, ConverterCache converterCache )
    {
        Collection bapiStructures;

        if ( getFieldType().isArray() )
        {
            bapiStructures = asList( value );
        }
        else
        {
            bapiStructures = ( Collection ) value;
        }

        List<Map<String, Object>> tableRows = new ArrayList<Map<String, Object>>();

        if ( bapiStructures != null )
        {
            for ( Object bapiStructure : bapiStructures )
            {

                @SuppressWarnings( {"unchecked"} )
                Map<String, Object> paramMap = ( Map<String, Object> ) getComponentParameter()
                        .mapToSap( bapiStructure, converterCache );
                tableRows.add( paramMap );
            }
        }

        return tableRows;
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

        TableMapping that = ( TableMapping ) o;

        if ( destinationType != null ? !destinationType.equals( that.destinationType ) : that.destinationType != null )
        {
            return false;
        }
        if ( componentParameter != null ? !componentParameter.equals( that.componentParameter ) :
             that.componentParameter != null )
        {
            return false;
        }
        //noinspection RedundantIfStatement
        if ( fieldType != null ? !fieldType.equals( that.fieldType ) : that.fieldType != null )
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + ( componentParameter != null ? componentParameter.hashCode() : 0 );
        result = 31 * result + ( fieldType != null ? fieldType.hashCode() : 0 );
        result = 31 * result + ( destinationType != null ? destinationType.hashCode() : 0 );
        return result;
    }
}
