package org.hibersap.mapping;

import java.lang.reflect.Field;

import org.hibersap.annotations.Convert;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.annotations.Table;
import org.hibersap.conversion.Converter;
import org.hibersap.conversion.DefaultConverter;

class BapiField
{
  private static final Class<Parameter> PARAM = Parameter.class;

  private static final Class<Import> IMPORT = Import.class;

  private static final Class<Export> EXPORT = Export.class;

  private static final Class<Table> TABLE = Table.class;

  private static final Class<Convert> CONVERT = Convert.class;

  private final Field field;

  public BapiField(Field field)
  {
    this.field = field;
  }

  public Class<?> getArrayType()
  {
    Class<?> associatedType = field.getType();
    return ReflectionHelper.getArrayType(associatedType);
  }

  public Class<?> getAssociatedType()
  {
    Class<?> type;
    if (isTable())
    {
      type = getArrayType();
      if (type == null)
      {
        type = getGenericType();
      }
    }
    else
    {
      type = getType();
    }
    return type;
  }

  public Class<? extends Converter> getConverter()
  {
    if (field.isAnnotationPresent(CONVERT))
    {
      Convert convert = field.getAnnotation(CONVERT);
      return convert.converter();
    }
    else
    {
      return DefaultConverter.class;
    }
  }

  public Class<?> getGenericType()
  {
    return ReflectionHelper.getGenericType(field);
  }

  public String getName()
  {
    return field.getName();
  }

  public String getSapName()
  {
    return getParameterAnnotation().name();
  }

  public Class<?> getType()
  {
    return field.getType();
  }

  public boolean isExport()
  {
    return field.isAnnotationPresent(EXPORT);
  }

  public boolean isImport()
  {
    return field.isAnnotationPresent(IMPORT);
  }

  public boolean isParameter()
  {
    return field.isAnnotationPresent(PARAM);
  }

  public boolean isStructure()
  {
    boolean result = false;
    if (isParameter())
    {
      result = getParameterAnnotation().type() == ParameterType.STRUCTURE;
    }
    return result;
  }

  public boolean isTable()
  {
    return field.isAnnotationPresent(TABLE);
  }

  private Parameter getParameterAnnotation()
  {
    return field.getAnnotation(PARAM);
  }
}
