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
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"executionInterceptorClasses"})
public final class ExecutionInterceptorClasses implements Serializable {

    @XmlElement(name = "execution-interceptor-class")
    protected List<String> executionInterceptorClasses = new ArrayList<String>();

    public List<String> getExecutionInterceptorClasses() {
        return this.executionInterceptorClasses;
    }

    public void add( final String interceptorClassName ) {
        executionInterceptorClasses.add( interceptorClassName );
    }

    @Override
    public boolean equals( final Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }

        ExecutionInterceptorClasses that = (ExecutionInterceptorClasses) o;

        if ( executionInterceptorClasses != null ?
             !executionInterceptorClasses.equals( that.executionInterceptorClasses ) :
             that.executionInterceptorClasses != null ) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return executionInterceptorClasses != null ? executionInterceptorClasses.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ExecutionInterceptorClasses{" +
                "executionInterceptorClasses=" + executionInterceptorClasses +
                '}';
    }
}
