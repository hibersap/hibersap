package org.hibersap.jbossdeployer.it.simple;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.jbossdeployer.metadata.HiberSapMetaData;
import org.hibersap.jbossdeployer.metadata.Property;
import org.hibersap.jbossdeployer.metadata.SessionManagerMetaData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ConfigurationMarshallTest
{
    private static final Log LOG = LogFactory.getLog( ConfigurationMarshallTest.class );

    private JAXBContext jaxbContext;

    @Before
    public void setup()
        throws JAXBException
    {
        jaxbContext = JAXBContext.newInstance( HiberSapMetaData.class, SessionManagerMetaData.class, Property.class );
    }

    @Test
    public void test()
        throws Exception
    {
        final InputStream configurationAsStream = getClass().getResourceAsStream( "/META-INF/hibersap.xml" );
        Assert.assertNotNull( configurationAsStream );

        final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        final Object unmarshalledObject = unmarshaller.unmarshal( configurationAsStream );
        final HiberSapMetaData hiberSapMetaData = (HiberSapMetaData) unmarshalledObject;

        final SessionManagerMetaData sessionManager = hiberSapMetaData.getSessionManager();
        Assert.assertNotNull( sessionManager );

        Assert.assertEquals( "A12", sessionManager.getName() );
        Assert.assertNotNull( sessionManager.getClasses() );
        Assert.assertEquals( 2, sessionManager.getClasses().size() );
    }

    @Test
    public void testMarshalling()
        throws Exception
    {
        final List<Property> properties = new ArrayList<Property>();
        final Property jcoProperty = new Property( "name", "value" );
        properties.add( jcoProperty );

        final SessionManagerMetaData sessionManagerMetaData = new SessionManagerMetaData( "session-name",
                                                                                          "ContextClass", properties );

        final List<String> classes = new ArrayList<String>();
        classes.add( "package.Class1" );
        classes.add( "package.Class2" );
        sessionManagerMetaData.setClasses( classes );

        final HiberSapMetaData hiberSapMetaData = new HiberSapMetaData( sessionManagerMetaData );

        final Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty( "jaxb.formatted.output", Boolean.TRUE );

        final StringWriter stringWriter = new StringWriter();
        marshaller.marshal( hiberSapMetaData, stringWriter );
        LOG.info( stringWriter.toString() );
    }
}