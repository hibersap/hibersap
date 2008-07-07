package org.hibersap.mapping.model;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibersap.MappingException;

public class BapiMapping
{
  private final String bapiName;

  private Class<?> associatedClass;

  private final Set<ObjectMapping> importParams = new HashSet<ObjectMapping>();

  private final Set<ObjectMapping> exportParams = new HashSet<ObjectMapping>();

  private final Set<TableMapping> tableParams = new HashSet<TableMapping>();

  private final ErrorHandling errorHandling;

  public BapiMapping(Class<?> associatedClass, String bapiName, ErrorHandling errorHandling)
  {
    if (StringUtils.isEmpty(bapiName))
    {
      throw new MappingException("Bapi name for class " + associatedClass.getName() + " is empty");
    }
    this.associatedClass = associatedClass;
    this.bapiName = bapiName;
    this.errorHandling = errorHandling;
  }

  public void addImportParameter(ObjectMapping parameter)
  {
    importParams.add(parameter);
  }

  public void addExportParameter(ObjectMapping parameter)
  {
    exportParams.add(parameter);
  }

  public void addTableParameter(TableMapping parameter)
  {
    tableParams.add(parameter);
  }

  public String getBapiName()
  {
    return bapiName;
  }

  public Class<?> getAssociatedClass()
  {
    return associatedClass;
  }

  public Set<ObjectMapping> getImportParameters()
  {
    return importParams;
  }

  public Set<ObjectMapping> getExportParameters()
  {
    return exportParams;
  }

  public Set<TableMapping> getTableParameters()
  {
    return tableParams;
  }

  public ErrorHandling getErrorHandling()
  {
    return this.errorHandling;
  }

  public static class ErrorHandling
  {
    private final boolean throwExceptionOnError;

    private final String pathToReturnStructure;

    private final String[] errorMessageTypes;

    public ErrorHandling(String pathToReturnStructure, String[] errorMessageTypes)
    {
      this.errorMessageTypes = errorMessageTypes;
      throwExceptionOnError = StringUtils.isNotEmpty(pathToReturnStructure);
      this.pathToReturnStructure = pathToReturnStructure;
    }

    public boolean isThrowExceptionOnError()
    {
      return this.throwExceptionOnError;
    }

    public String getPathToReturnStructure()
    {
      return this.pathToReturnStructure;
    }

    public String[] getErrorMessageTypes()
    {
      return this.errorMessageTypes;
    }
  }
}
