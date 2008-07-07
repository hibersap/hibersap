package org.hibersap.execution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.conversion.Converter;
import org.hibersap.mapping.ReflectionHelper;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.mapping.model.FieldMapping;
import org.hibersap.mapping.model.ObjectMapping;
import org.hibersap.mapping.model.ParameterMapping;
import org.hibersap.mapping.model.StructureMapping;
import org.hibersap.mapping.model.TableMapping;
import org.hibersap.mapping.model.ParameterMapping.ParamType;
import org.hibersap.session.ConverterCache;

public class PojoMapper
{
  private static final Log LOG = LogFactory.getLog(PojoMapper.class);

  private final ConverterCache converterCache;

  public PojoMapper(ConverterCache converterCache)
  {
    this.converterCache = converterCache;
  }

  public Map<String, Object> mapPojoToFunctionMap(Object bapi, BapiMapping bapiMapping)
  {
    Map<String, Object> functionMap = new HashMap<String, Object>();

    Set<ObjectMapping> imports = bapiMapping.getImportParameters();
    functionMap.put("IMPORT", pojoToMap(bapi, imports));

    Set<ObjectMapping> exports = bapiMapping.getExportParameters();
    functionMap.put("EXPORT", pojoToMap(bapi, exports));

    Set<TableMapping> tables = bapiMapping.getTableParameters();
    functionMap.put("TABLE", pojoToMap(bapi, tables));

    return functionMap;
  }

  public void mapFunctionMapToPojo(Object bapi, Map<String, Object> functionMap,
      BapiMapping bapiMapping)
  {
    Map<String, Object> imports = (Map<String, Object>) functionMap.get("IMPORT");
    Set<ObjectMapping> importMappings = bapiMapping.getImportParameters();
    mapToPojo(bapi, imports, importMappings);

    Map<String, Object> exports = (Map<String, Object>) functionMap.get("EXPORT");
    Set<ObjectMapping> exportMappings = bapiMapping.getExportParameters();
    mapToPojo(bapi, exports, exportMappings);

    Map<String, Object> tables = (Map<String, Object>) functionMap.get("TABLE");
    Set<TableMapping> tableMappings = bapiMapping.getTableParameters();
    mapToPojo(bapi, tables, tableMappings);
  }

  private void mapToPojo(Object bean, Map<String, Object> map,
      Set<? extends ParameterMapping> mappings)
  {
    for (ParameterMapping paramMapping : mappings)
    {
      ParamType paramType = paramMapping.getParamType();
      String fieldNameJava = paramMapping.getJavaName();
      String fieldNameSap = paramMapping.getSapName();
      Object value = map.get(fieldNameSap);

      if (paramType == ParamType.FIELD)
      {
        FieldMapping fieldMapping = (FieldMapping) paramMapping;
        Converter converter = converterCache.getConverter(fieldMapping.getConverter());
        Object convertedValue = converter.convertToJava(value);
        ReflectionHelper.setFieldValue(bean, fieldNameJava, convertedValue);
      }
      else if (paramType == ParamType.STRUCTURE)
      {
        Set<FieldMapping> fieldMappings = ((StructureMapping) paramMapping).getParameters();
        Map<String, Object> subMap = (Map<String, Object>) value;
        Object subBean = ReflectionHelper.newInstance(paramMapping.getAssociatedType());
        mapToPojo(subBean, subMap, fieldMappings);
        ReflectionHelper.setFieldValue(bean, fieldNameJava, subBean);
      }
      else
      {
        TableMapping tableMapping = (TableMapping) paramMapping;
        Collection<Object> collection = ReflectionHelper.newCollectionInstance(tableMapping
            .getCollectionType());
        ReflectionHelper.setFieldValue(bean, fieldNameJava, collection);

        Collection<Map<String, Object>> rows = (Collection<Map<String, Object>>) value;

        for (Map<String, Object> tableMap : rows)
        {
          Object elementBean = ReflectionHelper.newInstance(tableMapping.getAssociatedType());
          mapToPojo(elementBean, tableMap, tableMapping.getComponentParameter().getParameters());
          collection.add(elementBean);
        }

        if (tableMapping.getFieldType().isArray())
        {
          ReflectionHelper.setFieldValue(bean, fieldNameJava, collection.toArray());
        }
        else
        {
          ReflectionHelper.setFieldValue(bean, fieldNameJava, collection);
        }
      }
    }
  }

  private Map<String, Object> pojoToMap(Object bean, Set<? extends ParameterMapping> mappings)
  {
    Map<String, Object> map = new HashMap<String, Object>();

    for (ParameterMapping paramMapping : mappings)
    {
      String fieldNameJava = paramMapping.getJavaName();
      Object value = ReflectionHelper.getFieldValue(bean, fieldNameJava);

      if (value != null)
      {
        ParamType paramType = paramMapping.getParamType();
        String fieldNameSap = paramMapping.getSapName();

        if (paramType == ParamType.FIELD)
        {
          FieldMapping fieldMapping = (FieldMapping) paramMapping;
          Converter converter = converterCache.getConverter(fieldMapping.getConverter());
          map.put(fieldNameSap, converter.convertToSap(value));
        }
        else if (paramType == ParamType.STRUCTURE)
        {
          Set<FieldMapping> fieldMappings = ((StructureMapping) paramMapping).getParameters();
          map.put(fieldNameSap, pojoToMap(value, fieldMappings));
        }
        else
        {
          List<Map<String, Object>> valueMaps = new ArrayList<Map<String, Object>>();
          Set<FieldMapping> fieldMappings = ((TableMapping) paramMapping).getComponentParameter()
              .getParameters();

          // TODO value my be null, esp. when table acts as import parameter
          Collection<Object> beans = (Collection<Object>) value;
          for (Object object : beans)
          {
            valueMaps.add(pojoToMap(object, fieldMappings));
          }
          map.put(fieldNameSap, valueMaps);
        }
      }
    }
    return map;
  }
}
