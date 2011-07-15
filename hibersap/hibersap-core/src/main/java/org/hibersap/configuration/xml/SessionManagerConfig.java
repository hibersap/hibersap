/*
 * Copyright (c) 2009, 2011 akquinet tech@spree GmbH.
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

package org.hibersap.configuration.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.interceptor.BapiInterceptor;
import org.hibersap.interceptor.ExecutionInterceptor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@XmlType(namespace = HibersapConfig.NAMESPACE, propOrder = {
    "context",
    "jcaConnectionFactory",
    "jcaConnectionSpecFactory",
    "properties",
    "annotatedClasses",
    "executionInterceptorClasses",
    "bapiInterceptorClasses",
    "validationMode"})
@XmlAccessorType(XmlAccessType.PROPERTY)
public final class SessionManagerConfig implements Serializable
{
    private static final long serialVersionUID = 270142113574399232L;

    private static final Log LOG = LogFactory.getLog( SessionManagerConfig.class );

    private String name;

    private String context = "org.hibersap.execution.jco.JCoContext";

    private final Set<Property> properties = new HashSet<Property>();

    private final HashMap<String, String> nameValues = new HashMap<String, String>();

    private final HashSet<String> annotatedClasses = new HashSet<String>();

    private final HashSet<String> executionInterceptorClasses = new HashSet<String>();

    private final HashSet<String> bapiInterceptorClasses = new HashSet<String>();

    private String jcaConnectionFactory;

    private String jcaConnectionSpecFactory = "org.hibersap.execution.jca.cci.SapBapiJcaAdapterConnectionSpecFactory";

    private ValidationMode validationMode = ValidationMode.AUTO;

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
        nameValues.clear();

    }

    @XmlElement(name = "annotated-class", namespace = HibersapConfig.NAMESPACE)
    @XmlElementWrapper(name = "annotated-classes", namespace = HibersapConfig.NAMESPACE)
    public Set<String> getAnnotatedClasses()
    {
        return annotatedClasses;
    }

    public void setAnnotatedClasses( final Set<String> annotatedClasses )
    {
        this.annotatedClasses.clear();
        this.annotatedClasses.addAll( annotatedClasses );
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
        annotatedClasses.add( annotatedClass.getName() );
        return this;
    }

    public SessionManagerConfig setValidationMode( ValidationMode validationMode )
    {
        this.validationMode = validationMode;
        return this;
    }

    @XmlElement(name = "validation-mode", required = false, namespace = HibersapConfig.NAMESPACE)
    public ValidationMode getValidationMode()
    {
        return validationMode;
    }

    private Map<String, String> getNameValues()
    {
        // This is pretty complicated and could be simplified, if the
        // JAXB implementation is actually
        // @XmlAccessorType(XmlAccessType.PROPERTY)
        // Unfortunately the value of properties is still set to the field,
        // thus, we have to build the map lazily...
        if ( nameValues.isEmpty() )
        {
            for ( final Property property : properties )
            {
                nameValues.put( property.getName(), property.getValue() );
            }
        }
        assert nameValues.size() == properties.size() : "Map " + nameValues.size() + "!= Set " + properties.size();

        return nameValues;
    }

    @Override
    public String toString()
    {
        return "Session Configuration: " + name + ", Context: " + context + ", Properties: " + properties
            + ", Classes: " + annotatedClasses;
    }

    @XmlElement(name = "execution-interceptor-class", namespace = HibersapConfig.NAMESPACE)
    @XmlElementWrapper(name = "execution-interceptor-classes", namespace = HibersapConfig.NAMESPACE)
    public Set<String> getExecutionInterceptorClasses()
    {
        return executionInterceptorClasses;
    }

    public void setExecutionInterceptorClasses( final Collection<String> executionInterceptorClasses )
    {
        this.executionInterceptorClasses.clear();
        this.executionInterceptorClasses.addAll( executionInterceptorClasses );
    }

    public SessionManagerConfig addExecutionInterceptorClass( Class<? extends ExecutionInterceptor> interceptorClass )
    {
        executionInterceptorClasses.add( interceptorClass.getName() );
        return this;
    }

    @XmlElement(name = "bapi-interceptor-class", namespace = HibersapConfig.NAMESPACE)
    @XmlElementWrapper(name = "bapi-interceptor-classes", namespace = HibersapConfig.NAMESPACE)
    public Set<String> getBapiInterceptorClasses()
    {
        return bapiInterceptorClasses;
    }

    public void setBapiInterceptorClasses( final Collection<String> bapiInterceptorClasses )
    {
        this.bapiInterceptorClasses.clear();
        this.bapiInterceptorClasses.addAll( bapiInterceptorClasses );
    }

    public SessionManagerConfig addBapiInterceptorClass( Class<? extends BapiInterceptor> bapiInterceptorClass )
    {
        executionInterceptorClasses.add( bapiInterceptorClass.getName() );
        return this;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( annotatedClasses == null ) ? 0 : annotatedClasses.hashCode() );
        result = prime * result + ( ( context == null ) ? 0 : context.hashCode() );
        result = prime * result + ( ( executionInterceptorClasses == null ) ? 0 : executionInterceptorClasses.hashCode() );
        result = prime * result + ( ( jcaConnectionFactory == null ) ? 0 : jcaConnectionFactory.hashCode() );
        result = prime * result + ( ( jcaConnectionSpecFactory == null ) ? 0 : jcaConnectionSpecFactory.hashCode() );
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        result = prime * result + ( ( properties == null ) ? 0 : properties.hashCode() );
        return result;
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
        {
            return true;
        }
        if ( obj == null )
        {
            return false;
        }
        if ( getClass() != obj.getClass() )
        {
            return false;
        }
        SessionManagerConfig other = (SessionManagerConfig) obj;
        if ( annotatedClasses == null )
        {
            if ( other.annotatedClasses != null )
            {
                return false;
            }
        }
        else if ( !annotatedClasses.equals( other.annotatedClasses ) )
        {
            return false;
        }
        if ( context == null )
        {
            if ( other.context != null )
            {
                return false;
            }
        }
        else if ( !context.equals( other.context ) )
        {
            return false;
        }
        if ( executionInterceptorClasses == null )
        {
            if ( other.executionInterceptorClasses != null )
            {
                return false;
            }
        }
        else if ( !executionInterceptorClasses.equals( other.executionInterceptorClasses ) )
        {
            return false;
        }
        if ( jcaConnectionFactory == null )
        {
            if ( other.jcaConnectionFactory != null )
            {
                return false;
            }
        }
        else if ( !jcaConnectionFactory.equals( other.jcaConnectionFactory ) )
        {
            return false;
        }
        if ( jcaConnectionSpecFactory == null )
        {
            if ( other.jcaConnectionSpecFactory != null )
            {
                return false;
            }
        }
        else if ( !jcaConnectionSpecFactory.equals( other.jcaConnectionSpecFactory ) )
        {
            return false;
        }
        if ( name == null )
        {
            if ( other.name != null )
            {
                return false;
            }
        }
        else if ( !name.equals( other.name ) )
        {
            return false;
        }
        if ( properties == null )
        {
            if ( other.properties != null )
            {
                return false;
            }
        }
        else if ( !properties.equals( other.properties ) )
        {
            return false;
        }
        return true;
    }
}