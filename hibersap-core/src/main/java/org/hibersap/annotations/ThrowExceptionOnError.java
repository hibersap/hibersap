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

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Generate a SapException when the function module returns errors in a RETURN structure or table.
 * 
 * @author Carsten Erker
 */
@Retention(RUNTIME)
@Target(value = TYPE)
public @interface ThrowExceptionOnError
{
    /**
     * Contains the path to the BAPI's return structure or Table. The first element should be
     * 'EXPORT' or 'TABLE' to indicate if the return structure is defined as an export or table
     * parameter. The last element is the name of the return structure, usually 'RETURN'.
     */
    String returnStructure() default "EXPORT/RETURN";

    /**
     * The message types which Hibersap shall interpret as an error. In these cases an Exception
     * will be thrown. The RETURN structure's field TYPE is compared to the message types.
     */
    String[] errorMessageTypes() default { "E", "A" };
}
