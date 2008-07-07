package org.hibersap.mapping;

import java.lang.reflect.Field;

import org.hibersap.MappingException;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.ThrowExceptionOnError;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.mapping.model.FieldMapping;
import org.hibersap.mapping.model.ObjectMapping;
import org.hibersap.mapping.model.StructureMapping;
import org.hibersap.mapping.model.TableMapping;
import org.hibersap.mapping.model.BapiMapping.ErrorHandling;

public class AnnotationBapiMapper
{
  private static final Class<Bapi> BAPI = Bapi.class;

  /**
   * Takes an annotated BAPI class and creates a BapiMapping. The BapiMapping is
   * used when a BAPI gets executed to map SAP parameters to fields of the BAPI
   * class.
   * 
   * @param clazz
   *            The annotated Bapi class.
   * @return The BapiMapping
   */
  public BapiMapping mapBapi(Class<?> clazz)
  {
    checkBapiClass(clazz);

    Bapi bapiAnnotation = clazz.getAnnotation(BAPI);
    BapiMapping bapi = new BapiMapping(clazz, bapiAnnotation.name(), getErrorHandling(clazz));
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields)
    {
      BapiField bapiField = new BapiField(field);
      if (bapiField.isParameter())
      {
        addParameterToBapiMapping(bapi, bapiField);
      }
    }
    return bapi;
  }

  private ErrorHandling getErrorHandling(Class<?> clazz)
  {
    String pathToReturnStructure = null;
    String[] errorMessageTypes = null;
    if (clazz.isAnnotationPresent(ThrowExceptionOnError.class))
    {
      ThrowExceptionOnError annotation = clazz.getAnnotation(ThrowExceptionOnError.class);
      pathToReturnStructure = annotation.returnStructure();
      errorMessageTypes = annotation.errorMessageTypes();
    }
    return new ErrorHandling(pathToReturnStructure, errorMessageTypes);
  }

  private void checkBapiClass(Class<?> clazz)
  {
    if (!clazz.isAnnotationPresent(BAPI))
    {
      throw new MappingException("Class " + clazz.getName() + " is not annotated with @Bapi");
    }
  }

  private void addParameterToBapiMapping(BapiMapping bapiClass, BapiField field)
  {
    if (field.isTable())
    {
      bapiClass.addTableParameter(createTableMapping(field));
    }
    else
    {
      ObjectMapping mapping;
      if (field.isStructure())
      {
        mapping = createStructureMapping(field);
      }
      else
      {
        mapping = createFieldMapping(field);
      }
      if (field.isImport())
      {
        bapiClass.addImportParameter(mapping);
      }
      else
      {
        bapiClass.addExportParameter(mapping);
      }
    }
  }

  private TableMapping createTableMapping(BapiField field)
  {
    StructureMapping structureMapping = createStructureMapping(field);
    Class<?> associatedType = field.getAssociatedType();
    if (associatedType == null)
    {
      throw new MappingException("The type of field " + field + " can not be detected.");
    }
    return new TableMapping(field.getType(), associatedType, field.getSapName(), field.getName(),
        structureMapping);
  }

  private FieldMapping createFieldMapping(BapiField field)
  {
    return new FieldMapping(field.getType(), field.getSapName(), field.getName(), field
        .getConverter());
  }

  private StructureMapping createStructureMapping(BapiField structureField)
  {
    Class<?> structureType = structureField.getAssociatedType();

    StructureMapping structureMapping = new StructureMapping(structureType, structureField
        .getSapName(), structureField.getName());

    Field[] fields = structureType.getDeclaredFields();
    for (Field field : fields)
    {
      BapiField bapiField = new BapiField(field);
      if (bapiField.isParameter())
      {
        FieldMapping fieldMapping = createFieldMapping(bapiField);
        structureMapping.addParameter(fieldMapping);
      }
    }
    return structureMapping;
  }
}
