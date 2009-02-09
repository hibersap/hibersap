package org.hibersap.jbossdeployer.it.simple;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.hibersap.jbossdeployer.metadata.HiberSapMetaData;
import org.hibersap.jbossdeployer.metadata.Property;
import org.hibersap.jbossdeployer.metadata.SessionFactoryMetaData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ConfigurationMarshallTest {

	private JAXBContext jaxbContext;

	@Before
	public void setup() throws JAXBException {
		jaxbContext = JAXBContext.newInstance(HiberSapMetaData.class,
				SessionFactoryMetaData.class, Property.class);
	}

	@Test
	public void test() throws Exception {
		final InputStream configurationAsStream = getClass()
				.getResourceAsStream("/META-INF/hibersap.xml");
		Assert.assertNotNull(configurationAsStream);

		final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		final Object unmarshalledObject = unmarshaller
				.unmarshal(configurationAsStream);
		final HiberSapMetaData hiberSapMetaData = (HiberSapMetaData) unmarshalledObject;

		final SessionFactoryMetaData sessionFactory = hiberSapMetaData
				.getSessionFactory();
		Assert.assertNotNull(sessionFactory);

		Assert.assertEquals("A12", sessionFactory.getName());
		Assert.assertEquals(2, sessionFactory.getClasses().size());
	}

	@Test
	public void testMarshalling() throws Exception {
		final List<Property> properties = new ArrayList<Property>();
		final Property jcoProperty = new Property("name", "value");
		properties.add(jcoProperty);

		final SessionFactoryMetaData sessionFactoryMetaData = new SessionFactoryMetaData(
				"session-name", "ContextClass", properties);

		final List<String> classes = new ArrayList<String>();
		classes.add("package.Class1");
		classes.add("package.Class2");
		sessionFactoryMetaData.setClasses(classes);

		final HiberSapMetaData hiberSapMetaData = new HiberSapMetaData(
				sessionFactoryMetaData);

		final Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty("jaxb.formatted.output", Boolean.TRUE);

		final StringWriter stringWriter = new StringWriter();
		marshaller.marshal(hiberSapMetaData, stringWriter);
		System.out.println(stringWriter.toString());
	}
}