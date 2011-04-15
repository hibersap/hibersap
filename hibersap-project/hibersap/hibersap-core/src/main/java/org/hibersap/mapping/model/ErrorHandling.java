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
