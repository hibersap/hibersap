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

package org.hibersap;

import java.util.Collections;
import java.util.List;

/**
 * A SapException holds error messages corresponding to the values of SAP return structures. These
 * are commonly used in SAP functions to inform the client about any errors, warnings or other
 * information. It is used by the org.hibersap.interceptor.impl.SapErrorInterceptor but can be utilized by
 * the application to make its own evaluations of SAP return structures.
 *
 * @author Carsten Erker
 * @see org.hibersap.interceptor.impl.SapErrorInterceptor
 */
public class SapException extends HibersapException {

    private static final long serialVersionUID = 1L;

    private final List<SapError> sapErrors;

    public SapException(final List<SapError> sapErrors) {
        super("Error(s) occurred when calling function module");
        this.sapErrors = sapErrors;
    }

    public SapException(final SapError sapError) {
        this(Collections.singletonList(sapError));
    }

    public List<SapError> getErrors() {
        return sapErrors;
    }

    @Override
    public String toString() {
        return super.toString() + " ( " + sapErrors.toString() + " )";
    }

    /**
     * Hold information about (error, warning, info, etc.) values returned by a SAP function.
     *
     * @author cerker
     */
    public static class SapError {

        private final String message;

        private final String type;

        private final String id;

        private final String number;

        public SapError(final String type, final String id, final String number, final String message) {
            this.type = type;
            this.id = id;
            this.number = number;
            this.message = message;
        }

        public String getType() {
            return type;
        }

        public String getId() {
            return id;
        }

        public String getNumber() {
            return number;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return "SapError[type=" + type + ",id=" + id + ",number=" + number + ",message=" + message + "]";
        }
    }
}
