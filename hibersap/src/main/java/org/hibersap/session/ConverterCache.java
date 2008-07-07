package org.hibersap.session;

import java.util.HashMap;
import java.util.Map;

import org.hibersap.conversion.Converter;
import org.hibersap.mapping.ReflectionHelper;

public class ConverterCache
{
  private final Map<Class<? extends Converter>, Converter> converterForClass = new HashMap<Class<? extends Converter>, Converter>();

  public Converter getConverter(Class<? extends Converter> clazz)
  {
    if (clazz == null)
    {
      throw new IllegalArgumentException("null");
    }
    Converter converter = converterForClass.get(clazz);
    if (converter == null)
    {
      converter = (Converter) ReflectionHelper.newInstance(clazz);
      converterForClass.put(clazz, converter);
    }
    return converter;
  }
}
