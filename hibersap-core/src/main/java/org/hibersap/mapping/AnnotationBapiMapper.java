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

package org.hibersap.mapping;

import org.hibersap.MappingException;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ThrowExceptionOnError;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.mapping.model.ErrorHandling;
import org.hibersap.mapping.model.FieldMapping;
import org.hibersap.mapping.model.ParameterMapping;
import org.hibersap.mapping.model.StructureMapping;
import org.hibersap.mapping.model.TableMapping;

import java.lang.reflect.Field;
import java.util.Set;

import static org.hibersap.mapping.ReflectionHelper.getDeclaredFieldsWithAnnotationRecursively;

/**
 * Creates a BAPI Mapping for Annotated classes.
 *
 * @author Carsten Erker
 */
public class AnnotationBapiMapper
{
    private static final Class<Bapi> BAPI = Bapi.class;
    private static final Class<Parameter> PARAMETER = Parameter.class;

    private void addParameterToBapiMapping( BapiMapping bapiClass, BapiField field )
    {
        if ( field.isTable() )
        {
            bapiClass.addTableParameter( createTableMapping( field ) );
        }
        else
        {
            ParameterMapping mapping;
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

    private void assertIsBapiClass( Class<?> clazz )
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
                structureField.getName(), structureField.getConverter() );

        final Set<Field> fields = getDeclaredFieldsWithAnnotationRecursively( structureType, PARAMETER );
        for ( Field field : fields )
        {
            FieldMapping fieldMapping = createFieldMapping( new BapiField( field ) );
            structureMapping.addParameter( fieldMapping );
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
        return new TableMapping( field.getType(), associatedType, field.getSapName(), field.getName(),
                structureMapping, field.getConverter() );
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
        assertIsBapiClass( clazz );

        Bapi bapiAnnotation = clazz.getAnnotation( BAPI );
        BapiMapping bapiMapping = new BapiMapping( clazz, bapiAnnotation.value(), getErrorHandling( clazz ) );

        Set<Field> fields = getDeclaredFieldsWithAnnotationRecursively( clazz, PARAMETER );
        for ( Field field : fields )
        {
            addParameterToBapiMapping( bapiMapping, new BapiField( field ) );
        }
        return bapiMapping;
    }
}
