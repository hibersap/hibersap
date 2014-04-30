/*
 * Copyright (c) 2008-2014 akquinet tech@spree GmbH
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
 * HibersapException is an unchecked exception which is commonly thrown by the framework when
 * something goes wrong.
 *
 * @author Carsten Erker
 */
public class HibersapException
        extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new HibersapException with the specified cause.
     *
     * @param cause The cause
     */
    public HibersapException( final Throwable cause ) {
        super( cause );
    }

    /**
     * Constructs a new HibersapException with a detail message and a cause.
     *
     * @param msg   The message
     * @param cause The cause
     */
    public HibersapException( final String msg, final Throwable cause ) {
        super( msg, cause );
    }

    /**
     * Constructs a new HibersapException with a detail message.
     *
     * @param msg The message
     */
    public HibersapException( final String msg ) {
        super( msg );
    }
}
