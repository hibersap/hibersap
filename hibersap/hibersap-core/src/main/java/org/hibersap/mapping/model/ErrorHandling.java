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

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Holds information for error handling of a BAPI mapping.
 * 
 * @author Carsten Erker
 */
public class ErrorHandling implements Serializable
{
    private static final long serialVersionUID = 8008066068040245973L;

    private final boolean throwExceptionOnError;

    private final String pathToReturnStructure;

    private final String[] errorMessageTypes;

    public ErrorHandling( String pathToReturnStructure, String[] errorMessageTypes )
    {
        this.errorMessageTypes = errorMessageTypes == null ? null : errorMessageTypes;
        throwExceptionOnError = StringUtils.isNotEmpty( pathToReturnStructure );
        this.pathToReturnStructure = pathToReturnStructure;
    }

    public String[] getErrorMessageTypes()
    {
        return errorMessageTypes == null ? null : Arrays.copyOf(errorMessageTypes, errorMessageTypes.length);
    }

    public String getPathToReturnStructure()
    {
        return this.pathToReturnStructure;
    }

    public boolean isThrowExceptionOnError()
    {
        return this.throwExceptionOnError;
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

        ErrorHandling that = (ErrorHandling) o;

        if (throwExceptionOnError != that.throwExceptionOnError)
        {
            return false;
        }
        if (!Arrays.equals(errorMessageTypes, that.errorMessageTypes))
        {
            return false;
        }
        if (pathToReturnStructure != null ? !pathToReturnStructure.equals(that.pathToReturnStructure) : that.pathToReturnStructure != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = (throwExceptionOnError ? 1 : 0);
        result = 31 * result + (pathToReturnStructure != null ? pathToReturnStructure.hashCode() : 0);
        result = 31 * result + (errorMessageTypes != null ? Arrays.hashCode(errorMessageTypes) : 0);
        return result;
    }
}
