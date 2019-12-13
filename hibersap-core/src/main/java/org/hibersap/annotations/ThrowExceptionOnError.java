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

package org.hibersap.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.hibersap.bapi.BapiConstants;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Generate a SapException when the function module returns errors in a RETURN structure or table.
 *
 * @author Carsten Erker
 */
@Retention(RUNTIME)
@Target(value = TYPE)
public @interface ThrowExceptionOnError {

    /**
     * Contains the path to the BAPI's return structure or Table. The first element should be
     * 'EXPORT' or 'TABLE' to indicate if the return structure is defined as an export or table
     * parameter. The last element is the name of the return structure, usually 'RETURN'.
     */
    String returnStructure() default BapiConstants.EXPORT_RETURN;

    /**
     * The message types which Hibersap shall interpret as an error. In these cases an Exception
     * will be thrown. The RETURN structure's field TYPE is compared to the message types.
     */
    String[] errorMessageTypes() default {"E", "A"};
}
