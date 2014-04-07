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

package org.hibersap.mapping.model;

import org.hibersap.conversion.Converter;
import org.hibersap.conversion.ConverterCache;

/**
 * @author Carsten Erker
 */
public class FieldMapping extends ParameterMapping
{
    private static final long serialVersionUID = -7542970603293850477L;

    public FieldMapping( Class<?> associatedClass, String sapName, String javaName,
                         Class<? extends Converter> converter )
    {
        super( associatedClass, sapName, javaName, converter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParamType getParamType()
    {
        return ParamType.FIELD;
    }

    @Override
    protected Object getUnconvertedValueToJava( Object value, ConverterCache converterCache )
    {
        return value;
    }

    @Override
    protected Object getUnconvertedValueToSap( Object value, ConverterCache converterCache )
    {
        return value;
    }
}
