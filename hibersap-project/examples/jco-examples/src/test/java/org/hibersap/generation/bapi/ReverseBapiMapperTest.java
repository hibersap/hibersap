package org.hibersap.generation.bapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Set;

import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.mapping.model.ObjectMapping;
import org.hibersap.session.SessionFactory;
import org.junit.Test;

import com.sap.conn.jco.JCoException;

public class ReverseBapiMapperTest
{
    private SessionFactory sessionFactory;

    private ReverseBapiMapper mapper = new ReverseBapiMapper();

    @Test
    public void mapBapi()
        throws JCoException
    {
        AnnotationConfiguration configuration = new AnnotationConfiguration();
        sessionFactory = configuration.buildSessionFactory();

        BapiMapping map = mapper.map( "BAPI_SFLIGHT_GETLIST", sessionFactory );
        assertNotNull( map );
        assertEquals( "BAPI_SFLIGHT_GETLIST", map.getBapiName() );
        assertEquals( null, map.getAssociatedClass() );
        assertEquals( null, map.getErrorHandling() );

        Set<ObjectMapping> imports = map.getImportParameters();
        assertNotNull( imports );
        assertEquals( 7, imports.size() );

        assertNotNull( map.getExportParameters() );
        assertNotNull( map.getTableParameters() );
    }

    @Test
    public void getJavaFieldName()
        throws Exception
    {
        assertEquals( "_", mapper.getJavaFieldName( null ) );
        assertEquals( "_", mapper.getJavaFieldName( "" ) );
        assertEquals( "_x", mapper.getJavaFieldName( "X" ) );
        assertEquals( "_xY", mapper.getJavaFieldName( "X_Y" ) );
        assertEquals( "_xYZ", mapper.getJavaFieldName( "X_Y_Z" ) );
        assertEquals( "_myLittleField", mapper.getJavaFieldName( "MY_LITTLE_FIELD" ) );

    }
}
