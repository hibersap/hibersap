package org.hibersap.execution.neo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.HibersapException;
import org.hibersap.bapi.BapiConstants;
import org.hibersap.execution.UnsafeCastHelper;

import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFieldIterator;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRecord;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;

@SuppressWarnings("PackageAccessibility")

/**
 * Mapper for  SAP Cloud Plattform Runtime Neo. Since Table and Changing parameter are not suitable
 * They will be ignored
 *
 *  @author Ludger Pottmeier
 */
public class NeoJCoMapper {

    private static final Log LOG = LogFactory.getLog(NeoJCoMapper.class);

    void putFunctionMapValuesToFunction(final JCoFunction function, final Map<String, Object> functionMap) {
        final Map<String, Object> importMap = UnsafeCastHelper.castToMap(functionMap.get(BapiConstants.IMPORT));
        mapToJCo(function.getImportParameterList(), importMap);
//        final Map<String, Object> exportMap = UnsafeCastHelper.castToMap(functionMap.get(BapiConstants.EXPORT));
//        mapToJCo(function.getExportParameterList(), exportMap);
//        final Map<String, Object> changingMap = UnsafeCastHelper.castToMap(functionMap.get("CHANGING"));
//        mapToJCo(function.getChangingParameterList(), changingMap);
//        final Map<String, Object> tableMap = UnsafeCastHelper.castToMap(functionMap.get(BapiConstants.TABLE));
//        mapToJCo(function.getTableParameterList(), tableMap);
    }

    void putFunctionValuesToFunctionMap(final JCoFunction function, final Map<String, Object> map) {
//        map.put(BapiConstants.IMPORT, mapToMap(function.getImportParameterList()));
        map.put(BapiConstants.EXPORT, mapToMap(function.getExportParameterList()));
//        map.put("CHANGING", mapToMap(function.getChangingParameterList()));
//        map.put(BapiConstants.TABLE, mapToMap(function.getTableParameterList()));
    }

    private void checkTypes(final Object value, final String classNameOfBapiField, final String fieldName) {
        try {
            if (value != null && !Class.forName(classNameOfBapiField).isAssignableFrom(value.getClass())) {
                throw new HibersapException("JCo field " + fieldName + " has type " + classNameOfBapiField
                        + " while value to set has type " + value.getClass().getName());
            }
        } catch (final ClassNotFoundException e) {
            // TODO classNameOfBapiField: JCoRecord.getClassNameOfValue() returns the canonical name
            // which differs from the class name we can call with Class.forName() in some data types,
            // e.g. byte[]. Since there is no standard way of converting it, we suppress the Exception
            // when the class is not found to be able to work with byte arrays. byte[] currently (as
            // of JCo versionb 3.0.8) seems to be the only type JCo returns which would not work.
            // Questions: Does it make sense at all to do this check? If yes, how can this be done in
            // a nice way?

//            throw new HibersapException( "Class check of JCo field failed, class " + classNameOfBapiField
//                    + " not found", e );

            if (!classNameOfBapiField.equals(byte[].class.getCanonicalName())) {
                LOG.warn("Class check of JCo field failed, class " + classNameOfBapiField + " not found");
            }
        }
    }

    private void mapToJCo(final JCoRecord record, final Map<String, Object> map) {
        for (final String fieldName : map.keySet()) {
            final Object value = map.get(fieldName);

            if (Map.class.isAssignableFrom(value.getClass())) {
                final Map<String, Object> structureMap = UnsafeCastHelper.castToMap(value);
                final JCoStructure structure = record.getStructure(fieldName);
                mapToJCo(structure, structureMap);
            } else if (Collection.class.isAssignableFrom(value.getClass())) {
                final Collection<Map<String, Object>> tableMap = UnsafeCastHelper.castToCollectionOfMaps(value);
                final JCoTable table = record.getTable(fieldName);
                table.clear();
                for (final Map<String, Object> structureMap : tableMap) {
                    table.appendRow();
                    mapToJCo(table, structureMap);
                }
            } else {
                checkTypes(value, record.getClassNameOfValue(fieldName), fieldName);
                record.setValue(fieldName, value);
            }
        }
    }

    private Map<String, Object> mapToMap(final JCoRecord record) {
        final Map<String, Object> map = new HashMap<String, Object>();
        if (record == null) {
            return map;
        }

        final JCoFieldIterator iter = record.getFieldIterator();

        while (iter.hasNextField()) {
            final JCoField jcoField = iter.nextField();

            final String sapFieldName = jcoField.getName();

            if (jcoField.isStructure()) {
                map.put(sapFieldName, mapToMap(jcoField.getStructure()));
            } else if (jcoField.isTable()) {
                final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                final JCoTable table = jcoField.getTable();
                for (int j = 0; j < table.getNumRows(); j++) {
                    table.setRow(j);
                    list.add(mapToMap(table));
                }
                map.put(sapFieldName, list);
            } else {
                final Object value = jcoField.getValue();
                map.put(sapFieldName, value);
            }
        }
        return map;
    }
}
