/*
 * Copyright (c) 2008-2017 akquinet tech@spree GmbH
 *
 * This file is part of Hibersap.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hibersap.configuration.xml;

import java.io.Serializable;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.interceptor.BapiInterceptor;
import org.hibersap.interceptor.ExecutionInterceptor;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "context",
        "jcaConnectionFactory",
        "jcaConnectionSpecFactory",
        "properties",
        "annotatedClasses",
        "executionInterceptorClasses",
        "bapiInterceptorClasses",
        "validationMode"
})
public final class SessionManagerConfig implements Serializable {

    @XmlTransient
    private static final long serialVersionUID = 270142113574399232L;

    @XmlTransient
    private static final Log LOG = LogFactory.getLog(SessionManagerConfig.class);

    @XmlAttribute(required = true)
    protected String name;

    @XmlElement(name = "context")
    protected String context = "org.hibersap.execution.jco.JCoContext";

    @XmlElement(name = "properties")
    protected Properties properties = new Properties();

    @XmlElement(name = "annotated-classes")
    protected AnnotatedClasses annotatedClasses = new AnnotatedClasses();

    @XmlElement(name = "execution-interceptor-classes")
    protected ExecutionInterceptorClasses executionInterceptorClasses = new ExecutionInterceptorClasses();

    @XmlElement(name = "bapi-interceptor-classes")
    protected BapiInterceptorClasses bapiInterceptorClasses = new BapiInterceptorClasses();

    @XmlElement(name = "jca-connection-factory")
    protected String jcaConnectionFactory;

    @XmlElement(name = "jca-connectionspec-factory")
    protected String jcaConnectionSpecFactory = "org.hibersap.execution.jca.cci.SapBapiJcaAdapterConnectionSpecFactory";

    @XmlElement(name = "validation-mode")
    protected ValidationMode validationMode = ValidationMode.AUTO;

    public SessionManagerConfig() {
    }

    public SessionManagerConfig(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public SessionManagerConfig setName(final String name) {
        this.name = name;
        return this;
    }

    public String getContext() {
        return context;
    }

    public SessionManagerConfig setContext(final String context) {
        this.context = context;
        return this;
    }

    public String getJcaConnectionFactory() {
        return jcaConnectionFactory;
    }

    public SessionManagerConfig setJcaConnectionFactory(final String jcaConnectionFactory) {
        this.jcaConnectionFactory = jcaConnectionFactory;
        return this;
    }

    public String getJcaConnectionSpecFactory() {
        return jcaConnectionSpecFactory;
    }

    public SessionManagerConfig setJcaConnectionSpecFactory(final String jcaConnectionSpecFactory) {
        this.jcaConnectionSpecFactory = jcaConnectionSpecFactory;
        return this;
    }

    public List<Property> getProperties() {
        return properties.getProperties();
    }

    public SessionManagerConfig setProperties(final List<Property> properties) {
        LOG.debug("SETPROPS");
        this.properties.setProperties(properties);
        return this;
    }

    public List<String> getAnnotatedClasses() {
        return annotatedClasses.getAnnotatedClasses();
    }

    public void setAnnotatedClasses(final List<String> annotatedClasses) {
        this.annotatedClasses.getAnnotatedClasses().clear();
        this.annotatedClasses.getAnnotatedClasses().addAll(annotatedClasses);
    }

    public String getProperty(final String propertyName) {
        return properties.getPropertyValue(propertyName);
    }

    public SessionManagerConfig setProperty(final String name, final String value) {
        properties.setProperty(name, value);
        return this;
    }

    /**
     * This does not work in OSGi. Use AnnotationConfiguration.addBapiClasses instead.
     *
     * @param annotatedClass The @Bapi annotated class
     * @return The SessionManagerConfig
     */
    @Deprecated
    public SessionManagerConfig addAnnotatedClass(final Class<?> annotatedClass) {
        annotatedClasses.add(annotatedClass.getName());
        return this;
    }

    public SessionManagerConfig addAnnotatedClass(final String annotatedClassFqn) {
        annotatedClasses.add(annotatedClassFqn);
        return this;
    }

    public ValidationMode getValidationMode() {
        return validationMode;
    }

    public SessionManagerConfig setValidationMode(final ValidationMode validationMode) {
        this.validationMode = validationMode;
        return this;
    }

    public List<String> getExecutionInterceptorClasses() {
        return executionInterceptorClasses.getExecutionInterceptorClasses();
    }

    public SessionManagerConfig addExecutionInterceptorClass(final Class<? extends ExecutionInterceptor> interceptorClass) {
        executionInterceptorClasses.add(interceptorClass.getName());
        return this;
    }

    public List<String> getBapiInterceptorClasses() {
        return bapiInterceptorClasses.getBapiInterceptorClasses();
    }

    public SessionManagerConfig addBapiInterceptorClass(final Class<? extends BapiInterceptor> bapiInterceptorClass) {
        executionInterceptorClasses.add(bapiInterceptorClass.getName());
        return this;
    }

    @Override
    @Generated("IntelliJ IDEA")
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (context != null ? context.hashCode() : 0);
        result = 31 * result + (properties != null ? properties.hashCode() : 0);
        result = 31 * result + (annotatedClasses != null ? annotatedClasses.hashCode() : 0);
        result = 31 * result + (executionInterceptorClasses != null ? executionInterceptorClasses.hashCode() : 0);
        result = 31 * result + (bapiInterceptorClasses != null ? bapiInterceptorClasses.hashCode() : 0);
        result = 31 * result + (jcaConnectionFactory != null ? jcaConnectionFactory.hashCode() : 0);
        result = 31 * result + (jcaConnectionSpecFactory != null ? jcaConnectionSpecFactory.hashCode() : 0);
        result = 31 * result + (validationMode != null ? validationMode.hashCode() : 0);
        return result;
    }

    @Override
    @Generated("IntelliJ IDEA")
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SessionManagerConfig that = (SessionManagerConfig) o;

        if (annotatedClasses != null ? !annotatedClasses.equals(that.annotatedClasses) :
                that.annotatedClasses != null) {
            return false;
        }
        if (bapiInterceptorClasses != null ? !bapiInterceptorClasses.equals(that.bapiInterceptorClasses) :
                that.bapiInterceptorClasses != null) {
            return false;
        }
        if (context != null ? !context.equals(that.context) : that.context != null) {
            return false;
        }
        if (executionInterceptorClasses != null ?
                !executionInterceptorClasses.equals(that.executionInterceptorClasses) :
                that.executionInterceptorClasses != null) {
            return false;
        }
        if (jcaConnectionFactory != null ? !jcaConnectionFactory.equals(that.jcaConnectionFactory) :
                that.jcaConnectionFactory != null) {
            return false;
        }
        if (jcaConnectionSpecFactory != null ? !jcaConnectionSpecFactory.equals(that.jcaConnectionSpecFactory) :
                that.jcaConnectionSpecFactory != null) {
            return false;
        }
        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        if (properties != null ? !properties.equals(that.properties) : that.properties != null) {
            return false;
        }
        if (validationMode != that.validationMode) {
            return false;
        }

        return true;
    }

    public String toString() {
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