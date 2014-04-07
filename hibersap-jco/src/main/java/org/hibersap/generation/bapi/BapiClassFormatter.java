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

public class BapiClassFormatter
{
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

    private void checkPackagePath( String packagePath )
    {
        // TODO throw RuntimeException if invalid
    }

    public Map<String, String> createClasses( BapiMapping mapping, String packagePath )
    {
        checkPackagePath( packagePath );

        Map<String, String> result = new HashMap<String, String>();

        String bapiClassName = BapiFormatHelper.getCamelCaseBig( mapping.getBapiName() );
        String bapiClass = formatBapiClass( mapping, packagePath );
        result.put( bapiClassName, bapiClass );
        result.putAll( formatStructureClasses( mapping, packagePath ) );

        return result;
    }

    private Map<String, String> formatStructureClasses( BapiMapping mapping, String packagePath )
    {
        Map<String, String> result = new HashMap<String, String>();

        Set<ParameterMapping> params = new HashSet<ParameterMapping>();
        params.addAll( mapping.getImportParameters() );
        params.addAll( mapping.getExportParameters() );
        params.addAll( mapping.getTableParameters() );

        for ( ParameterMapping param : params )
        {
            if ( param.getParamType() != ParamType.FIELD )
            {
                String className = BapiFormatHelper.getCamelCaseBig( param.getSapName() );
                String structureClass = formatStructureClass( param, packagePath );
                result.put( className, structureClass );
            }
        }

        return result;
    }

    private String formatStructureClass( ParameterMapping param, String packagePath )
    {
        StructureMapping structure;
        if ( param instanceof StructureMapping )
        {
            structure = ( (StructureMapping) param );
        }
        else if ( param instanceof TableMapping )
        {
            structure = ( (TableMapping) param ).getComponentParameter();
        }
        else
        {
            throw new InternalHiberSapException(
                                                 "This exception should not occur, please report to Hibersap developers. Not supported: "
                                                     + param.getClass() );
        }
        String structureAnnotation = String.format( BAPI_STRUCTURE_ANNOTATION_FORMAT, BapiStructure.class.getName() );
        String className = BapiFormatHelper.getCamelCaseBig( param.getSapName() );
        String fieldsAndMethods = formatFieldsAndMethods( structure );
        return String.format( CLASS_FORMAT, packagePath, structureAnnotation, className, fieldsAndMethods );
    }

    private String formatBapiAnnotation( BapiMapping mapping )
    {
        return String.format( BAPI_ANNOTATION_FORMAT, Bapi.class.getName(), mapping.getBapiName() );
    }

    private String formatBapiClass( BapiMapping mapping, String packagePath )
    {
        return String.format( CLASS_FORMAT, packagePath, formatBapiAnnotation( mapping ), getBapiName( mapping ),
                              formatFieldsAndMethods( mapping ) );
    }

    private String formatField( String sapName, String javaName, String javaType, Class<?> typeAnnotation,
                                ParamType paramType )
    {
        String parameter = formatParameterAnnotation( sapName, paramType );
        if ( typeAnnotation != null )
        {
            String type = String.format( TYPE_ANNOTATION_FORMAT, typeAnnotation.getName() );
            return String.format( BAPI_FIELD_FORMAT, type, parameter, javaType, javaName );
        }
        else
        {
            return String.format( STRUCTURE_FIELD_FORMAT, parameter, javaType, javaName );
        }
    }

    private String formatFieldsAndMethods( BapiMapping bapiMapping )
    {
        StringBuilder fields = new StringBuilder();
        StringBuilder methods = new StringBuilder();

        formatFieldsAndMethods( bapiMapping.getImportParameters(), fields, methods, Import.class );
        formatFieldsAndMethods( bapiMapping.getExportParameters(), fields, methods, Export.class );
        formatFieldsAndMethods( bapiMapping.getTableParameters(), fields, methods, Table.class );

        return fields.toString() + methods.toString();
    }

    private String formatFieldsAndMethods( StructureMapping mapping )
    {
        StringBuilder fields = new StringBuilder();
        StringBuilder methods = new StringBuilder();

        formatFieldsAndMethods( mapping.getParameters(), fields, methods, null );

        return fields.toString() + methods.toString();
    }

    private void formatFieldsAndMethods( Set<? extends ParameterMapping> params, StringBuilder fields,
                                         StringBuilder methods, Class<?> annotation )
    {
        for ( ParameterMapping param : params )
        {
            String sapName = param.getSapName();
            String javaName = param.getJavaName();
            String javaType = getClassName( param );

            String field = formatField( sapName, javaName, javaType, annotation, param.getParamType() );
            fields.append( field );

            String getterAndSetter = formatMethods( sapName, javaName, javaType );
            methods.append( getterAndSetter );
        }
    }

    private String formatMethods( String sapName, String javaName, String javaType )
    {
        String getter = String.format( GETTER_FORMAT, javaType, BapiFormatHelper.getCamelCaseBig( sapName ), javaName );
        String setter = String.format( SETTER_FORMAT, BapiFormatHelper.getCamelCaseBig( sapName ), javaType, javaName,
                                       javaName, javaName );

        return getter + setter;
    }

    private String formatParameterAnnotation( String sapName, ParamType paramType )
    {
        if ( paramType == ParamType.STRUCTURE )
        {
            return String.format( PARAMETER_ANNOTATION_STRUCTURE_FORMAT, Parameter.class.getName(), sapName,
                                  ParameterType.class.getName(), ParameterType.STRUCTURE );
        }
        return String.format( PARAMETER_ANNOTATION_SIMPLE_FORMAT, Parameter.class.getName(), sapName );
    }

    private String getBapiName( BapiMapping mapping )
    {
        return BapiFormatHelper.getCamelCaseBig( mapping.getBapiName() );
    }

    private String getClassName( ParameterMapping param )
    {
        if ( param.getParamType() == ParamType.FIELD )
        {
            return param.getAssociatedType().getName();
        }

        String className = BapiFormatHelper.getCamelCaseBig( param.getSapName() );

        if ( param.getParamType() == ParamType.STRUCTURE )
        {
            return className;
        }

        return List.class.getName() + "<" + className + ">";
    }
}
