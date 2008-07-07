package org.hibersap.mapping.model;

import org.hibersap.conversion.Converter;

public class FieldMapping extends ObjectMapping
{
  private final Class<? extends Converter> converter;

  public FieldMapping(Class<?> associatedClass, String sapName, String javaName,
      Class<? extends Converter> converter)
  {
    super(associatedClass, sapName, javaName);
    this.converter = converter;
  }

  public Class<? extends Converter> getConverter()
  {
    return this.converter;
  }

  @Override
  public ParamType getParamType()
  {
    return ParamType.FIELD;
  }
}
