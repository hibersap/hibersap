package org.hibersap.mapping.model;

import java.util.HashSet;
import java.util.Set;

public class StructureMapping extends ObjectMapping
{
  private final Set<FieldMapping> parameters = new HashSet<FieldMapping>();

  public StructureMapping(Class<?> associatedClass, String sapName, String javaName)
  {
    super(associatedClass, sapName, javaName);
  }

  public void addParameter(FieldMapping fieldParam)
  {
    parameters.add(fieldParam);
  }

  public Set<FieldMapping> getParameters()
  {
    return parameters;
  }

  @Override
  public ParamType getParamType()
  {
    return ParamType.STRUCTURE;
  }
}
