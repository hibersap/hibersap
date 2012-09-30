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

package org.hibersap.configuration.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( name = "", propOrder = {"bapiInterceptorClasses"} )
public final class BapiInterceptorClasses implements Serializable
{
    @XmlElement( name = "bapi-interceptor-class" )
    protected List<String> bapiInterceptorClasses = new ArrayList<String>();

    public List<String> getBapiInterceptorClasses()
    {
        return this.bapiInterceptorClasses;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }

        BapiInterceptorClasses that = ( BapiInterceptorClasses ) o;

        if ( bapiInterceptorClasses != null ? !bapiInterceptorClasses.equals( that.bapiInterceptorClasses ) :
             that.bapiInterceptorClasses != null )
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return bapiInterceptorClasses != null ? bapiInterceptorClasses.hashCode() : 0;
    }

    @Override
    public String toString()
    {
        return "BapiInterceptorClasses{" +
                "bapiInterceptorClasses=" + bapiInterceptorClasses +
                '}';
    }
}
