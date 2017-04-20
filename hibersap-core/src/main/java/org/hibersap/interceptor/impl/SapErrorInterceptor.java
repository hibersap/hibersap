/*
 * Copyright (c) 2008-2017 akquinet tech@spree GmbH
 *
 * This file is part of Hibersap.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hibersap.interceptor.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.lang.ArrayUtils;
import org.hibersap.MappingException;
import org.hibersap.SapException;
import org.hibersap.SapException.SapError;
import org.hibersap.annotations.ThrowExceptionOnError;
import org.hibersap.execution.UnsafeCastHelper;
import org.hibersap.interceptor.ExecutionInterceptor;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.mapping.model.ErrorHandling;

/**
 * Throws a SapException after the execution of a BAPI call when SAP returned an Error message.
 * Checks the given RETURN structure or table parameter for the occurrence of the given error types.
 * The path to the RETURN structure and the error types are defined in the ThrowExceptionOnError
 * annotation.
 *
 * @author Carsten Erker
 * @see org.hibersap.annotations.ThrowExceptionOnError
 */
public class SapErrorInterceptor implements ExecutionInterceptor {

    // TODO test with return table
    public void afterExecution(final BapiMapping bapiMapping, final Map<String, Object> functionMap) {
        if (bapiMapping.getErrorHandling().isThrowExceptionOnError()) {
            checkForErrors(bapiMapping, functionMap);
        }
    }

    public void beforeExecution(final BapiMapping bapiMapping, final Map<String, Object> functionMap) {
        // nothing to do
    }

    private void checkForErrors(final BapiMapping bapiMapping, final Map<String, Object> functionMap) {
        final ErrorHandling errorHandling = bapiMapping.getErrorHandling();

        final String[] path = errorHandling.getPathToReturnStructure().split("/");
        final Map<String, Object> containingMap = getContainingMap(bapiMapping, functionMap, path);

        final String nameOfReturnStructure = path[path.length - 1];
        final Object returnObj = containingMap.get(nameOfReturnStructure);
        Collection<Map<String, Object>> lines;

        if (returnObj instanceof Map) {
            // we got a return structure
            final Map<String, Object> returnMap = UnsafeCastHelper.castToMap(returnObj);
            lines = Collections.singletonList(returnMap);
        } else if (returnObj instanceof Collection) {
            // we got a return table
            lines = UnsafeCastHelper.castToCollectionOfMaps(returnObj);
        } else {
            throw new MappingException("Checking for errors failed: Parameter returnStructure of Annotation "
                    + ThrowExceptionOnError.class.getSimpleName() + " in Class " + bapiMapping.getAssociatedClass()
                    + " does not point to a structure or table of function module " + bapiMapping.getBapiName() + "."
                    + nameOfReturnStructure + " = " + returnObj);
        }

        final String[] messageTypes = errorHandling.getErrorMessageTypes();
        checkSapErrors(messageTypes, lines);
    }

    private Map<String, Object> getContainingMap(final BapiMapping bapiMapping,
                                                 final Map<String, Object> functionMap,
                                                 final String[] path) {
        Map<String, Object> containingMap = functionMap;
        for (int i = 0; i < path.length - 1; i++) {
            final Object containingObj = containingMap.get(path[i].trim());
            if (containingObj instanceof Map) {
                containingMap = UnsafeCastHelper.castToMap(containingObj);
            } else {
                throw new MappingException("Checking for errors failed: Path element " + path[i]
                        + " does not point to a valid structure type of function module " + bapiMapping.getBapiName()
                        + ", but is a " + containingObj + ". See parameter returnStructure of Annotation "
                        + ThrowExceptionOnError.class.getSimpleName() + " in Class " + bapiMapping.getAssociatedClass());
            }
        }
        if (containingMap == null) {
            throw new MappingException("Checking for errors failed: Parameter returnStructure of Annotation "
                    + ThrowExceptionOnError.class.getSimpleName() + " in Class " + bapiMapping.getAssociatedClass()
                    + " does not contain a valid path for function module " + bapiMapping.getBapiName());
        }
        return containingMap;
    }

    private void checkSapErrors(final String[] messageTypes, final Collection<Map<String, Object>> returnTable) {
        final ArrayList<SapError> sapErrors = new ArrayList<SapError>();
        for (final Map<String, Object> map : returnTable) {
            final String type = (String) map.get("TYPE");
            if (ArrayUtils.contains(messageTypes, type)) {
                // TODO make casts safe / error tolerant
                final String id = (String) map.get("ID");
                final String number = (String) map.get("NUMBER");
                final String message = (String) map.get("MESSAGE");
                final SapError sapError = new SapError(type, id, number, message);
                sapErrors.add(sapError);
            }
        }
        if (!sapErrors.isEmpty()) {
            throw new SapException(sapErrors);
        }
    }
}
