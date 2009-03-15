package org.hibersap.configuration.xml;

/**
 * Copyright (C) 2008-2009 akquinet tech@spree GmbH
 * 
 * This file is part of Hibersap.
 * 
 * Hibersap is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * Hibersap is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with Hibersap. If
 * not, see <http://www.gnu.org/licenses/>.
 */

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.session.ExecutionInterceptor;

@XmlType(namespace = HibersapConfig.NAMESPACE, propOrder = {
    "context",
    "jcaConnectionFactory",
    "jcaConnectionSpecFactory",
    "properties",
    "classes",
    "interceptorClasses" })
@XmlAccessorType(XmlAccessType.PROPERTY)
public class SessionManagerConfig
{

    @XmlTransient
    private static final long serialVersionUID = 1;

    @XmlTransient
    private static final Log LOG = LogFactory.getLog( SessionManagerConfig.class );

    private String name;

    private String context = "org.hibersap.execution.jco.JCoContext";

    private final Set<Property> properties = new HashSet<Property>();

    private Map<String, String> nameValues = null;

    private Set<String> classes = new HashSet<String>();

    private Set<String> interceptorClasses = new HashSet<String>();

    private String jcaConnectionFactory;

    private String jcaConnectionSpecFactory = "org.hibersap.execution.jca.cci.SapBapiJcaAdapterConnectionSpecFactory";

    public SessionManagerConfig()
    {
        LOG.debug( "PUB-CONSTRUCTOR" );
        LOG.debug( "properties = " + properties );
    }

    public SessionManagerConfig( final String name )
    {
        this.name = name;
        LOG.debug( "CONSTRUCTOR #1" );
        LOG.debug( "properties = " + properties + " name=" + name );
    }

    SessionManagerConfig( final String name, final String context, final Set<Property> properties )
    {
        super();
        LOG.debug( "CONSTRUCTOR #2" );
        LOG.debug( "properties = " + properties + " name=" + name );
        this.name = name;
        this.context = context;
        setProperties( properties );
    }

    @XmlAttribute(required = true)
    public String getName()
    {
        return name;
    }

    public SessionManagerConfig setName( final String name )
    {
        this.name = name;
        return this;
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

    public SessionManagerConfig setJcaConnectionFactory( final String jcaConnectionFactory )
    {
        this.jcaConnectionFactory = jcaConnectionFactory;
        return this;
    }

    @XmlElement(name = "jca-connectionspec-factory", required = false, namespace = HibersapConfig.NAMESPACE)
    public String getJcaConnectionSpecFactory()
    {
        return jcaConnectionSpecFactory;
    }

    public SessionManagerConfig setJcaConnectionSpecFactory( final String jcaConnectionSpecFactory )
    {
        this.jcaConnectionSpecFactory = jcaConnectionSpecFactory;
        return this;
    }

    @XmlElement(name = "property", namespace = HibersapConfig.NAMESPACE)
    @XmlElementWrapper(name = "properties", namespace = HibersapConfig.NAMESPACE)
    public Set<Property> getProperties()
    {
        return properties;
    }

    public void setProperties( final Set<Property> properties )
    {
        LOG.debug( "SETPROPS" );
        this.properties.clear();
        this.properties.addAll( properties );
        nameValues = null;

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

    public String getProperty( final String propertyName )
    {
        return getNameValues().get( propertyName );
    }

    public SessionManagerConfig setContext( final String context )
    {
        this.context = context;
        return this;
    }

    public SessionManagerConfig setProperty( final String name, final String value )
    {
        final String currentValue = getNameValues().get( name );
        if ( currentValue != null )
        {
            final Property oldProperty = new Property( name, currentValue );
            assert properties.contains( oldProperty );
            final boolean oldValueExisted = properties.remove( oldProperty );
            assert oldValueExisted;
        }

        // do not use the getter, because the two collections are temporarily
        // out of sync
        // TODO: think about errors
        nameValues.put( name, value );
        final Property newProperty = new Property( name, value );
        properties.add( newProperty );
        assert nameValues.size() == properties.size() : nameValues.size() + " != " + properties.size();

        return this;
    }

    public SessionManagerConfig addAnnotatedClass( final Class<?> annotatedClass )
    {
        classes.add( annotatedClass.getName() );
        return this;
    }

    private Map<String, String> getNameValues()
    {
        // This is pretty complicated and could be simplified, if the
        // JAXB implementation is actually
        // @XmlAccessorType(XmlAccessType.PROPERTY)
        // Unfortunately the value of properties is still set to the field,
        // thus, we have to build the map lazily...
        if ( nameValues == null )
        {
            nameValues = new HashMap<String, String>( properties.size() );
            for ( final Property property : properties )
            {
                final String oldValue = nameValues.put( property.getName(), property.getValue() );
            }
        }
        assert nameValues != null;
        assert nameValues.size() == properties.size() : "Map " + nameValues.size() + "!= Set " + properties.size();

        return nameValues;
    }

    @Override
    public String toString()
    {
        return "Session Configuration: " + name + "\nContext: " + context + "\nProperties: " + properties
            + "\nClasses: " + classes;
    }

    @XmlElement(name = "class", namespace = HibersapConfig.NAMESPACE)
    @XmlElementWrapper(name = "interceptor-classes", namespace = HibersapConfig.NAMESPACE)
    public Set<String> getInterceptorClasses()
    {
        return interceptorClasses;
    }

    public void setInteceptorClasses( final Set<String> interceptorClasses )
    {
        this.interceptorClasses = interceptorClasses;
    }

    public void addInterceptor( Class<? extends ExecutionInterceptor> interceptorClass )
    {
        interceptorClasses.add( interceptorClass.getName() );
    }
}