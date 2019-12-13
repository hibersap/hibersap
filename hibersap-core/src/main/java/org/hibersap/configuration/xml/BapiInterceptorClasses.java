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
@XmlType(name = "", propOrder = {"bapiInterceptorClasses"})
public final class BapiInterceptorClasses implements Serializable {

    @XmlElement(name = "bapi-interceptor-class")
    protected List<String> bapiInterceptorClasses = new ArrayList<>();

    public List<String> getBapiInterceptorClasses() {
        return this.bapiInterceptorClasses;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BapiInterceptorClasses that = (BapiInterceptorClasses) o;

        return bapiInterceptorClasses != null ?
                bapiInterceptorClasses.equals(that.bapiInterceptorClasses) : that.bapiInterceptorClasses == null;
    }

    @Override
    public int hashCode() {
        return bapiInterceptorClasses != null ? bapiInterceptorClasses.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "BapiInterceptorClasses{" +
                "bapiInterceptorClasses=" + bapiInterceptorClasses +
                '}';
    }
}
