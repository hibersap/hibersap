/*
 * Copyright (c) 2008-2012 akquinet tech@spree GmbH
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
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.List;

@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( name = "", propOrder = {
        "context",
        "jcaConnectionFactory",
        "jcaConnectionSpecFactory",
        "properties",
        "annotatedClasses",
        "executionInterceptorClasses",
        "bapiInterceptorClasses",
        "validationMode"
} )
public final class SessionManagerConfig implements Serializable
{
    @XmlTransient
    private static final long serialVersionUID = 270142113574399232L;

    @XmlTransient
    private static final Log LOG = LogFactory.getLog( SessionManagerConfig.class );


    @XmlAttribute( required = true )
    protected String name;

    @XmlElement( name = "context" )
    protected String context = "org.hibersap.execution.jco.JCoContext";

    @XmlElement( name = "properties" )
    protected Properties properties = new Properties();

    @XmlElement( name = "annotated-classes" )
    protected AnnotatedClasses annotatedClasses = new AnnotatedClasses();

    @XmlElement( name = "execution-interceptor-classes" )
    protected ExecutionInterceptorClasses executionInterceptorClasses = new ExecutionInterceptorClasses();

    @XmlElement( name = "bapi-interceptor-classes" )
    protected BapiInterceptorClasses bapiInterceptorClasses = new BapiInterceptorClasses();

    @XmlElement( name = "jca-connection-factory" )
    protected String jcaConnectionFactory;

    @XmlElement( name = "jca-connectionspec-factory" )
    protected String jcaConnectionSpecFactory = "org.hibersap.execution.jca.cci.SapBapiJcaAdapterConnectionSpecFactory";

    @XmlElement( name = "validation-mode" )
//    @XmlJavaTypeAdapter( CollapsedStringAdapter.class )
    protected ValidationMode validationMode = ValidationMode.AUTO;

    public SessionManagerConfig()
    {
    }

    public SessionManagerConfig( final String name )
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public SessionManagerConfig setName( final String name )
    {
        this.name = name;
        return this;
    }

    public String getContext()
    {
        return context;
    }

    public String getJcaConnectionFactory()
    {
        return jcaConnectionFactory;
    }

    public SessionManagerConfig setJcaConnectionFactory( final String jcaConnectionFactory )
    {
        this.jcaConnectionFactory = jcaConnectionFactory;
        return this;
    }

    public String getJcaConnectionSpecFactory()
    {
        return jcaConnectionSpecFactory;
    }

    public SessionManagerConfig setJcaConnectionSpecFactory( final String jcaConnectionSpecFactory )
    {
        this.jcaConnectionSpecFactory = jcaConnectionSpecFactory;
        return this;
    }

    public List<Property> getProperties()
    {
        return properties.getProperties();
    }

    public SessionManagerConfig setProperties( final List<Property> properties )
    {
        LOG.debug( "SETPROPS" );
        this.properties.setProperties( properties );
        return this;
    }

    public List<String> getAnnotatedClasses()
    {
        return annotatedClasses.getAnnotatedClasses();
    }

    public void setAnnotatedClasses( final List<String> annotatedClasses )
    {
        this.annotatedClasses.getAnnotatedClasses().clear();
        this.annotatedClasses.getAnnotatedClasses().addAll( annotatedClasses );
    }

    public String getProperty( final String propertyName )
    {
        return properties.getPropertyValue( propertyName );
    }

    public SessionManagerConfig setContext( final String context )
    {
        this.context = context;
        return this;
    }

    public SessionManagerConfig setProperty( final String name, final String value )
    {
        properties.setProperty( name, value );
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

    public ValidationMode getValidationMode()
    {
        return validationMode;
    }

    public List<String> getExecutionInterceptorClasses()
    {
        return executionInterceptorClasses.getExecutionInterceptorClasses();
    }

    public SessionManagerConfig addExecutionInterceptorClass( Class<? extends ExecutionInterceptor> interceptorClass )
    {
        executionInterceptorClasses.add( interceptorClass.getName() );
        return this;
    }

    public List<String> getBapiInterceptorClasses()
    {
        return bapiInterceptorClasses.getBapiInterceptorClasses();
    }

    public SessionManagerConfig addBapiInterceptorClass( Class<? extends BapiInterceptor> bapiInterceptorClass )
    {
        executionInterceptorClasses.add( bapiInterceptorClass.getName() );
        return this;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }

        SessionManagerConfig that = ( SessionManagerConfig ) o;

        if ( annotatedClasses != null ? !annotatedClasses.equals( that.annotatedClasses ) :
             that.annotatedClasses != null )
        {
            return false;
        }
        if ( bapiInterceptorClasses != null ? !bapiInterceptorClasses.equals( that.bapiInterceptorClasses ) :
             that.bapiInterceptorClasses != null )
        {
            return false;
        }
        if ( context != null ? !context.equals( that.context ) : that.context != null )
        {
            return false;
        }
        if ( executionInterceptorClasses != null ?
             !executionInterceptorClasses.equals( that.executionInterceptorClasses ) :
             that.executionInterceptorClasses != null )
        {
            return false;
        }
        if ( jcaConnectionFactory != null ? !jcaConnectionFactory.equals( that.jcaConnectionFactory ) :
             that.jcaConnectionFactory != null )
        {
            return false;
        }
        if ( jcaConnectionSpecFactory != null ? !jcaConnectionSpecFactory.equals( that.jcaConnectionSpecFactory ) :
             that.jcaConnectionSpecFactory != null )
        {
            return false;
        }
        if ( name != null ? !name.equals( that.name ) : that.name != null )
        {
            return false;
        }
        if ( properties != null ? !properties.equals( that.properties ) : that.properties != null )
        {
            return false;
        }
        if ( validationMode != that.validationMode )
        {
            return false;
        }

        return true;
    }

    public String toString()
    {
        return "SessionManagerConfig{" +
                "annotatedClasses=" + annotatedClasses +
                ", name='" + name + '\'' +
                ", context='" + context + '\'' +
                ", properties=" + properties +
                ", executionInterceptorClasses=" + executionInterceptorClasses +
                ", bapiInterceptorClasses=" + bapiInterceptorClasses +
                ", jcaConnectionFactory='" + jcaConnectionFactory + '\'' +
                ", jcaConnectionSpecFactory='" + jcaConnectionSpecFactory + '\'' +
                ", validationMode=" + validationMode +
                '}';
    }
}