package org.hibersap.generation.bapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.mapping.model.FieldMapping;
import org.hibersap.mapping.model.ObjectMapping;
import org.hibersap.mapping.model.ParameterMapping;
import org.hibersap.mapping.model.StructureMapping;
import org.hibersap.mapping.model.TableMapping;
import org.hibersap.session.SessionManager;
import org.junit.Test;

import com.sap.conn.jco.JCoException;

public class ReverseBapiMapperTest
{
    private SessionManager sessionManager;

    private ReverseBapiMapper mapper = new ReverseBapiMapper();

    @Test
    public void mapBapi()
        throws JCoException
    {
        AnnotationConfiguration configuration = new AnnotationConfiguration();
        sessionManager = configuration.buildSessionManager();

        BapiMapping map = mapper.map( "BAPI_SFLIGHT_GETLIST", sessionManager );
        assertNotNull( map );
        assertEquals( "BAPI_SFLIGHT_GETLIST", map.getBapiName() );
        assertEquals( null, map.getAssociatedClass() );
        assertEquals( null, map.getErrorHandling() );

        Set<ObjectMapping> imports = map.getImportParameters();
        checkContains( imports, "AFTERNOON", "AIRLINECARRIER", "FROMCITY", "FROMCOUNTRYKEY", "MAXREAD", "TOCITY",
                       "TOCOUNTRYKEY" );

        Set<ObjectMapping> exports = map.getExportParameters();
        checkContains( exports, "RETURN" );
        StructureMapping returnStruct = (StructureMapping) exports.iterator().next();
        Set<FieldMapping> returnElements = returnStruct.getParameters();
        checkContains( returnElements, "TYPE", "ID", "NUMBER", "MESSAGE", "LOG_NO", "LOG_MSG_NO", "MESSAGE_V1",
                       "MESSAGE_V2", "MESSAGE_V3", "MESSAGE_V4", "PARAMETER", "ROW", "FIELD", "SYSTEM" );

        Set<TableMapping> tables = map.getTableParameters();
        checkContains( tables, "FLIGHTLIST" );
        TableMapping table = tables.iterator().next();
        StructureMapping tableStructure = table.getComponentParameter();
        Set<FieldMapping> tableElements = tableStructure.getParameters();
        checkContains( tableElements, "CARRID", "CONNID", "FLDATE", "AIRPFROM", "AIRPTO", "DEPTIME", "SEATSMAX",
                       "SEATSOCC" );
    }

    private void checkContains( Set<? extends ParameterMapping> mappings, String... names )
    {
        assertNotNull( mappings );
        assertEquals( names.length, mappings.size() );
        for ( Iterator<? extends ParameterMapping> iterator = mappings.iterator(); iterator.hasNext(); )
        {
            ParameterMapping mapping = iterator.next();
            String sapName = mapping.getSapName();
            assertTrue( sapName + " missing", ArrayUtils.contains( names, sapName ) );
        }
    }
}
