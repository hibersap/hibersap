package org.hibersap.configuration.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "property", namespace = HiberSap.NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
public class JCoProperty {

	@XmlAttribute(name = "name")
	private String name;

	@XmlAttribute(name = "value")
	private String value;

	public JCoProperty() {
	}

	public JCoProperty(final String name, final String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

}
