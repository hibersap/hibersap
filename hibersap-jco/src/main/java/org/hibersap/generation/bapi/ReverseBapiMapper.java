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

package org.hibersap.generation.bapi;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFieldIterator;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoFunctionTemplate;
import com.sap.conn.jco.JCoParameterFieldIterator;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoRecord;
import org.hibersap.HibersapException;
import org.hibersap.InternalHiberSapException;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.mapping.model.FieldMapping;
import org.hibersap.mapping.model.ParameterMapping;
import org.hibersap.mapping.model.StructureMapping;
import org.hibersap.mapping.model.TableMapping;
import org.hibersap.session.SessionManager;

import java.util.List;
import java.util.Set;

public class ReverseBapiMapper
{
    public BapiMapping map( String bapiName, SessionManager sessionManager )
    {
        JCoDestination destination;
        try
        {
            String sfName = sessionManager.getConfig().getName();
            destination = JCoDestinationManager.getDestination( sfName );

            JCoFunctionTemplate ft = destination.getRepository().getFunctionTemplate( bapiName );
            JCoFunction function = ft.getFunction();

            BapiMapping mapping = new BapiMapping( null, function.getName(), null );

            mapFields( mapping.getImportParameters(), function.getImportParameterList() );
            mapFields( mapping.getExportParameters(), function.getExportParameterList() );
            mapFields( mapping.getTableParameters(), function.getTableParameterList() );

            return mapping;
        }
        catch ( JCoException e )
        {
            throw new HibersapException( e );
        }
    }

    @SuppressWarnings( "unchecked" )
    private void mapFields( Set<? extends ParameterMapping> set, JCoParameterList jcoParams )
    {
        if ( jcoParams == null )
        {
            return;
        }

        JCoParameterFieldIterator iter = jcoParams.getParameterFieldIterator();
        while ( iter.hasNextField() )
        {
            JCoField field = iter.nextField();
            ParameterMapping param = getParameterMapping( field );

            if ( ParameterMapping.ParamType.FIELD == param.getParamType() )
            {
                ( ( Set<FieldMapping> ) set ).add( ( FieldMapping ) param );
            }
            else if ( ParameterMapping.ParamType.STRUCTURE == param.getParamType() )
            {
                ( ( Set<StructureMapping> ) set ).add( ( StructureMapping ) param );
            }
            else if ( ParameterMapping.ParamType.TABLE == param.getParamType() )
            {
                ( ( Set<TableMapping> ) set ).add( ( TableMapping ) param );
            }
        }
    }

    private ParameterMapping getParameterMapping( JCoField field )
    {
        String javaFieldName = BapiFormatHelper.getCamelCaseSmall( field.getName() );

        if ( field.isStructure() )
        {
            StructureMapping structureMapping = new StructureMapping( null, field.getName(), javaFieldName, null );
            addFieldMappings( structureMapping, field.getStructure() );
            return structureMapping;
        }
        if ( field.isTable() )
        {
            StructureMapping structureMapping = new StructureMapping( null, field.getName(), javaFieldName, null );
            addFieldMappings( structureMapping, field.getTable() );
            return new TableMapping( List.class, null, field.getName(), javaFieldName, structureMapping, null );
        }

        return new FieldMapping( getAssociatedClass( field ), field.getName(), javaFieldName, null );
    }

    /**
     * Determins the Java type of the JCoField's value, i.e. of the value that is created by JCo when mapping
     * a parameter of a certain ABAP data type to the corresponding Java type.
     * The JCoField interface returns the canonical class name of the Java type, which is in the given context
     * mostly the same as the type returned by Class.getName(), however, the ABAP types X (raw byte field) and
     * XSTRING (variable length byte field) are mapped to byte[], where the canonical name ("byte[]") differs
     * from the class name ("[B").
     *
     * @param field The JCoField.
     * @return The class representing the field type.
     */
    private Class<?> getAssociatedClass( JCoField field )
    {
        String canonicalClassName = field.getClassNameOfValue();

        if ( byte[].class.getCanonicalName().equals( canonicalClassName ) )
        {
            return byte[].class;
        }

        try
        {
            return Class.forName( canonicalClassName );
        }
        catch ( ClassNotFoundException e )
        {
            throw new InternalHiberSapException( "Can not get class for class name " + canonicalClassName, e );
        }
    }

    private void addFieldMappings( StructureMapping structureMapping, JCoRecord record )
    {
        JCoFieldIterator iter = record.getFieldIterator();

        while ( iter.hasNextField() )
        {
            FieldMapping fieldParam = ( FieldMapping ) getParameterMapping( iter.nextField() );
            structureMapping.addParameter( fieldParam );
        }
    }
}
