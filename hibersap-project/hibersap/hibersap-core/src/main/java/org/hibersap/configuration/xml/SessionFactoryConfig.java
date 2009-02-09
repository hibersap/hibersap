package org.hibersap.configuration.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType(namespace = HiberSap.NAMESPACE, propOrder = { "context", "jcaConnectionFactory", "properties", "classes" })
@XmlAccessorType(XmlAccessType.PROPERTY)
public class SessionFactoryConfig
{

    @XmlTransient
    private static final long serialVersionUID = 1;

    private String name;

    private String context;

    private List<Property> properties = new ArrayList<Property>( 0 );

    private List<String> classes = new ArrayList<String>( 0 );

    private String jcaConnectionFactory;

    public SessionFactoryConfig()
    {
    }

    public SessionFactoryConfig( final String name, final String context, final List<Property> properties )
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

    @XmlElement(name = "context", required = false, namespace = HiberSap.NAMESPACE)
    public String getContext()
    {
        return context;
    }

    @XmlElement(name = "jca-connection-factory", required = false, namespace = HiberSap.NAMESPACE)
    public String getJcaConnectionFactory()
    {
        return jcaConnectionFactory;
    }

    @XmlElement(name = "property", namespace = HiberSap.NAMESPACE)
    @XmlElementWrapper(name = "properties", namespace = HiberSap.NAMESPACE)
    public List<Property> getProperties()
    {
        return properties;
    }

    public void setContext( final String context )
    {
        this.context = context;
    }

    public void setJcaConnectionFactory( final String jcaConnectionFactory )
    {
        this.jcaConnectionFactory = jcaConnectionFactory;
    }

    public void setProperties( final List<Property> properties )
    {
        this.properties = properties;
    }

    @XmlElement(name = "class", namespace = HiberSap.NAMESPACE)
    @XmlElementWrapper(name = "annotated-classes", namespace = HiberSap.NAMESPACE)
    public List<String> getClasses()
    {
        return classes;
    }

    public void setClasses( final List<String> classes )
    {
        this.classes = classes;
    }

}