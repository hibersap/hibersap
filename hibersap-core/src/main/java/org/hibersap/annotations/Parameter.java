/*
 * Copyright (c) 2008-2025 tech@spree GmbH
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

package org.hibersap.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Maps the Java field to a parameter of the remote function module's interface.
 *
 * @author Carsten Erker
 */
@Retention(RUNTIME)
@Target(value = FIELD)
public @interface Parameter {

    /**
     * The name of the function module's parameter.
     *
     * @return The parameter name.
     */
    String value();

    /**
     * The type of the parameter, either SIMPLE for scalar types or STRUCTURE for complex types.
     *
     * @return The parameter type.
     */
    ParameterType type() default ParameterType.SIMPLE;
}
