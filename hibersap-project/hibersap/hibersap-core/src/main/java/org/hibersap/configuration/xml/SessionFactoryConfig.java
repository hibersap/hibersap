package org.hibersap.configuration.xml;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType(namespace = HibersapConfig.NAMESPACE, propOrder = { "context", "jcaConnectionFactory", "properties", "classes" })
@XmlAccessorType(XmlAccessType.PROPERTY)
public class SessionFactoryConfig
{

    @XmlTransient
    private static final long serialVersionUID = 1;

    private String name;

    private String context;

    private Set<Property> properties = new HashSet<Property>( 0 );

    private Set<String> classes = new HashSet<String>( 0 );

    private String jcaConnectionFactory;

    public SessionFactoryConfig()
    {
    }

    public SessionFactoryConfig( String name )
    {
        this.name = name;
    }

    SessionFactoryConfig( final String name, final String context, final Set<Property> properties )
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

    @XmlElement(name = "context", required = false, namespace = HibersapConfig.NAMESPACE)
    public String getContext()
    {
        return context;
    }

    @XmlElement(name = "jca-connection-factory", required = false, namespace = HibersapConfig.NAMESPACE)
    public String getJcaConnectionFactory()
    {
        return jcaConnectionFactory;
    }

    @XmlElement(name = "property", namespace = HibersapConfig.NAMESPACE)
    @XmlElementWrapper(name = "properties", namespace = HibersapConfig.NAMESPACE)
    public Set<Property> getProperties()
    {
        return properties;
    }

    public SessionFactoryConfig setJcaConnectionFactory( final String jcaConnectionFactory )
    {
        this.jcaConnectionFactory = jcaConnectionFactory;
        return this;
    }

    public void setProperties( final Set<Property> properties )
    {
        this.properties = properties;
    }

    @XmlElement(name = "class", namespace = HibersapConfig.NAMESPACE)
    @XmlElementWrapper(name = "annotated-classes", namespace = HibersapConfig.NAMESPACE)
    public Set<String> getClasses()
    {
        return classes;
    }

    public void setClasses( final Set<String> classes )
    {
        this.classes = classes;
    }

    public String getProperty( String name )
    {
        for ( Property property : properties )
        {
            if ( property.getName().equals( name ) )
                return property.getValue();
        }
        return null;
    }

    public SessionFactoryConfig setContext( String context )
    {
        this.context = context;
        return this;
    }

    public SessionFactoryConfig setProperty( String name, String value )
    {
        Property property = new Property( name, value );
        if ( properties.contains( property ) )
        {
        }
        properties.add( property );
        return this;
    }

    public SessionFactoryConfig addClass( Class<?> annotatedClass )
    {
        classes.add( annotatedClass.getName() );
        return this;
    }
}