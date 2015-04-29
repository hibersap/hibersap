/*
 * Copyright (c) 2008-2014 akquinet tech@spree GmbH
 *
 * This file is part of Hibersap.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
public class AnnotationBapiMapper {

    private static final Class<Bapi> BAPI = Bapi.class;
    private static final Class<Parameter> PARAMETER = Parameter.class;

    private void addParameterToBapiMapping( final BapiMapping bapiClass, final BapiField field ) {
        if ( field.isTable() ) {
            bapiClass.addTableParameter( createTableMapping( field ) );
        } else {
            ParameterMapping mapping = createParameterMapping(field);
            if ( field.isImport() ) {
                bapiClass.addImportParameter( mapping );
            } else {
                bapiClass.addExportParameter( mapping );
            }
        }
    }

	private ParameterMapping createParameterMapping(final BapiField field) {
		if ( field.isStructure() ) {
			return createStructureMapping( field );
		} else if (field.isTableStructure()) {
			// nested table, annotated with IMPORT or EXPORT
			return createTableMapping( field );
		} else {
			return createFieldMapping( field );
		}
	}

    private void assertIsBapiClass( final Class<?> clazz ) {
        if ( !clazz.isAnnotationPresent( BAPI ) ) {
            throw new MappingException( "Class " + clazz.getName() + " is not annotated with @Bapi" );
        }
    }

    private FieldMapping createFieldMapping( final BapiField field ) {
        return new FieldMapping( field.getType(), field.getSapName(), field.getName(), field.getConverter() );
    }

    private StructureMapping createStructureMapping( final BapiField structureField ) {
        Class<?> structureType = structureField.getAssociatedType();

        StructureMapping structureMapping = new StructureMapping( structureType, structureField.getSapName(),
                                                                  structureField.getName(), structureField.getConverter() );

        final Set<Field> fields = getDeclaredFieldsWithAnnotationRecursively( structureType, PARAMETER );
        for ( Field field : fields ) {
            ParameterMapping paramMapping = createParameterMapping( new BapiField( field ) );
            structureMapping.addParameter( paramMapping );
        }
        return structureMapping;
    }

    private TableMapping createTableMapping( final BapiField field ) {
        StructureMapping structureMapping = createStructureMapping( field );
        Class<?> associatedType = field.getAssociatedType();
        if ( associatedType == null ) {
            throw new MappingException( "The type of field " + field + " can not be detected." );
        }
        return new TableMapping( field.getType(), associatedType, field.getSapName(), field.getName(),
                                 structureMapping, field.getConverter() );
    }

    private ErrorHandling getErrorHandling( final Class<?> clazz ) {
        String pathToReturnStructure = null;
        String[] errorMessageTypes = null;
        if ( clazz.isAnnotationPresent( ThrowExceptionOnError.class ) ) {
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
    public BapiMapping mapBapi( final Class<?> clazz ) {
        assertIsBapiClass( clazz );

        Bapi bapiAnnotation = clazz.getAnnotation( BAPI );
        BapiMapping bapiMapping = new BapiMapping( clazz, bapiAnnotation.value(), getErrorHandling( clazz ) );

        Set<Field> fields = getDeclaredFieldsWithAnnotationRecursively( clazz, PARAMETER );
        for ( Field field : fields ) {
            addParameterToBapiMapping( bapiMapping, new BapiField( field ) );
        }
        return bapiMapping;
    }
}
