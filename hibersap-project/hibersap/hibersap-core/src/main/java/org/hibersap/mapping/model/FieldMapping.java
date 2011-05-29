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

import org.hibersap.conversion.Converter;

/**
 * @author Carsten Erker
 */
public class FieldMapping extends ObjectMapping
{
    private static final long serialVersionUID = -7542970603293850477L;

    private final Class<? extends Converter> converter;

    public FieldMapping( Class<?> associatedClass, String sapName, String javaName, Class<? extends Converter> converter )
    {
        super( associatedClass, sapName, javaName );
        this.converter = converter;
    }

    public Class<? extends Converter> getConverter()
    {
        return this.converter;
    }

    /**
     * {@inheritDoc}
     */
    public ParamType getParamType()
    {
        return ParamType.FIELD;
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

        FieldMapping that = (FieldMapping) o;

        if ( converter != null ? !converter.equals( that.converter ) : that.converter != null )
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + ( converter != null ? converter.hashCode() : 0 );
        return result;
    }
}
