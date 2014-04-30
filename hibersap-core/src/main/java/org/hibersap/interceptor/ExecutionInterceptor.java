/*
 * Copyright (c) 2008-2014 akquinet tech@spree GmbH
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

package org.hibersap.interceptor;

import org.hibersap.mapping.model.BapiMapping;

import java.io.Serializable;
import java.util.Map;

/**
 * Interceptors can be registered for a SessionManager to intercept function calls to SAP.
 * This interceptor offers access to low-level information about the function parameters
 * and the mapping.
 *
 * @author Carsten Erker
 */
public interface ExecutionInterceptor extends Serializable {

    void beforeExecution( BapiMapping bapiMapping, Map<String, Object> functionMap );

    void afterExecution( BapiMapping bapiMapping, Map<String, Object> functionMap );
}
