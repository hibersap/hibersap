package org.hibersap.mapping.model;

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

import org.apache.commons.lang.StringUtils;
import org.hibersap.MappingException;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is the framework internal representation of mappings between SAP function modules (BAPIs)
 * and Java classes.
 * 
 * @author Carsten Erker
 */
public class BapiMapping
{
    public static class ErrorHandling
        implements Serializable
    {
        private final boolean throwExceptionOnError;

        private final String pathToReturnStructure;

        private final String[] errorMessageTypes;

        public ErrorHandling( String pathToReturnStructure, String[] errorMessageTypes )
        {
            this.errorMessageTypes = errorMessageTypes.clone();
            throwExceptionOnError = StringUtils.isNotEmpty( pathToReturnStructure );
            this.pathToReturnStructure = pathToReturnStructure;
        }

        public String[] getErrorMessageTypes()
        {
            return this.errorMessageTypes;
        }

        public String getPathToReturnStructure()
        {
            return this.pathToReturnStructure;
        }

        public boolean isThrowExceptionOnError()
        {
            return this.throwExceptionOnError;
        }
    }

    private final String bapiName;

    private Class<?> associatedClass;

    private final Set<ObjectMapping> importParams = new HashSet<ObjectMapping>();

    private final Set<ObjectMapping> exportParams = new HashSet<ObjectMapping>();

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

    public void addExportParameter( ObjectMapping parameter )
    {
        exportParams.add( parameter );
    }

    public void addImportParameter( ObjectMapping parameter )
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

    public Set<ObjectMapping> getExportParameters()
    {
        return exportParams;
    }

    public Set<ObjectMapping> getImportParameters()
    {
        return importParams;
    }

    public Set<TableMapping> getTableParameters()
    {
        return tableParams;
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "[BAPI: " + bapiName + ", MappedClass: " + associatedClass + "]";
    }
}
