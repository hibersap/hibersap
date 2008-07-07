package org.hibersap.conversion;

import org.apache.commons.lang.StringUtils;

public class CharConverter implements Converter
{
  public Object convertToJava(Object sapValue) throws ConversionException
  {
    String valueStr = (String) sapValue;
    if (StringUtils.isEmpty(valueStr))
    {
      return ' ';
    }
    return valueStr.charAt(0);
  }

  public Object convertToSap(Object javaValue) throws ConversionException
  {
    Character valueChar = (Character) javaValue;
    return "" + valueChar;
  }
}
