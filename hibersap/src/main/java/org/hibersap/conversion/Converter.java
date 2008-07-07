package org.hibersap.conversion;

public interface Converter
{
  Object convertToJava(Object sapValue) throws ConversionException;

  Object convertToSap(Object javaValue) throws ConversionException;
}
