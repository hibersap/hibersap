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

/**
 * A MappingException is thrown by the framework when there are errors mapping BAPI classes.
 *
 * @author Carsten Erker
 */
public class MappingException extends HibersapException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new MappingException with a detail message and a cause.
     *
     * @param msg The message
     * @param cause The cause
     */
    public MappingException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs a new MappingException with the specified cause.
     *
     * @param cause The cause
     */
    public MappingException(final Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new MappingException with a detail message.
     *
     * @param msg The message
     */
    public MappingException(final String msg) {
        super(msg);
    }
}
