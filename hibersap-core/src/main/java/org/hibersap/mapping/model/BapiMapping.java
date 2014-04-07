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

import org.apache.commons.lang.StringUtils;
import org.hibersap.MappingException;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is the framework internal representation of mappings between SAP function modules
 * (BAPIs) and Java classes.
 *
 * @author Carsten Erker
 */
public class BapiMapping implements Serializable
{
    private static final long serialVersionUID = 6716958693316907614L;

    private final String bapiName;

    private Class<?> associatedClass;

    private final Set<ParameterMapping> importParams = new HashSet<ParameterMapping>();

    private final Set<ParameterMapping> exportParams = new HashSet<ParameterMapping>();

    private final Set<TableMapping> tableParams = new HashSet<TableMapping>();

    private final ErrorHandling errorHandling;

    public BapiMapping( Class<?> associatedClass, String bapiName, ErrorHandling errorHandling )
    {
        if ( StringUtils.isEmpty( bapiName ) )
        {
            throw new MappingException( "Bapi name for class " + associatedClass.getName() + " is empty" );
        }
        this.associatedClass = associatedClass;
        this.bapiName = bapiName;
        this.errorHandling = errorHandling;
    }

    public void addExportParameter( ParameterMapping parameter )
    {
        exportParams.add( parameter );
    }

    public void addImportParameter( ParameterMapping parameter )
    {
        importParams.add( parameter );
    }

    public void addTableParameter( TableMapping parameter )
    {
        tableParams.add( parameter );
    }

    public Class<?> getAssociatedClass()
    {
        return associatedClass;
    }

    public String getBapiName()
    {
        return bapiName;
    }

    public ErrorHandling getErrorHandling()
    {
        return this.errorHandling;
    }

    public Set<ParameterMapping> getExportParameters()
    {
        return exportParams;
    }

    public Set<ParameterMapping> getImportParameters()
    {
        return importParams;
    }

    public Set<TableMapping> getTableParameters()
    {
        return tableParams;
    }

    public Set<ParameterMapping> getAllParameters()
    {
        HashSet<ParameterMapping> parameters = new HashSet<ParameterMapping>();
        parameters.addAll( importParams );
        parameters.addAll( exportParams );
        parameters.addAll( tableParams );
        return parameters;
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "[BAPI: " + bapiName + ", MappedClass: " + associatedClass + "]";
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

        BapiMapping that = ( BapiMapping ) o;

        if ( associatedClass != null ? !associatedClass.equals( that.associatedClass ) : that.associatedClass != null )
        {
            return false;
        }
        if ( bapiName != null ? !bapiName.equals( that.bapiName ) : that.bapiName != null )
        {
            return false;
        }
        if ( errorHandling != null ? !errorHandling.equals( that.errorHandling ) : that.errorHandling != null )
        {
            return false;
        }
        if ( exportParams != null ? !exportParams.equals( that.exportParams ) : that.exportParams != null )
        {
            return false;
        }
        if ( importParams != null ? !importParams.equals( that.importParams ) : that.importParams != null )
        {
            return false;
        }
        //noinspection RedundantIfStatement
        if ( tableParams != null ? !tableParams.equals( that.tableParams ) : that.tableParams != null )
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = bapiName != null ? bapiName.hashCode() : 0;
        result = 31 * result + ( associatedClass != null ? associatedClass.hashCode() : 0 );
        result = 31 * result + ( importParams != null ? importParams.hashCode() : 0 );
        result = 31 * result + ( exportParams != null ? exportParams.hashCode() : 0 );
        result = 31 * result + ( tableParams != null ? tableParams.hashCode() : 0 );
        result = 31 * result + ( errorHandling != null ? errorHandling.hashCode() : 0 );
        return result;
    }
}
