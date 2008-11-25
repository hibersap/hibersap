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
        final Properties properties = createParser( "" );

        assertTrue( properties.isEmpty() );
    }

    @Test
    public void testMinimalXML()
        throws Exception
    {
        final Properties properties = createParser( "<hibersap><session-factory name=\"TEST\">"
            + "<context> JCoContext </context></session-factory></hibersap>" );

        assertEquals( "TEST", properties.getProperty( Environment.SESSION_FACTORY_NAME ) );
        assertEquals( "JCoContext", properties.getProperty( Environment.CONTEXT_CLASS ) );
    }

    @Test
    public void testTooManySessionFactories()
        throws Exception
    {
        final Properties properties = createParser( "<hibersap><session-factory name=\"TEST1\">"
            + "<context> JCoContext </context></session-factory>" + "<session-factory name=\"TEST2\">"
            + "<context> JCoContext </context></session-factory></hibersap>" );

        assertTrue( properties.isEmpty() );
    }

    @Test
    public void testSessionNameMissing()
        throws Exception
    {
        final Properties properties = createParser( "<hibersap>" + "<session-factory>"
            + "<context> JCoContext </context></session-factory></hibersap>" );

        assertTrue( properties.isEmpty() );
    }

    @Test
    public void testContextMissing()
        throws Exception
    {
        final Properties properties = createParser( "<hibersap><session-factory name=\"TEST\">"
            + " </session-factory></hibersap>" );

        assertTrue( properties.toString(), properties.isEmpty() );
    }

    @Test
    public void testContextWithEmptyContent()
        throws Exception
    {
        final Properties properties = createParser( "<hibersap><session-factory name=\"TEST\">"
            + "<context> \t\n</context></session-factory></hibersap>" );

        assertTrue( properties.toString(), properties.isEmpty() );
    }

    @Test
    public void testBrokenEndTag()
        throws Exception
    {
        final Properties properties = createParser( "<hibersap><session-factory name=\"TEST\">"
            + "<context> JCoContext </context></session-factory></hiberSAP>" );

        assertTrue( properties.isEmpty() );
    }

    @Test
    public void testProperties()
        throws Exception
    {
        final Properties properties = createParser( "<hibersap><session-factory name=\"TEST\">"
            + "<context> JCoContext </context>"
            + "<properties><property name=\"hibersap.jco.client.client\" value=\" 4711\" /></properties>"
            + "</session-factory></hibersap>" );

        assertEquals( "4711", properties.getProperty( "hibersap.jco.client.client" ) );
    }

    @Test
    public void testPropertiesNameMissing()
        throws Exception
    {
        final Properties properties = createParser( "<hibersap><session-factory name=\"TEST\">"
            + "<context> JCoContext </context>" + "<properties><property value=\" 4711\" /></properties>"
            + "</session-factory></hibersap>" );

        assertTrue( properties.isEmpty() );
    }

    private static Properties createParser( final String xml )
    {
        final HibersapXMLParser hibersapXMLParser = new HibersapXMLParser( "hibersap.xml",
                                                                           new ByteArrayInputStream( xml.getBytes() ) );
        return hibersapXMLParser.parseXML();
    }
}
