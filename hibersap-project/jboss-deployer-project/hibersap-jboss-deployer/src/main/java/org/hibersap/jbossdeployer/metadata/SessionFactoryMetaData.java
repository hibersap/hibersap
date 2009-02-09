package org.hibersap.jbossdeployer.metadata;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

// @XmlRootElement(name = "session-factoryX", namespace = HiberSapMetaData.NAMESPACE)
@XmlType(namespace = HiberSapMetaData.NAMESPACE, propOrder = { "context", "properties", "classes" })
@XmlAccessorType(XmlAccessType.PROPERTY)
public class SessionFactoryMetaData
    implements Serializable
{

    @XmlTransient
    private static final long serialVersionUID = 1;

    private String name;

    private String context;

    private List<Property> properties;

    private List<String> classes;

    public SessionFactoryMetaData()
    {
    }

    public SessionFactoryMetaData( final String name, final String context, final List<Property> properties )
    {
        super();
        this.name = name;
        this.context = context;
        this.properties = properties;
    }

    @XmlAttribute(required = true)
    public String getName()
    {
        return name;
    }

    public void setName( final String name )
    {
        this.name = name;
    }

    // TODO: Klassentyp benutzen
    @XmlElement(name = "context", required = false)
    public String getContext()
    {
        return context;
    }

    @XmlElement(name = "property", namespace = HiberSapMetaData.NAMESPACE)
    @XmlElementWrapper(name = "properties")
    public List<Property> getProperties()
    {
        return properties;
    }

    public void setContext( final String context )
    {
        this.context = context;
    }

    public void setProperties( final List<Property> properties )
    {
        this.properties = properties;
    }

    @XmlElement(name = "class", namespace = HiberSapMetaData.NAMESPACE)
    public List<String> getClasses()
    {
        return classes;
    }

    public void setClasses( final List<String> classes )
    {
        this.classes = classes;
    }

}