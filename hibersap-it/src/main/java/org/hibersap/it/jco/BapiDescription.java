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

package org.hibersap.it.jco;

import org.hibersap.annotations.Parameter;

public class BapiDescription
{
    @Parameter( "OBJECTNAME" )
    private String businessObjectName;

    @Parameter( "BO_TEXT" )
    private String businessObjectDescription;

    @Parameter( "BAPINAME" )
    private String bapiName;

    @Parameter( "BAPI_TEXT" )
    private String bapiDescription;

    @Parameter( "ABAPNAME" )
    private String abapName;

    @SuppressWarnings( {"UnusedDeclaration"} )
    private BapiDescription()
    {
        // for Hibersap
    }

    BapiDescription( String businessObjectName, String businessObjectDescription, String bapiName,
                     String bapiDescription,
                     String abapName )
    {
        this.businessObjectName = businessObjectName;
        this.businessObjectDescription = businessObjectDescription;
        this.bapiName = bapiName;
        this.bapiDescription = bapiDescription;
        this.abapName = abapName;
    }

    public String getBusinessObjectName()
    {
        return businessObjectName;
    }

    public String getBusinessObjectDescription()
    {
        return businessObjectDescription;
    }

    public String getBapiName()
    {
        return bapiName;
    }

    public String getBapiDescription()
    {
        return bapiDescription;
    }

    public String getAbapName()
    {
        return abapName;
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

        BapiDescription that = ( BapiDescription ) o;

        if ( abapName != null ? !abapName.equals( that.abapName ) : that.abapName != null )
        {
            return false;
        }
        if ( bapiDescription != null ? !bapiDescription.equals( that.bapiDescription ) : that.bapiDescription != null )
        {
            return false;
        }
        if ( bapiName != null ? !bapiName.equals( that.bapiName ) : that.bapiName != null )
        {
            return false;
        }
        if ( businessObjectDescription != null ? !businessObjectDescription.equals( that.businessObjectDescription ) :
             that.businessObjectDescription != null )
        {
            return false;
        }
        if ( businessObjectName != null ? !businessObjectName.equals( that.businessObjectName ) :
             that.businessObjectName != null )
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = businessObjectName != null ? businessObjectName.hashCode() : 0;
        result = 31 * result + ( businessObjectDescription != null ? businessObjectDescription.hashCode() : 0 );
        result = 31 * result + ( bapiName != null ? bapiName.hashCode() : 0 );
        result = 31 * result + ( bapiDescription != null ? bapiDescription.hashCode() : 0 );
        result = 31 * result + ( abapName != null ? abapName.hashCode() : 0 );
        return result;
    }

    @Override
    public String toString()
    {
        return "BapiDescription{" +
                "businessObjectName='" + businessObjectName + '\'' +
                ", businessObjectDescription='" + businessObjectDescription + '\'' +
                ", bapiName='" + bapiName + '\'' +
                ", bapiDescription='" + bapiDescription + '\'' +
                ", abapName='" + abapName + '\'' +
                '}';
    }
}
