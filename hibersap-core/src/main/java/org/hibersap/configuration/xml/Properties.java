/*
 * Copyright (c) 2008-2025 tech@spree GmbH
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
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"properties"})
public final class Properties implements Serializable {

    @XmlElement(name = "property")
    protected List<Property> properties = new ArrayList<>();

    public List<Property> getProperties() {
        return this.properties;
    }

    public void setProperties(final List<Property> properties) {
        this.properties = properties;
    }

    public boolean contains(final Property property) {
        return properties.contains(property);
    }

    public boolean remove(final Property property) {
        return properties.remove(property);
    }

    public void add(final Property property) {
        removePropertyWithName(property.getName());
        properties.add(property);
    }

    public int size() {
        return properties.size();
    }

    public void setProperty(final String name, final String value) {
        removePropertyWithName(name);
        properties.add(new Property(name, value));
    }

    private void removePropertyWithName(final String name) {
        for (Property property : properties) {
            if (property.getName().equals(name)) {
                properties.remove(property);
                break;
            }
        }
    }

    public String getPropertyValue(final String name) {
        for (Property property : properties) {
            if (property.getName().equals(name)) {
                return property.getValue();
            }
        }
        return null;
    }

    @Override
    @Generated("")
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Properties that = (Properties) o;

        if (properties != null ? !properties.equals(that.properties) : that.properties != null) {
            return false;
        }

        return true;
    }

    @Override
    @Generated("")
    public int hashCode() {
        return properties != null ? properties.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Properties{" +
                "properties=" + properties +
                '}';
    }
}
