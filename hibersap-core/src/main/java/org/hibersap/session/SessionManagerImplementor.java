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

package org.hibersap.session;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.conversion.ConverterCache;
import org.hibersap.interceptor.BapiInterceptor;
import org.hibersap.interceptor.ExecutionInterceptor;
import org.hibersap.mapping.model.BapiMapping;

/**
 * The internal interface to the SessionManager. A SessionManager is used to create Hibersap
 * sessions.
 *
 * @author Carsten Erker
 */
public interface SessionManagerImplementor extends Serializable {

    /**
     * Returns this SessionManager's BapiMappings.
     *
     * @return A Map with the Class as key and the corresponding BapiMapping object as value.
     */
    Map<String, BapiMapping> getBapiMappings();

    /**
     * Returns this SessionManager's Converter cache.
     *
     * @return The ConverterCache instance.
     */
    ConverterCache getConverterCache();

    /**
     * Returns this SessionManager's Configuration.
     *
     * @return The SessionManagerConfig instance.
     */
    SessionManagerConfig getConfig();

    /**
     * Returns this SessionManager's Context class.
     *
     * @return The Context instance.
     */
    Context getContext();

    /**
     * Returns this SessionManager's Execution interceptors.
     *
     * @return A Set with the ExecutionInterceptor instances.
     */
    Set<ExecutionInterceptor> getExecutionInterceptors();

    /**
     * Returns this SessionManager's Bapi interceptors.
     *
     * @return A Set with the BapiInterceptor instances.
     */
    Set<BapiInterceptor> getBapiInterceptors();
}
