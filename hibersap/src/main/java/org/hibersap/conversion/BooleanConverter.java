package org.hibersap.conversion;

/**
 * Converts between Java boolean and SAP CHAR type. A Java value of true will be
 * converted to "X", A Java value of false will be converted to "" and vice
 * versa.
 * 
 * @author cerker
 */
public class BooleanConverter implements Converter
{
  public Object convertToJava(Object sapValue) throws ConversionException
  {
    if (!String.class.isInstance(sapValue))
    {
      throw new ConversionException("Expected: " + String.class.getName() + " but was: "
          + sapValue.getClass().getName());
    }
    String value = (String) sapValue;
    if ("X".equalsIgnoreCase(value))
    {
      return Boolean.TRUE;
    }
    else
    {
      return Boolean.FALSE;
    }
  }

  public Object convertToSap(Object javaValue) throws ConversionException
  {
    if (!Boolean.class.isInstance(javaValue))
    {
      throw new ConversionException("Expected: " + Boolean.class.getName() + " but was: "
          + javaValue.getClass().getName());
    }
    boolean value = ((Boolean) javaValue).booleanValue();
    if (value)
    {
      return "X";
    }
    else
    {
      return "";
    }
  }

}
