package org.hibersap.generation.bapi;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.hibersap.mapping.model.StructureMapping;
import org.hibersap.mapping.model.TableMapping;
import org.hibersap.mapping.model.ParameterMapping.ParamType;

public class BapiClassFormatter
{
    private static final String classFormat = "package %s;%n%n%s%npublic class %s%n{%n%s}";

    private static final String bapiFieldFormat = "\t%s%n\t%s%n\tprivate %s %s;%n%n";

    private static final String structureFieldFormat = "\t%s%n\tprivate %s %s;%n%n";

    private static final String getterFormat = "\tpublic %s get%s()%n\t{%n\t\treturn %s;%n\t}%n%n";

    private static final String setterFormat = "\tpublic void set%s(%s %s)%n\t{%n\t\tthis.%s = %s;%n\t}%n%n";

    private static final String parameterAnnotationSimpleFormat = "@%s(\"%s\")";

    private static final String parameterAnnotationStructureFormat = "@%s(value = \"%s\", type = %s.%s)";

    private static final String typeAnnotationFormat = "@%s";

    private static final String bapiAnnotationFormat = "@%s(\"%s\")";

    private static final String bapiStructureAnnotationFormat = "@%s";

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
        String structureAnnotation = String.format( bapiStructureAnnotationFormat, BapiStructure.class.getName() );
        String className = BapiFormatHelper.getCamelCaseBig( param.getSapName() );
        String fieldsAndMethods = formatFieldsAndMethods( structure );
        return String.format( classFormat, packagePath, structureAnnotation, className, fieldsAndMethods );
    }

    private String formatBapiAnnotation( BapiMapping mapping )
    {
        return String.format( bapiAnnotationFormat, Bapi.class.getName(), mapping.getBapiName() );
    }

    private String formatBapiClass( BapiMapping mapping, String packagePath )
    {
        return String.format( classFormat, packagePath, formatBapiAnnotation( mapping ), getBapiName( mapping ),
                              formatFieldsAndMethods( mapping ) );
    }

    private String formatField( String sapName, String javaName, String javaType, Class<?> typeAnnotation,
                                ParamType paramType )
    {
        String parameter = formatParameterAnnotation( sapName, paramType );
        if ( typeAnnotation != null )
        {
            String type = String.format( typeAnnotationFormat, typeAnnotation.getName() );
            String field = String.format( bapiFieldFormat, type, parameter, javaType, javaName );
            return field;
        }
        else
        {
            String field = String.format( structureFieldFormat, parameter, javaType, javaName );            
            return field;
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
        String getter = String.format( getterFormat, javaType, BapiFormatHelper.getCamelCaseBig( sapName ), javaName );
        String setter = String.format( setterFormat, BapiFormatHelper.getCamelCaseBig( sapName ), javaType, javaName,
                                       javaName, javaName );

        return getter + setter;
    }

    private String formatParameterAnnotation( String sapName, ParamType paramType )
    {
        if ( paramType == ParamType.STRUCTURE )
        {
            return String.format( parameterAnnotationStructureFormat, Parameter.class.getName(), sapName,
                                  ParameterType.class.getName(), ParameterType.STRUCTURE );
        }
        return String.format( parameterAnnotationSimpleFormat, Parameter.class.getName(), sapName );
    }

    private String getBapiName( BapiMapping mapping )
    {
        return BapiFormatHelper.getCamelCaseBig( mapping.getBapiName() );
    }

    private String getClassName( ParameterMapping param )
    {
        if ( param.getParamType() == ParamType.FIELD )
            return param.getAssociatedType().getName();

        String className = BapiFormatHelper.getCamelCaseBig( param.getSapName() );

        if ( param.getParamType() == ParamType.STRUCTURE )
            return className;

        return List.class.getName() + "<" + className + ">";
    }
}
