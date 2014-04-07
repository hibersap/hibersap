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

package org.hibersap.mapping;

import org.hibersap.annotations.Convert;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.annotations.Table;
import org.hibersap.conversion.Converter;

import java.lang.reflect.Field;
import java.util.Collection;


/**
 * @author Carsten Erker
 */
class BapiField
{
    private static final Class<Parameter> PARAM = Parameter.class;

    private static final Class<Import> IMPORT = Import.class;

    private static final Class<Export> EXPORT = Export.class;

    private static final Class<Table> TABLE = Table.class;

    private static final Class<Convert> CONVERT = Convert.class;

    private final Field field;

    public BapiField( Field field )
    {
        this.field = field;
    }

    public Class<?> getArrayType()
    {
        Class<?> associatedType = field.getType();
        return ReflectionHelper.getArrayType( associatedType );
    }

    /**
     * @return The type.
     */
    public Class<?> getAssociatedType()
    {
        if ( field.getType().isArray() )
        {
            return getArrayType();
        }
        if ( Collection.class.isAssignableFrom( field.getType() ) )
        {
            return getGenericType();
        }
        return getType();
    }

    public Class<? extends Converter> getConverter()
    {
        if ( field.isAnnotationPresent( CONVERT ) )
        {
            Convert convert = field.getAnnotation( CONVERT );
            return convert.converter();
        }
        return null;
    }

    public Class<?> getGenericType()
    {
        return ReflectionHelper.getGenericType( field );
    }

    public String getName()
    {
        return field.getName();
    }

    private Parameter getParameterAnnotation()
    {
        return field.getAnnotation( PARAM );
    }

    public String getSapName()
    {
        return getParameterAnnotation().value();
    }

    public Class<?> getType()
    {
        return field.getType();
    }

    public boolean isExport()
    {
        return field.isAnnotationPresent( EXPORT );
    }

    public boolean isImport()
    {
        return field.isAnnotationPresent( IMPORT );
    }

    public boolean isParameter()
    {
        return field.isAnnotationPresent( PARAM );
    }

    public boolean isStructure()
    {
        boolean result = false;
        if ( isParameter() )
        {
            result = getParameterAnnotation().type() == ParameterType.STRUCTURE;
        }
        return result;
    }

    public boolean isTable()
    {
        return field.isAnnotationPresent( TABLE );
    }
}
