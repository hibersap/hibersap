package org.hibersap.configuration.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Set;

import org.junit.Test;

public class HiberSapJaxbXmlParserTest
{
    @Test
    public void testOK()
        throws Exception
    {
        final HibersapJaxbXmlParser hiberSapJaxbXmlParser = new HibersapJaxbXmlParser();
        HibersapConfig config = hiberSapJaxbXmlParser.parseResource( "/xml-configurations/hibersapOK.xml" );

        assertNotNull( config );

        List<SessionFactoryConfig> sessionFactories = config.getSessionFactories();
        assertEquals( 2, sessionFactories.size() );

        SessionFactoryConfig sf1 = sessionFactories.get( 0 );
        assertEquals( "A12", sf1.getName() );
        assertEquals( "org.hibersap.execution.jco.JCoContext", sf1.getContext() );
        assertEquals( "java:/eis/sap/A12", sf1.getJcaConnectionFactory() );

        Set<Property> properties = sf1.getProperties();
        assertEquals( 7, properties.size() );

        Set<String> classes = sf1.getClasses();
        assertEquals( 2, classes.size() );

        SessionFactoryConfig sf2 = sessionFactories.get( 1 );
        assertEquals( "B34", sf2.getName() );
        assertEquals( "org.hibersap.execution.jco.JCAContext", sf2.getContext() );
        assertEquals( "java:/eis/sap/B34", sf2.getJcaConnectionFactory() );

        properties = sf2.getProperties();
        assertEquals( 7, properties.size() );

        classes = sf2.getClasses();
        assertEquals( 2, classes.size() );
    }
}
