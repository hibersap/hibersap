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

package org.hibersap.interceptor.impl;

import org.apache.commons.lang.ArrayUtils;
import org.hibersap.MappingException;
import org.hibersap.SapException;
import org.hibersap.SapException.SapError;
import org.hibersap.annotations.ThrowExceptionOnError;
import org.hibersap.execution.UnsafeCastHelper;
import org.hibersap.interceptor.ExecutionInterceptor;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.mapping.model.ErrorHandling;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Throws a SapException after the execution of a BAPI call when SAP returned an Error message.
 * Checks the given RETURN structure or table parameter for the occurrence of the given error types.
 * The path to the RETURN structure and the error types are defined in the ThrowExceptionOnError
 * annotation.
 * 
 * @see org.hibersap.annotations.ThrowExceptionOnError
 * @author Carsten Erker
 */
public class SapErrorInterceptor
    implements ExecutionInterceptor
{
    // TODO test with return table
    public void afterExecution( final BapiMapping bapiMapping, final Map<String, Object> functionMap )
        throws SapException
    {
        if ( bapiMapping.getErrorHandling().isThrowExceptionOnError() )
        {
            checkForErrors( bapiMapping, functionMap );
        }
    }

    public void beforeExecution( final BapiMapping bapiMapping, final Map<String, Object> functionMap )
    {
        // nothing to do
    }

    private void checkForErrors( final BapiMapping bapiMapping, final Map<String, Object> functionMap )
        throws SapException
    {
        final ErrorHandling errorHandling = bapiMapping.getErrorHandling();

        final String[] path = errorHandling.getPathToReturnStructure().split( "/" );
        final Map<String, Object> containingMap = getContainingMap( bapiMapping, functionMap, path );

        final String nameOfReturnStructure = path[path.length - 1];
        final Object returnObj = containingMap.get( nameOfReturnStructure );
        Collection<Map<String, Object>> lines;

        if ( returnObj instanceof Map )
        {
            // we got a return structure
            final Map<String, Object> returnMap = UnsafeCastHelper.castToMap( returnObj );
            lines = Collections.singletonList( returnMap );
        }
        else if ( returnObj instanceof Collection )
        {
            // we got a return table
            lines = UnsafeCastHelper.castToCollectionOfMaps( returnObj );
        }
        else
        {
            throw new MappingException( "Checking for errors failed: Parameter returnStructure of Annotation "
                + ThrowExceptionOnError.class.getSimpleName() + " in Class " + bapiMapping.getAssociatedClass()
                + " does not point to a structure or table of function module " + bapiMapping.getBapiName() + "."
                + nameOfReturnStructure + " = " + returnObj );
        }

        final String[] messageTypes = errorHandling.getErrorMessageTypes();
        checkSapErrors( messageTypes, lines );
    }

    private Map<String, Object> getContainingMap( final BapiMapping bapiMapping, final Map<String, Object> functionMap,
                                                  final String[] path )
    {
        Map<String, Object> containingMap = functionMap;
        for ( int i = 0; i < path.length - 1; i++ )
        {
            final Object containingObj = containingMap.get( path[i].trim() );
            if ( containingObj instanceof Map )
            {
                containingMap = UnsafeCastHelper.castToMap( containingObj );
            }
            else
            {
                throw new MappingException( "Checking for errors failed: Path element " + path[i]
                    + " does not point to a valid structure type of function module " + bapiMapping.getBapiName()
                    + ", but is a " + containingObj + ". See parameter returnStructure of Annotation "
                    + ThrowExceptionOnError.class.getSimpleName() + " in Class " + bapiMapping.getAssociatedClass() );
            }
        }
        if ( containingMap == null )
        {
            throw new MappingException( "Checking for errors failed: Parameter returnStructure of Annotation "
                + ThrowExceptionOnError.class.getSimpleName() + " in Class " + bapiMapping.getAssociatedClass()
                + " does not contain a valid path for function module " + bapiMapping.getBapiName() );
        }
        return containingMap;
    }

    private void checkSapErrors( final String[] messageTypes, final Collection<Map<String, Object>> returnTable )
        throws SapException
    {
        final ArrayList<SapError> sapErrors = new ArrayList<SapError>();
        for ( final Map<String, Object> map : returnTable )
        {
            final String type = (String) map.get( "TYPE" );
            if ( ArrayUtils.contains( messageTypes, type ) )
            {
                // TODO make casts safe / error tolerant
                final String id = (String) map.get( "ID" );
                final String number = (String) map.get( "NUMBER" );
                final String message = (String) map.get( "MESSAGE" );
                final SapError sapError = new SapError( type, id, number, message );
                sapErrors.add( sapError );
            }
        }
        if ( !sapErrors.isEmpty() )
        {
            throw new SapException( sapErrors );
        }
    }
}
