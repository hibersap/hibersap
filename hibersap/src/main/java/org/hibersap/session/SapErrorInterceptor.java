package org.hibersap.session;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.hibersap.CallbackException;
import org.hibersap.MappingException;
import org.hibersap.SapException;
import org.hibersap.SapException.SapError;
import org.hibersap.annotations.ThrowExceptionOnError;
import org.hibersap.execution.UnsafeCastHelper;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.mapping.model.BapiMapping.ErrorHandling;

/**
 * @author Carsten Erker
 */
public class SapErrorInterceptor
    implements ExecutionInterceptor
{
    // TODO test with return table
    public void afterExecute( BapiMapping bapiMapping, Map<String, Object> functionMap )
        throws CallbackException
    {
        if ( bapiMapping.getErrorHandling().isThrowExceptionOnError() )
        {
            checkForErrors( bapiMapping, functionMap );
        }
    }

    public void beforeExecute( BapiMapping bapiMapping, Map<String, Object> functionMap )
        throws CallbackException
    {
        // nothing to do
    }

    private void checkForErrors( BapiMapping bapiMapping, Map<String, Object> functionMap )
    {
        ErrorHandling errorHandling = bapiMapping.getErrorHandling();

        String[] path = errorHandling.getPathToReturnStructure().split( "/" );
        Map<String, Object> containingMap = getContainingMap( bapiMapping, functionMap, path );

        String nameOfReturnStructure = path[path.length - 1];
        Object returnObj = containingMap.get( nameOfReturnStructure );
        Collection<Map<String, Object>> lines;

        if ( returnObj instanceof Map )
        {
            // we got a return structure
            Map<String, Object> returnMap = UnsafeCastHelper.castToMap( returnObj );
            lines = Collections.singletonList( returnMap );
        }
        else if ( Collection.class.isAssignableFrom( returnObj.getClass() ) )
        {
            // we got a return table
            lines = UnsafeCastHelper.castToCollectionOfMaps( returnObj );
        }
        else
        {
            throw new MappingException( "Checking for errors failed: Parameter returnStructure of Annotation "
                + ThrowExceptionOnError.class.getSimpleName() + " in Class " + bapiMapping.getAssociatedClass()
                + " does not point to a structure or table of function module " + bapiMapping.getBapiName() );
        }

        String[] messageTypes = errorHandling.getErrorMessageTypes();
        checkSapErrors( messageTypes, lines );
    }

    private Map<String, Object> getContainingMap( BapiMapping bapiMapping, Map<String, Object> functionMap,
                                                  String[] path )
    {
        Map<String, Object> containingMap = functionMap;
        for ( int i = 0; i < path.length - 1; i++ )
        {
            Object containingObj = containingMap.get( path[i].trim() );
            if ( containingObj instanceof Map )
            {
                containingMap = UnsafeCastHelper.castToMap( containingObj );
            }
            else
            {
                throw new MappingException( "Checking for errors failed: Path element " + path[i]
                    + " does not point to a valid structure type of function module " + bapiMapping.getBapiName()
                    + ". See parameter returnStructure of Annotation " + ThrowExceptionOnError.class.getSimpleName()
                    + " in Class " + bapiMapping.getAssociatedClass() );
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

    private void checkSapErrors( String[] messageTypes, Collection<Map<String, Object>> returnTable )
    {
        ArrayList<SapError> sapErrors = new ArrayList<SapError>();
        for ( Map<String, Object> map : returnTable )
        {
            String type = (String) map.get( "TYPE" );
            if ( ArrayUtils.contains( messageTypes, type ) )
            {
                // TODO make casts safe / error tolerant
                String id = (String) map.get( "ID" );
                String number = (String) map.get( "NUMBER" );
                String message = (String) map.get( "MESSAGE" );
                SapError sapError = new SapError( type, id, number, message );
                sapErrors.add( sapError );
            }
        }
        if ( !sapErrors.isEmpty() )
        {
            throw new SapException( sapErrors );
        }
    }
}
