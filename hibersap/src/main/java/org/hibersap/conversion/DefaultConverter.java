package org.hibersap.conversion;

public class DefaultConverter implements Converter
{
  public Object convertToJava(Object sapValue) throws ConversionException
  {
    return sapValue;
  }

  public Object convertToSap(Object javaValue) throws ConversionException
  {
    return javaValue;
  }
}
