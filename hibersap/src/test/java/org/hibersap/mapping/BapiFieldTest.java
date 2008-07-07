package org.hibersap.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibersap.annotations.Table;
import org.junit.Test;

public class BapiFieldTest
{
  @Table
  @SuppressWarnings("unused")
  private List<String> listGeneric;

  @Table
  @SuppressWarnings("unused")
  private Integer[] integerArray;

  @SuppressWarnings("unused")
  private Collection<?> list;

  @SuppressWarnings("unused")
  private Map<Integer, String> mapGeneric;

  @SuppressWarnings("unused")
  private int[] intArray;

  @SuppressWarnings("unused")
  private int intPrimitive;

  @SuppressWarnings(
  { "unchecked", "unused" })
  private ArrayList listSimple;

  @Test
  public void getActualType() throws NoSuchFieldException
  {
    BapiField field = new BapiField(getClass().getDeclaredField("listGeneric"));
    assertEquals(String.class, field.getAssociatedType());

    field = new BapiField(getClass().getDeclaredField("integerArray"));
    assertEquals(Integer.class, field.getAssociatedType());

    field = new BapiField(getClass().getDeclaredField("intPrimitive"));
    assertEquals(int.class, field.getAssociatedType());
  }

  @Test
  public void getGenericType() throws NoSuchFieldException
  {
    BapiField field = new BapiField(getClass().getDeclaredField("listGeneric"));
    assertEquals(String.class, field.getGenericType());

    field = new BapiField(getClass().getDeclaredField("list"));
    assertNull(field.getGenericType());

    field = new BapiField(getClass().getDeclaredField("listSimple"));
    assertNull(field.getGenericType());

    field = new BapiField(getClass().getDeclaredField("mapGeneric"));
    assertNull(field.getGenericType());

    field = new BapiField(getClass().getDeclaredField("integerArray"));
    assertEquals(null, field.getGenericType());
  }

  @Test
  public void getArrayType() throws NoSuchFieldException
  {
    BapiField fieldIntegerArray = new BapiField(getClass().getDeclaredField("integerArray"));
    assertEquals(Integer.class, fieldIntegerArray.getArrayType());

    BapiField fieldIntArray = new BapiField(getClass().getDeclaredField("intArray"));
    assertEquals(int.class, fieldIntArray.getArrayType());
  }
}
