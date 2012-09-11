package org.hibersap.generation.bapi;

import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.mapping.model.FieldMapping;
import org.hibersap.mapping.model.ParameterMapping;
import org.hibersap.mapping.model.StructureMapping;
import org.hibersap.mapping.model.TableMapping;
import org.hibersap.session.SessionManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

public class ReverseBapiMapperTest
{
    private SessionManager sessionManager;

    private ReverseBapiMapper mapper = new ReverseBapiMapper();

    @Before
    public void buildSessionManager() throws Exception
    {
        AnnotationConfiguration configuration = new AnnotationConfiguration();
        sessionManager = configuration.buildSessionManager();
    }

    @After
    public void closeSessionManager() throws Exception
    {
        sessionManager.close();
    }

    @Test
    public void mapsBapi() throws Exception
    {
        BapiMapping map = mapper.map( "BAPI_SFLIGHT_GETLIST", sessionManager );

        assertThat( map ).isNotNull();
        assertThat( map.getBapiName() ).isEqualTo( "BAPI_SFLIGHT_GETLIST" );
        assertThat( map.getAssociatedClass() ).isNull();
        assertThat( map.getErrorHandling() ).isNull();
    }

    @Test
    public void mapsAllImportParameters() throws Exception
    {
        BapiMapping map = mapper.map( "BAPI_SFLIGHT_GETLIST", sessionManager );

        Set<ParameterMapping> imports = map.getImportParameters();

        assertThat( collectSapNames( imports ) ).containsOnly( "AFTERNOON", "AIRLINECARRIER", "FROMCITY",
                "FROMCOUNTRYKEY", "MAXREAD", "TOCITY", "TOCOUNTRYKEY" );
    }

    @Test
    public void mapsAllExportParameters() throws Exception
    {
        BapiMapping map = mapper.map( "BAPI_SFLIGHT_GETLIST", sessionManager );

        Set<ParameterMapping> exports = map.getExportParameters();

        assertThat( collectSapNames( exports ) ).containsOnly( "RETURN" );
    }

    @Test
    public void mapsStructureOfExportParamaterReturn() throws Exception
    {
        BapiMapping map = mapper.map( "BAPI_SFLIGHT_GETLIST", sessionManager );

        Set<ParameterMapping> exports = map.getExportParameters();

        StructureMapping structure = ( StructureMapping ) exports.iterator().next();
        Set<FieldMapping> parameters = structure.getParameters();

        assertThat( collectSapNames( parameters ) ).containsOnly( "TYPE", "ID", "NUMBER", "MESSAGE",
                "LOG_NO", "LOG_MSG_NO", "MESSAGE_V1", "MESSAGE_V2", "MESSAGE_V3", "MESSAGE_V4",
                "PARAMETER", "ROW", "FIELD", "SYSTEM" );
    }

    @Test
    public void mapsAllTableParameters() throws Exception
    {
        BapiMapping map = mapper.map( "BAPI_SFLIGHT_GETLIST", sessionManager );

        Set<TableMapping> tables = map.getTableParameters();
        assertThat( collectSapNames( tables ) ).containsOnly( "FLIGHTLIST" );
    }

    @Test
    public void mapsStructureOfTableParameterFLightList() throws Exception
    {
        BapiMapping map = mapper.map( "BAPI_SFLIGHT_GETLIST", sessionManager );

        TableMapping table = map.getTableParameters().iterator().next();
        StructureMapping tableStructure = table.getComponentParameter();
        Set<FieldMapping> tableElements = tableStructure.getParameters();

        assertThat( collectSapNames( tableElements ) ).containsOnly( "CARRID", "CONNID", "FLDATE", "AIRPFROM",
                "AIRPTO", "DEPTIME", "SEATSMAX", "SEATSOCC" );
    }

    @Test
    public void mapsRawStringDataType() throws Exception
    {
        BapiMapping map = mapper.map( "STFC_DEEP_STRUCTURE", sessionManager );

        Set<ParameterMapping> imports = map.getImportParameters();

        assertThat( collectSapNames( imports ) ).containsOnly( "IMPORTSTRUCT" );
    }

    @Test
    public void mapsStructureWithRawStringDataType() throws Exception
    {
        BapiMapping map = mapper.map( "STFC_DEEP_STRUCTURE", sessionManager );

        StructureMapping importStruct = ( StructureMapping ) map.getImportParameters().iterator().next();
        assertThat( importStruct.getSapName() ).isEqualTo( "IMPORTSTRUCT" );

        Set<FieldMapping> fields = importStruct.getParameters();
        assertThat( collectSapNames( fields ) ).containsOnly( "I", "C", "STR", "XSTR" );
    }

    @Test
    public void mapsStructureFieldWithRawStringDataType() throws Exception
    {
        BapiMapping map = mapper.map( "STFC_DEEP_STRUCTURE", sessionManager );

        StructureMapping importStruct = ( StructureMapping ) map.getImportParameters().iterator().next();
        FieldMapping xstr = getField( importStruct.getParameters(), "XSTR" );
        assertThat( xstr.getJavaName() ).isEqualTo( "_xstr" );
        assertThat( xstr.getParamType() ).isSameAs( ParameterMapping.ParamType.FIELD );
        assertThat( xstr.getAssociatedType() ).isSameAs( byte[].class );
        assertThat( xstr.getConverterClass() ).isNull();
    }

    private FieldMapping getField( Set<FieldMapping> fields, String sapName )
    {
        for ( FieldMapping field : fields )
        {
            if ( field.getSapName().equals( sapName ) )
            {
                return field;
            }
        }
        throw fail( "Expected: fieldMapping has element with SAP name " + sapName );
    }

    private Set<String> collectSapNames( Set<? extends ParameterMapping> mappings )
    {
        HashSet<String> sapNames = new HashSet<String>();
        for ( ParameterMapping mapping : mappings )
        {
            sapNames.add( mapping.getSapName() );
        }
        return sapNames;
    }
}
