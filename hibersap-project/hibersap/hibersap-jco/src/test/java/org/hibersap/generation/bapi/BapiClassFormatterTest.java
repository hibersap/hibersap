package org.hibersap.generation.bapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.annotations.Table;
import org.hibersap.mapping.AnnotationBapiMapper;
import org.hibersap.mapping.model.BapiMapping;
import org.junit.Test;

public class BapiClassFormatterTest
{
    private final BapiClassFormatter formatter = new BapiClassFormatter();

    private final AnnotationBapiMapper mapper = new AnnotationBapiMapper();

    @Test
    public void createClass()
        throws Exception
    {
        BapiMapping bapiMapping = mapper.mapBapi( BapiTest.class );
        Map<String, String> classes = formatter.createClasses( bapiMapping, "org.hibersap.generated.test" );

        assertNotNull( classes );

        Set<String> classNames = classes.keySet();
        for ( String className : classNames )
        {
            System.out.println( "\n\n" + className + ":" );
            System.out.println( classes.get( className ) );
        }

        assertEquals( 3, classes.size() );

        assertTrue( classes.containsKey( "BapiTest" ) );
        assertTrue( classes.containsKey( "TestStructure" ) );
        assertTrue( classes.containsKey( "TestTable" ) );
    }

    @SuppressWarnings("unused")
    @Bapi("BAPI_TEST")
    private class BapiTest
    {
        @Import
        @Parameter("TEST_STRING")
        private String _testString;

        @Import
        @Parameter("TEST_INT")
        private int _testInt;

        @Export
        @Parameter(value = "TEST_STRUCTURE", type = ParameterType.STRUCTURE)
        private TestStructure _testStructure;

        @Table
        @Parameter("TEST_TABLE")
        private List<TestTable> _testTable;
    }

    @SuppressWarnings("unused")
    @BapiStructure
    private class TestStructure
    {
        @Parameter("STRUCT_STRING")
        private String _structString;

        @Parameter("STRUCT_INT")
        private int _structInt;
    }

    @SuppressWarnings("unused")
    @BapiStructure
    private class TestTable
    {
        @Parameter("TABLE_STRING")
        private String _tableString;

        @Parameter("TABLE_INT")
        private int _tableInt;
    }
}
