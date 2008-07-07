package org.hibersap.mapping.model;

public abstract class ObjectMapping extends ParameterMapping
{
  public ObjectMapping(Class<?> associatedClass, String sapName, String javaName)
  {
    super(associatedClass, sapName, javaName);
  }
}
