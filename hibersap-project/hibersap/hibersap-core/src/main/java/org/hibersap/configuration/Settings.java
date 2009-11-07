package org.hibersap.configuration;

/*
 * Copyright (C) 2008 akquinet tech@spree GmbH
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

import org.hibersap.session.Context;

import java.io.Serializable;

/**
 * @author Carsten Erker
 */
public final class Settings
    implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Context context;

    public Context getContext()
    {
        return context;
    }

    public void setContext( Context context )
    {
        this.context = context;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( context == null ) ? 0 : context.hashCode() );
        return result;
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
        {
            return true;
        }
        if ( obj == null )
        {
            return false;
        }
        if ( getClass() != obj.getClass() )
        {
            return false;
        }
        Settings other = (Settings) obj;
        if ( context == null )
        {
            if ( other.context != null )
            {
                return false;
            }
        }
        else if ( !context.equals( other.context ) )
        {
            return false;
        }
        return true;
    }
}
