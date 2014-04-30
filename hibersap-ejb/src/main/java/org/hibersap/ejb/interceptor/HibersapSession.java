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

package org.hibersap.ejb.interceptor;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The HibersapSession annotation can be used on an EJB field to let the HibersapSessionInterceptor inject the current
 * Hibersap session.
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface HibersapSession {

    /**
     * Holds the JNDI name for looking up the corresponding Hibersap SessionManager.
     *
     * @return The JNDI name to which the SessionManager is bound.
     */
    String value();
}
