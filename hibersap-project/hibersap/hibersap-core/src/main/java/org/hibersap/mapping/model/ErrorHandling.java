package org.hibersap.mapping.model;

import org.apache.commons.lang.StringUtils;

/**
 * Holds information for error handling of a BAPI mapping.
 * 
 * @author Carsten Erker
 */
public class ErrorHandling
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
        return errorMessageTypes == null ? null : errorMessageTypes;
    }

    public String getPathToReturnStructure()
    {
        return this.pathToReturnStructure;
    }

    public boolean isThrowExceptionOnError()
    {
        return this.throwExceptionOnError;
    }
}
