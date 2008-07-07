package org.hibersap.mapping.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibersap.MappingException;

public class TableMapping extends ParameterMapping
{
  private final StructureMapping componentParameter;

  private final Class<?> fieldType;

  @SuppressWarnings("unchecked")
  private final Class<? extends Collection> collectionType;

  /**
   * @param fieldType
   *            The type of the field in the bean; may be a Collection interface
   *            like List, Set, Collection, a concrete class that implements
   *            Collection or an array.
   * @param associatedType
   *            The type of the elements, i.e. a Pojo class.
   * @param sapName
   *            The table's name in SAP.
   * @param javaName
   *            The Java field name of the Collection or array.
   * @param componentParameter
   *            A StructureMapping containing the table's fields.
   */
  public TableMapping(Class<?> fieldType, Class<?> associatedType, String sapName, String javaName,
      StructureMapping componentParameter)
  {
    super(associatedType, sapName, javaName);
    this.componentParameter = componentParameter;
    this.fieldType = fieldType;
    this.collectionType = determineCollectionType(fieldType);
  }

  public StructureMapping getComponentParameter()
  {
    return componentParameter;
  }

  public Class<?> getFieldType()
  {
    return this.fieldType;
  }

  @SuppressWarnings("unchecked")
  public Class<? extends Collection> getCollectionType()
  {
    return this.collectionType;
  }

  @Override
  public ParamType getParamType()
  {
    return ParamType.TABLE;
  }

  @SuppressWarnings("unchecked")
  private Class<? extends Collection> determineCollectionType(Class<?> type)
  {
    Class<? extends Collection> collectionType;

    if (Collection.class.isAssignableFrom(type))
    {
      if (type.isInterface())
      {
        if (List.class.equals(type))
        {
          collectionType = ArrayList.class;
        }
        else if (Set.class.equals(type))
        {
          collectionType = HashSet.class;
        }
        else if (Collection.class.equals(type))
        {
          collectionType = ArrayList.class;
        }
        else
        {
          throw new MappingException("Collection of type " + type.getName()
              + " not supported. See Field " + javaName);
        }
      }
      else
      {
        collectionType = (Class<? extends Collection>) type;
      }
    }
    else if (type.isArray())
    {
      collectionType = ArrayList.class;
    }
    else
    {
      throw new MappingException("The field " + javaName
          + " must be an array or an implementation of " + Collection.class.getName()
          + ", but is: " + fieldType.getName());
    }

    return collectionType;
  }
}
