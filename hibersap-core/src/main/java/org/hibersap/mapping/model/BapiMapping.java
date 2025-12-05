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

package org.hibersap.mapping.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Generated;
import org.hibersap.MappingException;
import org.jspecify.annotations.Nullable;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * This class is the framework internal representation of mappings between SAP function modules
 * (BAPIs) and Java classes.
 *
 * @author Carsten Erker
 */
public final class BapiMapping implements Serializable {

    private static final long serialVersionUID = 6716958693316907614L;

    private final String bapiName;
    private final Set<ParameterMapping> importParams = new HashSet<>();
    private final Set<ParameterMapping> exportParams = new HashSet<>();
    private final Set<ParameterMapping> changingParams = new HashSet<>();
    private final Set<TableMapping> tableParams = new HashSet<>();
    private final ErrorHandling errorHandling;
    private final Class<?> associatedClass;

    public BapiMapping(final Class<?> associatedClass, @Nullable final String bapiName, final ErrorHandling errorHandling) {
        if (isBlank(bapiName)) {
            throw new MappingException("Bapi name for class " + associatedClass.getName() + " is empty");
        }
        this.associatedClass = associatedClass;
        this.bapiName = bapiName;
        this.errorHandling = errorHandling;
    }

    public void addExportParameter(final ParameterMapping parameter) {
        exportParams.add(parameter);
    }

    public void addChangingParameter(final ParameterMapping parameter) {
        changingParams.add(parameter);
    }

    public void addImportParameter(final ParameterMapping parameter) {
        importParams.add(parameter);
    }

    public void addTableParameter(final TableMapping parameter) {
        tableParams.add(parameter);
    }

    public Class<?> getAssociatedClass() {
        return associatedClass;
    }

    public String getBapiName() {
        return bapiName;
    }

    public ErrorHandling getErrorHandling() {
        return this.errorHandling;
    }

    public Set<ParameterMapping> getExportParameters() {
        return exportParams;
    }

    public Set<ParameterMapping> getChangingParameters() {
        return changingParams;
    }

    public Set<ParameterMapping> getImportParameters() {
        return importParams;
    }

    public Set<TableMapping> getTableParameters() {
        return tableParams;
    }

    public Set<ParameterMapping> getAllParameters() {
        HashSet<ParameterMapping> parameters = new HashSet<>();
        parameters.addAll(importParams);
        parameters.addAll(exportParams);
        parameters.addAll(changingParams);
        parameters.addAll(tableParams);
        return parameters;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[BAPI: " + bapiName + ", MappedClass: " + associatedClass + "]";
    }

    @Override
    @Generated("IntelliJ")
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BapiMapping that = (BapiMapping) o;

        if (!bapiName.equals(that.bapiName)) {
            return false;
        }
        if (!importParams.equals(that.importParams)) {
            return false;
        }
        if (!exportParams.equals(that.exportParams)) {
            return false;
        }
        if (!changingParams.equals(that.changingParams)) {
            return false;
        }
        if (!tableParams.equals(that.tableParams)) {
            return false;
        }
        if (!errorHandling.equals(that.errorHandling)) {
            return false;
        }
        return associatedClass.equals(that.associatedClass);
    }

    @Override
    @Generated("IntelliJ")
    public int hashCode() {
        int result = bapiName.hashCode();
        result = 31 * result + importParams.hashCode();
        result = 31 * result + exportParams.hashCode();
        result = 31 * result + changingParams.hashCode();
        result = 31 * result + tableParams.hashCode();
        result = 31 * result + errorHandling.hashCode();
        result = 31 * result + associatedClass.hashCode();
        return result;
    }
}
