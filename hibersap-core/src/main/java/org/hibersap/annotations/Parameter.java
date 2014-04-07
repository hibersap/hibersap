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
@Retention( RUNTIME )
@Target( value = FIELD )
public @interface Parameter
{
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
