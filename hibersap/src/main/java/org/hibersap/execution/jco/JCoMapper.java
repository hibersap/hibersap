/**
 * 
 */
package org.hibersap.execution.jco;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibersap.HibersapException;

import com.sap.mw.jco.JCO.Field;
import com.sap.mw.jco.JCO.Function;
import com.sap.mw.jco.JCO.Record;
import com.sap.mw.jco.JCO.Table;

public class JCoMapper
{
  void putFunctionValuesToFunctionMap(Function function, Map<String, Object> map)
  {
    map.put("IMPORT", mapToMap(function.getImportParameterList()));
    map.put("EXPORT", mapToMap(function.getExportParameterList()));
    map.put("TABLE", mapToMap(function.getTableParameterList()));
  }

  private Map<String, Object> mapToMap(Record record)
  {
    Map<String, Object> map = new HashMap<String, Object>();
    if (record == null) return map;
    for (int i = 0; i < record.getNumFields(); i++)
    {
      Field jcoField = record.getField(i);
      String sapFieldName = jcoField.getName();

      if (jcoField.isStructure())
      {
        map.put(sapFieldName, mapToMap(jcoField.getStructure()));
      }
      else if (jcoField.isTable())
      {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Table table = jcoField.getTable();
        for (int j = 0; j < table.getNumRows(); j++)
        {
          table.setRow(j);
          list.add(mapToMap(table));
        }
        map.put(sapFieldName, list);
      }
      else
      {
        Object value = jcoField.getValue();
        map.put(sapFieldName, value);
      }
    }
    return map;
  }

  void putFunctionMapValuesToFunction(Function function, Map<String, Object> functionMap)
  {
    mapToJCo(function.getImportParameterList(), (Map<String, Object>) functionMap.get("IMPORT"));
    mapToJCo(function.getExportParameterList(), (Map<String, Object>) functionMap.get("EXPORT"));
    mapToJCo(function.getTableParameterList(), (Map<String, Object>) functionMap.get("TABLE"));
  }

  private void mapToJCo(Record record, Map<String, Object> map)
  {
    for (String fieldName : map.keySet())
    {
      Object value = map.get(fieldName);
      Field jcoField = record.getField(fieldName);
      if (jcoField.isStructure())
      {
        Map<String, Object> structureMap = (Map<String, Object>) value;
        mapToJCo(jcoField.getStructure(), structureMap);
      }
      else if (jcoField.isTable())
      {
        Collection<Map<String, Object>> tableMap = (Collection<Map<String, Object>>) value;
        Table table = jcoField.getTable();
        table.clear();
        for (Map<String, Object> structureMap : tableMap)
        {
          table.appendRow();
          mapToJCo(table, structureMap);
        }
      }
      else
      {
        checkTypes(value, jcoField);
        jcoField.setValue(value);
      }
    }
  }

  private void checkTypes(Object value, Field jcoField)
  {
    try
    {
      if (value != null
          && !Class.forName(jcoField.getClassNameOfValue()).isAssignableFrom(value.getClass()))
      {
        throw new HibersapException("JCo field " + jcoField.getName() + " has type "
            + jcoField.getClassNameOfValue() + " while value to set has type "
            + value.getClass().getName());
      }
    }
    catch (ClassNotFoundException e)
    {
      throw new HibersapException("Class check of JCo field failed, class "
          + jcoField.getClassNameOfValue() + " not found", e);
    }
  }
}