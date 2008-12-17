package org.hibersap.configuration.xml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.hibersap.InternalHiberSapException;
import org.hibersap.configuration.Environment;
import org.hibersap.execution.jco.JCoContext;

public class HiberSapJaxbXmlParser {

	private final JAXBContext jaxbContext;

	public HiberSapJaxbXmlParser() {
		try {
			jaxbContext = JAXBContext.newInstance(HiberSap.class,
					SessionFactory.class, JCoProperty.class);
		} catch (final JAXBException e) {
			throw new RuntimeException("Cannot not create a JAXB context.", e);
		}
	}

	public Properties parseResource(final String resourceName)
			throws HibersapParseException {
		final InputStream resourceStream = accessResource(resourceName);

		final HiberSap hiberSapConfig = parseResource(resourceStream,
				resourceName);

		final Properties properties = convertToProperties(hiberSapConfig);

		return properties;
	}

	private Properties convertToProperties(final HiberSap hiberSapConfig) {
		final Properties properties = new Properties();
		final SessionFactory sessionFactory = hiberSapConfig
				.getSessionFactory();
		properties.setProperty(Environment.SESSION_FACTORY_NAME, sessionFactory
				.getName());
		properties.setProperty(Environment.CONTEXT_CLASS, sessionFactory
				.getContext());

		final List<String> annotatedClasses = sessionFactory.getClasses();
		final Iterator<String> classesIterator = annotatedClasses.iterator();
		for (int idx = 0; idx < annotatedClasses.size(); idx++) {
			properties.setProperty(Environment.BABI_CLASSES_PREFIX + idx,
					classesIterator.next());
		}

		final List<JCoProperty> jcoProperties = sessionFactory
				.getJCoProperties();
		for (final JCoProperty jCoProperty : jcoProperties) {
			properties.setProperty(JCoContext.HIBERSAP_JCO_PREFIX
					+ jCoProperty.getName(), jCoProperty.getValue());
		}
		return properties;
	}

	private HiberSap parseResource(final InputStream resourceStream,
			final String resourceName) throws HibersapParseException {
		Unmarshaller unmarshaller;
		try {
			unmarshaller = jaxbContext.createUnmarshaller();
		} catch (final JAXBException e) {
			throw new InternalHiberSapException(
					"Cannot create an unmarshaller. ", e);
		}

		Object unmarshalledObject;
		try {
			unmarshalledObject = unmarshaller.unmarshal(resourceStream);
		} catch (final JAXBException e) {
			throw new HibersapParseException("Cannot parse the resource "
					+ resourceName, e);
		}
		if (unmarshalledObject == null) {
			throw new HibersapParseException("Resource " + resourceName
					+ " is empty.");
		}
		if (!(unmarshalledObject instanceof HiberSap)) {
			throw new HibersapParseException(
					"Resource "
							+ resourceName
							+ " does not consist of a hibersap specification. I found a "
							+ unmarshalledObject.getClass().getSimpleName());

		}
		final HiberSap hiberSapConfig = (HiberSap) unmarshalledObject;
		return hiberSapConfig;
	}

	private InputStream accessResource(final String resourceName) {
		URL resource = Thread.currentThread().getContextClassLoader()
				.getResource(resourceName);
		if (resource == null) {
			resource = getClass().getResource(resourceName);
		}
		if (resource == null) {
			throw new InternalHiberSapException("Cannot locate resource "
					+ resourceName);
		}
		final InputStream resourceStream;
		try {
			resourceStream = resource.openStream();
		} catch (final IOException e) {
			throw new InternalHiberSapException("Cannot open resource "
					+ resourceName, e);
		}
		return resourceStream;
	}

}
