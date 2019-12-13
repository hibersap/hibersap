/*
 * Copyright (c) 2008-2019 akquinet tech@spree GmbH
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

package org.hibersap.mapping.model;

import java.io.Serializable;
import java.util.Arrays;
import javax.annotation.Generated;
import org.apache.commons.lang3.StringUtils;

/**
 * Holds information for error handling of a BAPI mapping.
 *
 * @author Carsten Erker
 */
public class ErrorHandling implements Serializable {

    private static final long serialVersionUID = 8008066068040245973L;

    private final boolean throwExceptionOnError;

    private final String pathToReturnStructure;

    private final String[] errorMessageTypes;

    public ErrorHandling(final String pathToReturnStructure, final String[] errorMessageTypes) {
        this.errorMessageTypes = errorMessageTypes;
        throwExceptionOnError = StringUtils.isNotEmpty(pathToReturnStructure);
        this.pathToReturnStructure = pathToReturnStructure;
    }

    public String[] getErrorMessageTypes() {
        return errorMessageTypes == null ? null : Arrays.copyOf(errorMessageTypes, errorMessageTypes.length);
    }

    public String getPathToReturnStructure() {
        return this.pathToReturnStructure;
    }

    public boolean isThrowExceptionOnError() {
        return this.throwExceptionOnError;
    }

    @Override
    @Generated("IntelliJ IDEA")
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ErrorHandling that = (ErrorHandling) o;

        if (throwExceptionOnError != that.throwExceptionOnError) {
            return false;
        }
        if (!Arrays.equals(errorMessageTypes, that.errorMessageTypes)) {
            return false;
        }
        if (pathToReturnStructure != null ? !pathToReturnStructure.equals(that.pathToReturnStructure)
                : that.pathToReturnStructure != null) {
            return false;
        }

        return true;
    }

    @Override
    @Generated("IntelliJ IDEA")
    public int hashCode() {
        int result = (throwExceptionOnError ? 1 : 0);
        result = 31 * result + (pathToReturnStructure != null ? pathToReturnStructure.hashCode() : 0);
        result = 31 * result + (errorMessageTypes != null ? Arrays.hashCode(errorMessageTypes) : 0);
        return result;
    }
}
