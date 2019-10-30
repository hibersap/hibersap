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

package org.hibersap.it.jco;

import org.hibersap.annotations.Parameter;

public class BapiDescription {

    @Parameter("OBJECTNAME")
    private String businessObjectName;

    @Parameter("BO_TEXT")
    private String businessObjectDescription;

    @Parameter("BAPINAME")
    private String bapiName;

    @Parameter("BAPI_TEXT")
    private String bapiDescription;

    @Parameter("ABAPNAME")
    private String abapName;

    @SuppressWarnings({"UnusedDeclaration"})
    private BapiDescription() {
        // for Hibersap
    }

    BapiDescription(final String businessObjectName,
                    final String businessObjectDescription,
                    final String bapiName,
                    final String bapiDescription,
                    final String abapName) {
        this.businessObjectName = businessObjectName;
        this.businessObjectDescription = businessObjectDescription;
        this.bapiName = bapiName;
        this.bapiDescription = bapiDescription;
        this.abapName = abapName;
    }

    public String getBusinessObjectName() {
        return businessObjectName;
    }

    public String getBusinessObjectDescription() {
        return businessObjectDescription;
    }

    public String getBapiName() {
        return bapiName;
    }

    public String getBapiDescription() {
        return bapiDescription;
    }

    public String getAbapName() {
        return abapName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BapiDescription that = (BapiDescription) o;

        if (abapName != null ? !abapName.equals(that.abapName) : that.abapName != null) {
            return false;
        }
        if (bapiDescription != null ? !bapiDescription.equals(that.bapiDescription) : that.bapiDescription != null) {
            return false;
        }
        if (bapiName != null ? !bapiName.equals(that.bapiName) : that.bapiName != null) {
            return false;
        }
        if (businessObjectDescription != null ? !businessObjectDescription.equals(that.businessObjectDescription) :
                that.businessObjectDescription != null) {
            return false;
        }
        if (businessObjectName != null ? !businessObjectName.equals(that.businessObjectName) :
                that.businessObjectName != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = businessObjectName != null ? businessObjectName.hashCode() : 0;
        result = 31 * result + (businessObjectDescription != null ? businessObjectDescription.hashCode() : 0);
        result = 31 * result + (bapiName != null ? bapiName.hashCode() : 0);
        result = 31 * result + (bapiDescription != null ? bapiDescription.hashCode() : 0);
        result = 31 * result + (abapName != null ? abapName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BapiDescription{" +
                "businessObjectName='" + businessObjectName + '\'' +
                ", businessObjectDescription='" + businessObjectDescription + '\'' +
                ", bapiName='" + bapiName + '\'' +
                ", bapiDescription='" + bapiDescription + '\'' +
                ", abapName='" + abapName + '\'' +
                '}';
    }
}
