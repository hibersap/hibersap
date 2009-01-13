package org.hibersap.configuration.xml;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ConfigurationMarshallTest {

	private JAXBContext jaxbContext;

	@Before
	public void setup() throws JAXBException {
		jaxbContext = JAXBContext.newInstance(HiberSap.class,
				SessionFactory.class, JCoProperty.class);
	}

	@Test
	public void testParseOkConfiguration() throws Exception {
		final InputStream configurationAsStream = getClass()
				.getResourceAsStream("/xml-configurations/hibersapOK.xml");
		Assert.assertNotNull(configurationAsStream);

		final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		final Object unmarshalledObject = unmarshaller
				.unmarshal(configurationAsStream);
		final HiberSap hiberSapMetaData = (HiberSap) unmarshalledObject;

		final SessionFactory sessionFactory = hiberSapMetaData
				.getSessionFactory();
		Assert.assertNotNull(sessionFactory);

		Assert.assertEquals("A12", sessionFactory.getName());
		Assert.assertEquals(2, sessionFactory.getClasses().size());
	}

	@Test
	public void testMarshalling() throws Exception {
		final List<JCoProperty> properties = new ArrayList<JCoProperty>();
		final JCoProperty jcoProperty = new JCoProperty("name", "value");
		properties.add(jcoProperty);

		final SessionFactory sessionFactoryMetaData = new SessionFactory(
				"session-name", "ContextClass", properties);

		final List<String> classes = new ArrayList<String>();
		classes.add("package.Class1");
		classes.add("package.Class2");
		sessionFactoryMetaData.setClasses(classes);

		final HiberSap hiberSapMetaData = new HiberSap(sessionFactoryMetaData);

		final Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty("jaxb.formatted.output", Boolean.TRUE);

		final StringWriter stringWriter = new StringWriter();
		marshaller.marshal(hiberSapMetaData, stringWriter);
		System.out.println(stringWriter.toString());
	}
}