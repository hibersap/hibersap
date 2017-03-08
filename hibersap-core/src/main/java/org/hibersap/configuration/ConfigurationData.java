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

package org.hibersap.configuration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.interceptor.BapiInterceptor;
import org.hibersap.interceptor.ExecutionInterceptor;
import org.hibersap.mapping.model.BapiMapping;

/**
 * Data object to transport configuration data from Configuration to SessionManager when building the latter.
 */
public class ConfigurationData {

    private final SessionManagerConfig sessionManagerConfig;

    private final Map<String, BapiMapping> bapiMappingsForClass = new HashMap<String, BapiMapping>();

    private final Set<ExecutionInterceptor> executionInterceptors = new HashSet<ExecutionInterceptor>();

    private final Set<BapiInterceptor> bapiInterceptors = new HashSet<BapiInterceptor>();

    ConfigurationData( final SessionManagerConfig sessionManagerConfig ) {
        this.sessionManagerConfig = sessionManagerConfig;
    }

    public SessionManagerConfig getSessionManagerConfig() {
        return sessionManagerConfig;
    }

    public void addExecutionInterceptors( final Set<ExecutionInterceptor> executionInterceptors ) {
        this.executionInterceptors.addAll( executionInterceptors );
    }

    public void addBapiInterceptors( final Set<BapiInterceptor> bapiInterceptors ) {
        this.bapiInterceptors.addAll( bapiInterceptors );
    }

    public void addBapiMappingsForClass( final Map<String, BapiMapping> bapiMappings ) {
        bapiMappingsForClass.putAll( bapiMappings );
    }

    public Set<ExecutionInterceptor> getExecutionInterceptors() {
        return executionInterceptors;
    }

    public Set<BapiInterceptor> getBapiInterceptors() {
        return bapiInterceptors;
    }

    public Map<String, BapiMapping> getBapiMappingsForClass() {
        return bapiMappingsForClass;
    }
}
