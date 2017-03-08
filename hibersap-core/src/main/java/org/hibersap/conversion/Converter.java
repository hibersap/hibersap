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

package org.hibersap.conversion;

import java.io.Serializable;

/**
 * Implementation of a data type converter. Converts on-the-fly from SAP data type to any Java type.
 * The corresponding field in the BAPI class can thus be independent of the SAP type. E .g., a SAP
 * character type which represents a boolean (since ABAP does not have a boolean data type) can be
 * converted to a Java boolean. To utilize a Converter, annotate the BAPI class field using the @Convert
 * annotation.
 *
 * @author Carsten Erker
 */
public interface Converter<J, S> extends Serializable {

    /**
     * Convert the SAP value, as it is returned by the underlying interfacing technology (e.g. the
     * SAP Java Connector, JCo) to the Java data type of the corresponding BAPI class field.
     * Hibersap will call this method after calling the SAP function and before setting the field in
     * the Java class.
     *
     * @param sapValue The object which is returned by the SAP interface
     * @return The converted value
     * @throws ConversionException if the value can not be converted
     */
    J convertToJava(S sapValue) throws ConversionException;

    /**
     * Convert the Java value of the corresponding BAPI class field to the data type as it is
     * expected by the underlying interfacing technology (e.g. the SAP Java Connector, JCo).
     * Hibersap will call this method before calling the SAP function.
     *
     * @param javaValue The value of the BAPI class field
     * @return The converted value
     * @throws ConversionException if the value can not be converted
     */
    S convertToSap(J javaValue) throws ConversionException;
}
