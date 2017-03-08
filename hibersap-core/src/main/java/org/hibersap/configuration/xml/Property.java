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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class Property implements Serializable {

    @XmlAttribute(required = true)
    protected String name;
    @XmlAttribute(required = true)
    protected String value;

    @SuppressWarnings({"UnusedDeclaration"})
    public Property() {
    }

    public Property( final String name, final String value ) {
        this.name = name;
        this.value = value;
    }

    /**
     * Gets the value of the name properties.
     *
     * @return possible object is
     * {@link String }
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name properties.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setName( final String value ) {
        this.name = value;
    }

    /**
     * Gets the value of the value properties.
     *
     * @return possible object is
     * {@link String }
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value properties.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setValue( final String value ) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "(" + name + " => " + value + ")";
    }

    @Override
    public boolean equals( final Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }

        Property property = (Property) o;

        if ( name != null ? !name.equals( property.name ) : property.name != null ) {
            return false;
        }
        if ( value != null ? !value.equals( property.value ) : property.value != null ) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + ( value != null ? value.hashCode() : 0 );
        return result;
    }
}
