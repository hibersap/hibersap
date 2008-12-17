package org.hibersap.configuration.xml;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.hibersap.configuration.Environment;
import org.hibersap.execution.jco.JCoContext;
import org.junit.Assert;
import org.junit.Test;

public class HiberSapJaxbXmlParserTest {

	@Test
	public void testOK() throws Exception {
		final HiberSapJaxbXmlParser hiberSapJaxbXmlParser = new HiberSapJaxbXmlParser();
		final Properties properties = hiberSapJaxbXmlParser
				.parseResource("/xml-configurations/hibersapOK.xml");

		Assert.assertNotNull(properties);

		testForNotNull(properties, Environment.SESSION_FACTORY_NAME);
		testForNotNull(properties, Environment.CONTEXT_CLASS);

		final Set<String> bapiClasses = new HashSet<String>();
		final Set<String> sapProperties = new HashSet<String>();

		for (final Object keyObject : properties.keySet()) {
			final String key = (String) keyObject;
			if (key.startsWith(Environment.BABI_CLASSES_PREFIX)) {
				bapiClasses.add(properties.getProperty(key));
			} else if (key.startsWith(JCoContext.HIBERSAP_JCO_PREFIX)) {
				sapProperties.add(properties.getProperty(key));
			}
		}

		Assert.assertEquals(2, bapiClasses.size());
		Assert.assertEquals(7, sapProperties.size());
	}

	private void testForNotNull(final Properties properties,
			final String propertyName) {
		final String contextClass = (String) properties.get(propertyName);
		Assert.assertNotNull(contextClass);
	}
}
