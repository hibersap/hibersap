package org.hibersap.configuration.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.util.Properties;

import org.hibersap.configuration.Environment;
import org.junit.Test;

public class HibersapXMLParserTest
{
    @Test
    public void testEmptyXML()
        throws Exception
    {
        final Properties properties = parseXML( "" );

        assertTrue( properties.isEmpty() );
    }

    @Test
    public void testMinimalXML()
        throws Exception
    {
        final Properties properties = parseXML( "<hibersap><session-factory name=\"TEST\">"
            + "<context> JCoContext </context></session-factory></hibersap>" );

        assertEquals( "TEST", properties.getProperty( Environment.SESSION_FACTORY_NAME ) );
        assertEquals( "JCoContext", properties.getProperty( Environment.CONTEXT_CLASS ) );
    }

    @Test
    public void testTooManySessionFactories()
        throws Exception
    {
        final Properties properties = parseXML( "<hibersap><session-factory name=\"TEST1\">"
            + "<context> JCoContext </context></session-factory> <session-factory name=\"TEST2\">"
            + "<context> JCoContext </context></session-factory></hibersap>" );

        assertTrue( properties.isEmpty() );
    }

    @Test
    public void testSessionNameMissing()
        throws Exception
    {
        final Properties properties = parseXML( "<hibersap>" + "<session-factory>"
            + "<context> JCoContext </context></session-factory></hibersap>" );

        assertTrue( properties.isEmpty() );
    }

    @Test
    public void testContextMissing()
        throws Exception
    {
        final Properties properties = parseXML( "<hibersap><session-factory name=\"TEST\">"
            + " </session-factory></hibersap>" );

        assertTrue( properties.toString(), properties.isEmpty() );
    }

    @Test
    public void testContextWithEmptyContent()
        throws Exception
    {
        final Properties properties = parseXML( "<hibersap><session-factory name=\"TEST\">"
            + "<context> \t\n</context></session-factory></hibersap>" );

        assertTrue( properties.toString(), properties.isEmpty() );
    }

    @Test
    public void testBrokenEndTag()
        throws Exception
    {
        final Properties properties = parseXML( "<hibersap><session-factory name=\"TEST\">"
            + "<context> JCoContext </context></session-factory></hiberSAP>" );

        assertTrue( properties.isEmpty() );
    }

    @Test
    public void testProperties()
        throws Exception
    {
        final Properties properties = parseXML( "<hibersap><session-factory name=\"TEST\">"
            + "<context> JCoContext </context>"
            + "<properties><property name=\"hibersap.jco.client.client\" value=\" 4711\" /></properties>"
            + "</session-factory></hibersap>" );

        assertEquals( "4711", properties.getProperty( "hibersap.jco.client.client" ) );
    }

    @Test
    public void testPropertiesNameMissing()
        throws Exception
    {
        final Properties properties = parseXML( "<hibersap><session-factory name=\"TEST\">"
            + "<context> JCoContext </context>" + "<properties><property value=\" 4711\" /></properties>"
            + "</session-factory></hibersap>" );

        assertTrue( properties.isEmpty() );
    }

    @Test
    public void testMappedClasses()
        throws Exception
    {
        final Properties properties = parseXML( "<hibersap><session-factory name=\"TEST\">"
            + "<context>JCoContext</context><class>foo.bar.MyBapi1</class> <class>foo.bar.MyBapi2 </class></session-factory></hibersap>" );
        System.out.println( properties );
        assertEquals( "foo.bar.MyBapi1", properties.getProperty( Environment.BABI_CLASSES_PREFIX + "0" ) );
        assertEquals( "foo.bar.MyBapi2", properties.getProperty( Environment.BABI_CLASSES_PREFIX + "1" ) );
    }

    @Test
    public void testConnectionFactory()
        throws Exception
    {
        final Properties properties = parseXML( "<hibersap><session-factory name=\"TEST\">"
            + "<context> JTAContext </context> <jca-connection-factory> java:/eis/sap/A12 </jca-connection-factory></session-factory></hibersap>" );

        assertEquals( "java:/eis/sap/A12", properties.getProperty( Environment.JCA_CONNECTION_FACTORY ) );
        assertEquals( "JTAContext", properties.getProperty( Environment.CONTEXT_CLASS ) );
    }

    private static Properties parseXML( final String xml )
    {
        final HibersapXMLParser hibersapXMLParser = new HibersapXMLParser( "hibersap.xml",
                                                                           new ByteArrayInputStream( xml.getBytes() ) );
        return hibersapXMLParser.parseXML();
    }
}
