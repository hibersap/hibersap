package org.hibersap.generation.bapi;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibersap.HibersapException;
import org.hibersap.InternalHiberSapException;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.mapping.model.FieldMapping;
import org.hibersap.mapping.model.ParameterMapping;
import org.hibersap.mapping.model.StructureMapping;
import org.hibersap.mapping.model.TableMapping;
import org.hibersap.session.SessionManager;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFieldIterator;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoFunctionTemplate;
import com.sap.conn.jco.JCoParameterFieldIterator;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoRecord;

public class ReverseBapiMapper
{
    public BapiMapping map( String bapiName, SessionManager sessionManager )
    {
        JCoDestination destination;
        try
        {
            String sfName = sessionManager.getConfig().getName();
            destination = JCoDestinationManager.getDestination( sfName );

            JCoFunctionTemplate ft = destination.getRepository().getFunctionTemplate( bapiName );
            JCoFunction function = ft.getFunction();

            BapiMapping mapping = new BapiMapping( null, function.getName(), null );

            mapFields( mapping.getImportParameters(), function.getImportParameterList() );
            mapFields( mapping.getExportParameters(), function.getExportParameterList() );
            mapFields( mapping.getTableParameters(), function.getTableParameterList() );

            return mapping;
        }
        catch ( JCoException e )
        {
            throw new HibersapException( e );
        }
    }

    @SuppressWarnings("unchecked")
    private void mapFields( Set<? extends ParameterMapping> set, JCoParameterList jcoParams )
    {
        JCoParameterFieldIterator iter = jcoParams.getParameterFieldIterator();
        while ( iter.hasNextField() )
        {
            JCoField field = iter.nextField();
            ParameterMapping param = getParameterMapping( field );

            if ( ParameterMapping.ParamType.FIELD == param.getParamType() )
            {
                ( (Set<FieldMapping>) set ).add( (FieldMapping) param );
            }
            else if ( ParameterMapping.ParamType.STRUCTURE == param.getParamType() )
            {
                ( (Set<StructureMapping>) set ).add( (StructureMapping) param );
            }
            else if ( ParameterMapping.ParamType.TABLE == param.getParamType() )
            {
                ( (Set<TableMapping>) set ).add( (TableMapping) param );
            }
        }
    }

    private ParameterMapping getParameterMapping( JCoField field )
    {
        String javaFieldName = BapiFormatHelper.getCamelCaseSmall( field.getName() );

        if ( field.isStructure() )
        {
            return new StructureMapping( null, field.getName(), javaFieldName, getFieldMappings( field.getStructure() ) );
        }
        if ( field.isTable() )
        {
            StructureMapping structureMapping = new StructureMapping( null, field.getName(), javaFieldName,
                                                                      getFieldMappings( field.getTable() ) );
            return new TableMapping( List.class, null, field.getName(), javaFieldName, structureMapping );
        }
        try
        {
            Class<?> associatedClass = Class.forName( field.getClassNameOfValue() );
            return new FieldMapping( associatedClass, field.getName(), javaFieldName, null );
        }
        catch ( ClassNotFoundException e )
        {
            throw new InternalHiberSapException( "Can not get class for " + field.getClassNameOfValue(), e );
        }
    }

    private Set<FieldMapping> getFieldMappings( JCoRecord record )
    {
        Set<FieldMapping> result = new HashSet<FieldMapping>();

        JCoFieldIterator iter = record.getFieldIterator();

        while ( iter.hasNextField() )
        {
            FieldMapping fieldParam = (FieldMapping) getParameterMapping( iter.nextField() );
            result.add( fieldParam );
        }

        return result;
    }
}
