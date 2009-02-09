package org.hibersap.configuration.xml;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.configuration.HibersapProperties;
import org.junit.Assert;
import org.junit.Test;

public class HiberSapJaxbXmlParserTest
{
    private static final Log LOG = LogFactory.getLog( HiberSapJaxbXmlParserTest.class );

    @Test
    public void testOK()
        throws Exception
    {
        final HiberSapJaxbXmlParser hiberSapJaxbXmlParser = new HiberSapJaxbXmlParser();
        final Properties properties = hiberSapJaxbXmlParser.parseResource( "/xml-configurations/hibersapOK.xml" );

        Assert.assertNotNull( properties );

        testForNotNull( properties, HibersapProperties.SESSION_FACTORY_NAME );
        testForNotNull( properties, HibersapProperties.CONTEXT_CLASS );
        testForNotNull( properties, HibersapProperties.JCA_CONNECTION_FACTORY );

        final Set<String> bapiClasses = new HashSet<String>();
        final Set<String> jcoProperties = new HashSet<String>();

        for ( final Object keyObject : properties.keySet() )
        {
            final String key = (String) keyObject;
            if ( key.startsWith( HibersapProperties.BAPI_CLASSES_PREFIX ) )
            {
                bapiClasses.add( properties.getProperty( key ) );
            }
            else if ( key.startsWith( "jco." ) )
            {
                jcoProperties.add( properties.getProperty( key ) );
            }
        }

        Assert.assertEquals( 2, bapiClasses.size() );
        Assert.assertEquals( 7, jcoProperties.size() );
    }

    private void testForNotNull( final Properties properties, final String propertyName )
    {
        final String propertyValue = (String) properties.get( propertyName );
        LOG.info( propertyName + "=" + propertyValue );
        Assert.assertNotNull( propertyValue );
    }
}
