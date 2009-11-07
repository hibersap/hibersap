package org.hibersap.mapping;

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

import org.hibersap.MappingException;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.ThrowExceptionOnError;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.mapping.model.ErrorHandling;
import org.hibersap.mapping.model.FieldMapping;
import org.hibersap.mapping.model.ObjectMapping;
import org.hibersap.mapping.model.StructureMapping;
import org.hibersap.mapping.model.TableMapping;

import java.lang.reflect.Field;

/**
 * Creates a BAPI Mapping for Annotated classes.
 * 
 * @author Carsten Erker
 */
public class AnnotationBapiMapper
{
    private static final Class<Bapi> BAPI = Bapi.class;

    private void addParameterToBapiMapping( BapiMapping bapiClass, BapiField field )
    {
        if ( field.isTable() )
        {
            bapiClass.addTableParameter( createTableMapping( field ) );
        }
        else
        {
            ObjectMapping mapping;
            if ( field.isStructure() )
            {
                mapping = createStructureMapping( field );
            }
            else
            {
                mapping = createFieldMapping( field );
            }
            if ( field.isImport() )
            {
                bapiClass.addImportParameter( mapping );
            }
            else
            {
                bapiClass.addExportParameter( mapping );
            }
        }
    }

    private void checkBapiClass( Class<?> clazz )
    {
        if ( !clazz.isAnnotationPresent( BAPI ) )
        {
            throw new MappingException( "Class " + clazz.getName() + " is not annotated with @Bapi" );
        }
    }

    private FieldMapping createFieldMapping( BapiField field )
    {
        return new FieldMapping( field.getType(), field.getSapName(), field.getName(), field.getConverter() );
    }

    private StructureMapping createStructureMapping( BapiField structureField )
    {
        Class<?> structureType = structureField.getAssociatedType();

        StructureMapping structureMapping = new StructureMapping( structureType, structureField.getSapName(),
                                                                  structureField.getName() );

        Field[] fields = structureType.getDeclaredFields();
        for ( Field field : fields )
        {
            BapiField bapiField = new BapiField( field );
            if ( bapiField.isParameter() )
            {
                FieldMapping fieldMapping = createFieldMapping( bapiField );
                structureMapping.addParameter( fieldMapping );
            }
        }
        return structureMapping;
    }

    private TableMapping createTableMapping( BapiField field )
    {
        StructureMapping structureMapping = createStructureMapping( field );
        Class<?> associatedType = field.getAssociatedType();
        if ( associatedType == null )
        {
            throw new MappingException( "The type of field " + field + " can not be detected." );
        }
        return new TableMapping( field.getType(), associatedType, field.getSapName(), field.getName(), structureMapping );
    }

    private ErrorHandling getErrorHandling( Class<?> clazz )
    {
        String pathToReturnStructure = null;
        String[] errorMessageTypes = null;
        if ( clazz.isAnnotationPresent( ThrowExceptionOnError.class ) )
        {
            ThrowExceptionOnError annotation = clazz.getAnnotation( ThrowExceptionOnError.class );
            pathToReturnStructure = annotation.returnStructure();
            errorMessageTypes = annotation.errorMessageTypes();
        }
        return new ErrorHandling( pathToReturnStructure, errorMessageTypes );
    }

    /**
     * Takes an annotated BAPI class and creates a BapiMapping. The BapiMapping is used when a BAPI
     * gets executed to map SAP parameters to fields of the BAPI class.
     * 
     * @param clazz The annotated Bapi class.
     * @return The BapiMapping
     */
    public BapiMapping mapBapi( Class<?> clazz )
    {
        checkBapiClass( clazz );

        Bapi bapiAnnotation = clazz.getAnnotation( BAPI );
        BapiMapping bapi = new BapiMapping( clazz, bapiAnnotation.value(), getErrorHandling( clazz ) );
        Field[] fields = clazz.getDeclaredFields();
        for ( Field field : fields )
        {
            BapiField bapiField = new BapiField( field );
            if ( bapiField.isParameter() )
            {
                addParameterToBapiMapping( bapi, bapiField );
            }
        }
        return bapi;
    }
}
