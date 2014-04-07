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

package org.hibersap;

import java.util.Collections;
import java.util.List;

/**
 * A SapException holds error messages corresponding to the values of SAP return structures. These
 * are commonly used in SAP functions to inform the client about any errors, warnings or other
 * information. It is used by the org.hibersap.interceptor.impl.SapErrorInterceptor but can be utilized by
 * the application to make its own evaluations of SAP return structures.
 * 
 * @see org.hibersap.interceptor.impl.SapErrorInterceptor
 * 
 * @author Carsten Erker
 */
public class SapException
    extends HibersapException
{
    private static final long serialVersionUID = 1L;

    private final List<SapError> sapErrors;

    public SapException( List<SapError> sapErrors )
    {
        super( "Error(s) occurred when calling function module" );
        this.sapErrors = sapErrors;

    }

    public SapException( SapError sapError )
    {
        this( Collections.singletonList( sapError ) );
    }

    public List<SapError> getErrors()
    {
        return sapErrors;
    }

    @Override
    public String toString()
    {
        return super.toString() + " ( " + sapErrors.toString() + " )";
    }

    /**
     * Hold information about (error, warning, info, etc.) values returned by a SAP function.
     * 
     * @author cerker
     */
    public static class SapError
    {
        private final String message;

        private final String type;

        private final String id;

        private final String number;

        public SapError( String type, String id, String number, String message )
        {
            this.type = type;
            this.id = id;
            this.number = number;
            this.message = message;
        }

        public String getType()
        {
            return type;
        }

        public String getId()
        {
            return id;
        }

        public String getNumber()
        {
            return number;
        }

        public String getMessage()
        {
            return message;
        }

        @Override
        public String toString()
        {
            return "SapError[type=" + type + ",id=" + id + ",number=" + number + ",message=" + message + "]";
        }
    }
}
