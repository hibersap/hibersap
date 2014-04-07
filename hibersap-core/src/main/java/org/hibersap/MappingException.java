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

/**
 * A MappingException is thrown by the framework when there are errors mapping BAPI classes.
 * 
 * @author Carsten Erker
 */
public class MappingException
    extends HibersapException
{
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new MappingException with a detail message and a cause.
     * 
     * @param msg The message
     * @param cause The cause
     */
    public MappingException( String msg, Throwable cause )
    {
        super( msg, cause );
    }

    /**
     * Constructs a new MappingException with the specified cause.
     * 
     * @param cause The cause
     */
    public MappingException( Throwable cause )
    {
        super( cause );
    }

    /**
     * Constructs a new MappingException with a detail message.
     * 
     * @param msg The message
     */
    public MappingException( String msg )
    {
        super( msg );
    }
}
