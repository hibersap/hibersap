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

package org.hibersap.generation.bapi;

import org.hibersap.InternalHiberSapException;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.annotations.Table;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.mapping.model.ParameterMapping;
import org.hibersap.mapping.model.ParameterMapping.ParamType;
import org.hibersap.mapping.model.StructureMapping;
import org.hibersap.mapping.model.TableMapping;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BapiClassFormatter {

    private static final String CLASS_FORMAT = "package %s;%n%n%s%npublic class %s%n{%n%s}";

    private static final String BAPI_FIELD_FORMAT = "\t%s%n\t%s%n\tprivate %s %s;%n%n";

    private static final String STRUCTURE_FIELD_FORMAT = "\t%s%n\tprivate %s %s;%n%n";

    private static final String GETTER_FORMAT = "\tpublic %s get%s()%n\t{%n\t\treturn %s;%n\t}%n%n";

    private static final String SETTER_FORMAT = "\tpublic void set%s(%s %s)%n\t{%n\t\tthis.%s = %s;%n\t}%n%n";

    private static final String PARAMETER_ANNOTATION_SIMPLE_FORMAT = "@%s(\"%s\")";

    private static final String PARAMETER_ANNOTATION_STRUCTURE_FORMAT = "@%s(value = \"%s\", type = %s.%s)";

    private static final String TYPE_ANNOTATION_FORMAT = "@%s";

    private static final String BAPI_ANNOTATION_FORMAT = "@%s(\"%s\")";

    private static final String BAPI_STRUCTURE_ANNOTATION_FORMAT = "@%s";

    private void checkPackagePath( final String packagePath ) {
        // TODO throw RuntimeException if invalid
    }

    public Map<String, String> createClasses( final BapiMapping mapping, final String packagePath ) {
        checkPackagePath( packagePath );

        Map<String, String> result = new HashMap<String, String>();

        String bapiClassName = BapiFormatHelper.getCamelCaseBig( mapping.getBapiName() );
        String bapiClass = formatBapiClass( mapping, packagePath );
        result.put( bapiClassName, bapiClass );
        result.putAll( formatStructureClasses( mapping, packagePath ) );

        return result;
    }

    private Map<String, String> formatStructureClasses( final BapiMapping mapping, final String packagePath ) {
        Map<String, String> result = new HashMap<String, String>();

        Set<ParameterMapping> params = new HashSet<ParameterMapping>();
        params.addAll( mapping.getImportParameters() );
        params.addAll( mapping.getExportParameters() );
        params.addAll( mapping.getTableParameters() );

        for ( ParameterMapping param : params ) {
            if ( param.getParamType() != ParamType.FIELD ) {
                String className = BapiFormatHelper.getCamelCaseBig( param.getSapName() );
                String structureClass = formatStructureClass( param, packagePath );
                result.put( className, structureClass );
            }
        }

        return result;
    }

    private String formatStructureClass( final ParameterMapping param, final String packagePath ) {
        StructureMapping structure;
        if ( param instanceof StructureMapping ) {
            structure = ( (StructureMapping) param );
        } else if ( param instanceof TableMapping ) {
            structure = ( (TableMapping) param ).getComponentParameter();
        } else {
            throw new InternalHiberSapException(
                    "This exception should not occur, please report to Hibersap developers. Not supported: "
                            + param.getClass()
            );
        }
        String structureAnnotation = String.format( BAPI_STRUCTURE_ANNOTATION_FORMAT, BapiStructure.class.getName() );
        String className = BapiFormatHelper.getCamelCaseBig( param.getSapName() );
        String fieldsAndMethods = formatFieldsAndMethods( structure );
        return String.format( CLASS_FORMAT, packagePath, structureAnnotation, className, fieldsAndMethods );
    }

    private String formatBapiAnnotation( final BapiMapping mapping ) {
        return String.format( BAPI_ANNOTATION_FORMAT, Bapi.class.getName(), mapping.getBapiName() );
    }

    private String formatBapiClass( final BapiMapping mapping, final String packagePath ) {
        return String.format( CLASS_FORMAT, packagePath, formatBapiAnnotation( mapping ), getBapiName( mapping ),
                              formatFieldsAndMethods( mapping ) );
    }

    private String formatField( final String sapName, final String javaName, final String javaType, final Class<?> typeAnnotation, final ParamType paramType ) {
        String parameter = formatParameterAnnotation( sapName, paramType );
        if ( typeAnnotation != null ) {
            String type = String.format( TYPE_ANNOTATION_FORMAT, typeAnnotation.getName() );
            return String.format( BAPI_FIELD_FORMAT, type, parameter, javaType, javaName );
        } else {
            return String.format( STRUCTURE_FIELD_FORMAT, parameter, javaType, javaName );
        }
    }

    private String formatFieldsAndMethods( final BapiMapping bapiMapping ) {
        StringBuilder fields = new StringBuilder();
        StringBuilder methods = new StringBuilder();

        formatFieldsAndMethods( bapiMapping.getImportParameters(), fields, methods, Import.class );
        formatFieldsAndMethods( bapiMapping.getExportParameters(), fields, methods, Export.class );
        formatFieldsAndMethods( bapiMapping.getTableParameters(), fields, methods, Table.class );

        return fields.toString() + methods.toString();
    }

    private String formatFieldsAndMethods( final StructureMapping mapping ) {
        StringBuilder fields = new StringBuilder();
        StringBuilder methods = new StringBuilder();

        formatFieldsAndMethods( mapping.getParameters(), fields, methods, null );

        return fields.toString() + methods.toString();
    }

    private void formatFieldsAndMethods( final Set<? extends ParameterMapping> params,
                                         final StringBuilder fields,
                                         final StringBuilder methods,
                                         final Class<?> annotation ) {
        for ( ParameterMapping param : params ) {
            String sapName = param.getSapName();
            String javaName = param.getJavaName();
            String javaType = getClassName( param );

            String field = formatField( sapName, javaName, javaType, annotation, param.getParamType() );
            fields.append( field );

            String getterAndSetter = formatMethods( sapName, javaName, javaType );
            methods.append( getterAndSetter );
        }
    }

    private String formatMethods( final String sapName, final String javaName, final String javaType ) {
        String getter = String.format( GETTER_FORMAT, javaType, BapiFormatHelper.getCamelCaseBig( sapName ), javaName );
        String setter = String.format( SETTER_FORMAT, BapiFormatHelper.getCamelCaseBig( sapName ), javaType, javaName,
                                       javaName, javaName );
        return getter + setter;
    }

    private String formatParameterAnnotation( final String sapName, final ParamType paramType ) {
        if ( paramType == ParamType.STRUCTURE ) {
            return String.format( PARAMETER_ANNOTATION_STRUCTURE_FORMAT, Parameter.class.getName(), sapName,
                                  ParameterType.class.getName(), ParameterType.STRUCTURE );
        }
        return String.format( PARAMETER_ANNOTATION_SIMPLE_FORMAT, Parameter.class.getName(), sapName );
    }

    private String getBapiName( final BapiMapping mapping ) {
        return BapiFormatHelper.getCamelCaseBig( mapping.getBapiName() );
    }

    private String getClassName( final ParameterMapping param ) {
        if ( param.getParamType() == ParamType.FIELD ) {
            return param.getAssociatedType().getName();
        }

        String className = BapiFormatHelper.getCamelCaseBig( param.getSapName() );

        if ( param.getParamType() == ParamType.STRUCTURE ) {
            return className;
        }

        return List.class.getName() + "<" + className + ">";
    }
}
