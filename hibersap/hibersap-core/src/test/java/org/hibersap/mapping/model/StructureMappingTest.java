package org.hibersap.mapping.model;

import org.hibersap.annotations.Parameter;
import org.hibersap.conversion.ConverterCache;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.fest.assertions.Assertions.assertThat;

public class StructureMappingTest
{

    private StructureMapping structureMapping;

    @Before
    public void setUp() throws Exception
    {
        structureMapping = new StructureMapping( TestStructureBean.class, "sapName", "javaName",
                null );
        structureMapping.addParameter( new FieldMapping( Integer.class, "sapField1", "javaField1", null ) );
        structureMapping.addParameter( new FieldMapping( float.class, "sapField2", "javaField2", null ) );
    }

    @Test
    public void testName() throws Exception
    {
        HashMap<String, Object> fieldMap = new HashMap<String, Object>();
        fieldMap.put( "sapField1", Integer.MAX_VALUE );
        fieldMap.put( "sapField2", Float.MAX_VALUE );

        Object value = structureMapping.getUnconvertedValueToJava( fieldMap, new ConverterCache() );

        assertThat( value ).isInstanceOf( TestStructureBean.class );
        TestStructureBean bean = ( TestStructureBean ) value;
        assertThat( bean.javaField1 ).isEqualTo( Integer.MAX_VALUE );
        assertThat( bean.javaField2 ).isEqualTo( Float.MAX_VALUE );
    }

    private static class TestStructureBean
    {
        @Parameter( "sapField1" )
        private Integer javaField1;

        @Parameter( "sapField2" )
        private float javaField2;

        private TestStructureBean()
        {
        }
    }
}
