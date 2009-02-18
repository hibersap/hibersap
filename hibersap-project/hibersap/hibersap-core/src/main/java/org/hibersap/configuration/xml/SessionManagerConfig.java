package org.hibersap.configuration.xml;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType(namespace = HibersapConfig.NAMESPACE, propOrder = { "context",
		"jcaConnectionFactory", "properties", "classes" })
@XmlAccessorType(XmlAccessType.PROPERTY)
public class SessionManagerConfig {

	@XmlTransient
	private static final long serialVersionUID = 1;

	private String name;

	private String context;

	private final Set<Property> properties = new HashSet<Property>();

	private Map<String, String> nameValues = null;

	private Set<String> classes = new HashSet<String>();

	private String jcaConnectionFactory;

	public SessionManagerConfig() {
		System.out.println("PUB-CONSTRUCTOR");
		System.out.println("properties = " + properties);
	}

	public SessionManagerConfig(final String name) {
		this.name = name;
		System.out.println("CONSTRUCTOR #1");
		System.out.println("properties = " + properties + " name=" + name);
	}

	SessionManagerConfig(final String name, final String context,
			final Set<Property> properties) {
		super();
		System.out.println("CONSTRUCTOR #2");
		System.out.println("properties = " + properties + " name=" + name);
		this.name = name;
		this.context = context;
		setProperties(properties);
	}

	@XmlAttribute(required = true)
	public String getName() {
		return name;
	}

	public SessionManagerConfig setName(final String name) {
		this.name = name;
		return this;
	}

	@XmlElement(name = "context", required = false, namespace = HibersapConfig.NAMESPACE)
	public String getContext() {
		return context;
	}

	@XmlElement(name = "jca-connection-factory", required = false, namespace = HibersapConfig.NAMESPACE)
	public String getJcaConnectionFactory() {
		return jcaConnectionFactory;
	}

	@XmlElement(name = "property", namespace = HibersapConfig.NAMESPACE)
	@XmlElementWrapper(name = "properties", namespace = HibersapConfig.NAMESPACE)
	public Set<Property> getProperties() {
		return properties;
	}

	public SessionManagerConfig setJcaConnectionFactory(
			final String jcaConnectionFactory) {
		this.jcaConnectionFactory = jcaConnectionFactory;
		return this;
	}

	public void setProperties(final Set<Property> properties) {
		System.out.println("SETPROPS");
		this.properties.clear();
		this.properties.addAll(properties);
		nameValues = null;

	}

	@XmlElement(name = "class", namespace = HibersapConfig.NAMESPACE)
	@XmlElementWrapper(name = "annotated-classes", namespace = HibersapConfig.NAMESPACE)
	public Set<String> getClasses() {
		return classes;
	}

	public void setClasses(final Set<String> classes) {
		this.classes = classes;
	}

	public String getProperty(final String name) {
		return getNameValues().get(name);
	}

	public SessionManagerConfig setContext(final String context) {
		this.context = context;
		return this;
	}

	public SessionManagerConfig setProperty(final String name,
			final String value) {
		final String currentValue = getNameValues().get(name);
		if (currentValue != null) {
			final Property oldProperty = new Property(name, currentValue);
			assert properties.contains(oldProperty);
			final boolean oldValueExisted = properties.remove(oldProperty);
			assert oldValueExisted;
		}

		// do not use the getter, because the two collections are temporarily
		// out of sync
		// TODO: think about errors
		nameValues.put(name, value);
		final Property newProperty = new Property(name, value);
		properties.add(newProperty);
		assert nameValues.size() == properties.size() : nameValues.size()
				+ " != " + properties.size();

		return this;
	}

	public SessionManagerConfig addClass(final Class<?> annotatedClass) {
		classes.add(annotatedClass.getName());
		return this;
	}

	private Map<String, String> getNameValues() {
		// This is pretty complicated and could be simplified, if the
		// JAXB implementation is actually
		// @XmlAccessorType(XmlAccessType.PROPERTY)
		// Unfortunately the value of properties is still set to the field,
		// thus, we have to build the map lazily...
		if (nameValues == null) {
			nameValues = new HashMap<String, String>(properties.size());
			for (final Property property : properties) {
				final String oldValue = nameValues.put(property.getName(),
						property.getValue());
			}
		}
		assert nameValues != null;
		assert nameValues.size() == properties.size() : "Map "
				+ nameValues.size() + "!= Set " + properties.size();

		return nameValues;
	}

	@Override
	public String toString() {
		return "Session Configuration: " + name + "\nContext: " + context
				+ "\nProperties: " + properties + "\nClasses: " + classes;
	}
}