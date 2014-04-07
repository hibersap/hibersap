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

package org.hibersap.interceptor;

import java.io.Serializable;
import java.util.Map;

import org.hibersap.mapping.model.BapiMapping;

/**
 * Interceptors can be registered for a SessionManager to intercept function calls to SAP.
 * This interceptor offers access to low-level information about the function parameters
 * and the mapping.
 *
 * @author Carsten Erker
 */
public interface ExecutionInterceptor
    extends Serializable
{
    void beforeExecution( BapiMapping bapiMapping, Map<String, Object> functionMap );

    void afterExecution( BapiMapping bapiMapping, Map<String, Object> functionMap );
}
