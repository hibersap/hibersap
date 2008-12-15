package org.hibersap.generation.bapi;

import org.hibersap.configuration.Environment;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.mapping.model.FieldMapping;
import org.hibersap.mapping.model.ObjectMapping;
import org.hibersap.mapping.model.ParameterMapping;
import org.hibersap.mapping.model.StructureMapping;
import org.hibersap.mapping.model.TableMapping;
import org.hibersap.session.SessionFactory;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoFunctionTemplate;
import com.sap.conn.jco.JCoParameterFieldIterator;
import com.sap.conn.jco.JCoParameterList;

public class ReverseBapiMapper
{
    public BapiMapping map( String bapiName, SessionFactory sessionFactory )
        throws JCoException
    {
        JCoDestination destination = JCoDestinationManager.getDestination( sessionFactory.getProperties()
            .getProperty( Environment.SESSION_FACTORY_NAME ) );

        JCoFunctionTemplate ft = destination.getRepository().getFunctionTemplate( bapiName );
        JCoFunction function = ft.getFunction();
        System.out.println( ft.toString() );

        BapiMapping mapping = new BapiMapping( null, function.getName(), null );

        JCoParameterList imports = function.getImportParameterList();
        JCoParameterFieldIterator iter = imports.getParameterFieldIterator();
        while ( iter.hasNextField() )
        {
            JCoField field = iter.nextField();
            ParameterMapping param = getParameterMapping( field );
            mapping.addImportParameter( (ObjectMapping) param );
        }

        return mapping;
    }

    private ParameterMapping getParameterMapping( JCoField field )
    {
        if ( field.isStructure() )
        {
            return new StructureMapping( null, field.getName(), getJavaFieldName( field.getName() ) );
        }
        if ( field.isTable() )
        {
            return new TableMapping( null, null, field.getName(), getJavaFieldName( field.getName() ),
                                     (StructureMapping) getParameterMapping( field ) );
        }

        return new FieldMapping( null, field.getName(), getJavaFieldName( field.getName() ), null );
    }

    String getJavaFieldName( String sapName )
    {
        StringBuffer result = new StringBuffer( "_" );

        if ( sapName == null )
            return result.toString();

        String[] parts = sapName.split( "_" );
        for ( int i = 0; i < parts.length; i++ )
        {
            if ( i == 0 )
            {
                result.append( parts[i].toLowerCase() );
            }
            else
            {
                result.append( parts[i].substring( 0, 1 ).toUpperCase() )
                    .append( parts[i].substring( 1 ).toLowerCase() );
            }
        }
        return result.toString();
    }
}
