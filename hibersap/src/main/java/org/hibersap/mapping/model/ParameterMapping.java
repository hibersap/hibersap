package org.hibersap.mapping.model;

public abstract class ParameterMapping
{
  public enum ParamType
  {
    FIELD, STRUCTURE, TABLE
  };

  protected final Class<?> associatedType;

  protected final String sapName;

  protected final String javaName;

  public ParameterMapping(Class<?> associatedType, String sapName, String javaName)
  {
    this.associatedType = associatedType;
    this.sapName = sapName;
    this.javaName = javaName;
  }

  public abstract ParamType getParamType();

  public Class<?> getAssociatedType()
  {
    return associatedType;
  }

  public String getJavaName()
  {
    return this.javaName;
  }

  public String getSapName()
  {
    return sapName;
  }
}