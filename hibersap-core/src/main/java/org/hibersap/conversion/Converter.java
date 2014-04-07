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
public interface Converter<J, S> extends Serializable
{
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
    J convertToJava( S sapValue ) throws ConversionException;

    /**
     * Convert the Java value of the corresponding BAPI class field to the data type as it is
     * expected by the underlying interfacing technology (e.g. the SAP Java Connector, JCo).
     * Hibersap will call this method before calling the SAP function.
     *
     * @param javaValue The value of the BAPI class field
     * @return The converted value
     * @throws ConversionException if the value can not be converted
     */
    S convertToSap( J javaValue ) throws ConversionException;
}
