package org.hibersap.mapping.model;

import java.io.Serializable;

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

/**
 * @author Carsten Erker
 */
public abstract class ParameterMapping
    implements Serializable
{
    public enum ParamType {
        FIELD, STRUCTURE, TABLE
    }

    private final Class<?> associatedType;

    private final String sapName;

    private final String javaName;

    public ParameterMapping( Class<?> associatedType, String sapName, String javaName )
    {
        this.associatedType = associatedType;
        this.sapName = sapName;
        this.javaName = javaName;
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

    public String getSapName()
    {
        return sapName;
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

        ParameterMapping that = (ParameterMapping) o;

        if (associatedType != null ? !associatedType.equals(that.associatedType) : that.associatedType != null)
        {
            return false;
        }
        if (javaName != null ? !javaName.equals(that.javaName) : that.javaName != null)
        {
            return false;
        }
        if (sapName != null ? !sapName.equals(that.sapName) : that.sapName != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = associatedType != null ? associatedType.hashCode() : 0;
        result = 31 * result + (sapName != null ? sapName.hashCode() : 0);
        result = 31 * result + (javaName != null ? javaName.hashCode() : 0);
        return result;
    }
}