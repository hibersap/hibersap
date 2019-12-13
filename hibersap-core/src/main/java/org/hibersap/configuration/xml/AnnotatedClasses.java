/*
 * Copyright (c) 2008-2019 akquinet tech@spree GmbH
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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"annotatedClasses"})
public final class AnnotatedClasses implements Serializable {

    @XmlElement(name = "annotated-class")
    protected List<String> annotatedClasses = new ArrayList<>();

    public List<String> getAnnotatedClasses() {
        return this.annotatedClasses;
    }

    public void add(final String annotatedClassName) {
        annotatedClasses.add(annotatedClassName);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AnnotatedClasses that = (AnnotatedClasses) o;

        return annotatedClasses != null ? annotatedClasses.equals(that.annotatedClasses) : that.annotatedClasses == null;
    }

    @Override
    public int hashCode() {
        return annotatedClasses != null ? annotatedClasses.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "AnnotatedClasses{" +
                "annotatedClasses=" + annotatedClasses +
                '}';
    }
}
