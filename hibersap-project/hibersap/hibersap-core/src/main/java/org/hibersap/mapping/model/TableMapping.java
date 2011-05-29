package org.hibersap.mapping.model;

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

import org.hibersap.MappingException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Carsten Erker
 */
public class TableMapping extends ParameterMapping
{
    private static final long serialVersionUID = 6134694196341208013L;

    private final StructureMapping componentParameter;

    private final Class<?> fieldType;

    @SuppressWarnings("unchecked")
    private final Class<? extends Collection> collectionType;

    /**
     * @param fieldType The type of the field in the bean; may be a Collection interface like List,
     *            Set, Collection, a concrete class that implements Collection or an array.
     * @param associatedType The type of the elements, i.e. a Pojo class.
     * @param sapName The table's name in SAP.
     * @param javaName The Java field name of the Collection or array.
     * @param componentParameter A StructureMapping containing the table's fields.
     */
    public TableMapping( Class<?> fieldType, Class<?> associatedType, String sapName, String javaName,
                         StructureMapping componentParameter )
    {
        super( associatedType, sapName, javaName );
        this.componentParameter = componentParameter;
        this.fieldType = fieldType;
        this.collectionType = determineCollectionType( fieldType );
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Collection> determineCollectionType( Class<?> type )
    {
        Class<? extends Collection> resultingType;

        if ( Collection.class.isAssignableFrom( type ) )
        {
            if ( type.isInterface() )
            {
                if ( List.class.equals( type ) )
                {
                    resultingType = ArrayList.class;
                }
                else if ( Set.class.equals( type ) )
                {
                    resultingType = HashSet.class;
                }
                else if ( Collection.class.equals( type ) )
                {
                    resultingType = ArrayList.class;
                }
                else
                {
                    throw new MappingException( "Collection of type " + type.getName() + " not supported. See Field "
                        + getJavaName() );
                }
            }
            else
            {
                resultingType = (Class<? extends Collection>) type;
            }
        }
        else if ( type.isArray() )
        {
            resultingType = ArrayList.class;
        }
        else
        {
            throw new MappingException( "The field " + getJavaName() + " must be an array or an implementation of "
                + Collection.class.getName() + ", but is: " + fieldType.getName() );
        }

        return resultingType;
    }

    @SuppressWarnings("unchecked")
    public Class<? extends Collection> getCollectionType()
    {
        return this.collectionType;
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
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        if (!super.equals(o))
        {
            return false;
        }

        TableMapping that = (TableMapping) o;

        if (collectionType != null ? !collectionType.equals(that.collectionType) : that.collectionType != null)
        {
            return false;
        }
        if (componentParameter != null ? !componentParameter.equals(that.componentParameter) : that.componentParameter != null)
        {
            return false;
        }
        if (fieldType != null ? !fieldType.equals(that.fieldType) : that.fieldType != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + (componentParameter != null ? componentParameter.hashCode() : 0);
        result = 31 * result + (fieldType != null ? fieldType.hashCode() : 0);
        result = 31 * result + (collectionType != null ? collectionType.hashCode() : 0);
        return result;
    }
}
