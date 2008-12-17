package org.hibersap.configuration.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType(namespace = HiberSap.NAMESPACE, propOrder = { "context",
		"JCoProperties", "classes" })
@XmlAccessorType(XmlAccessType.PROPERTY)
public class SessionFactory {

	@XmlTransient
	private static final long serialVersionUID = 1;

	private String name;

	private String context;

	private List<JCoProperty> jCoProperties;

	private List<String> classes;

	public SessionFactory() {
	}

	public SessionFactory(final String name, final String context,
			final List<JCoProperty> coProperties) {
		super();
		this.name = name;
		this.context = context;
		jCoProperties = coProperties;
	}

	@XmlAttribute(required = true)
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@XmlElement(name = "context", required = false, namespace = HiberSap.NAMESPACE)
	public String getContext() {
		return context;
	}

	@XmlElement(name = "property", namespace = HiberSap.NAMESPACE)
	@XmlElementWrapper(name = "jco-properties", namespace = HiberSap.NAMESPACE)
	public List<JCoProperty> getJCoProperties() {
		return jCoProperties;
	}

	public void setContext(final String context) {
		this.context = context;
	}

	public void setJCoProperties(final List<JCoProperty> coProperties) {
		jCoProperties = coProperties;
	}

	@XmlElement(name = "class", namespace = HiberSap.NAMESPACE)
	@XmlElementWrapper(name = "annotated-classes", namespace = HiberSap.NAMESPACE)
	public List<String> getClasses() {
		return classes;
	}

	public void setClasses(final List<String> classes) {
		this.classes = classes;
	}

}