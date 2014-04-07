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

package org.hibersap.configuration;

import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.interceptor.BapiInterceptor;
import org.hibersap.interceptor.ExecutionInterceptor;
import org.hibersap.mapping.model.BapiMapping;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Data object to transport configuration data from Configuration to SessionManager when building the latter.
 */
public class ConfigurationData
{
    private final SessionManagerConfig sessionManagerConfig;

    private final Map<Class<?>, BapiMapping> bapiMappingsForClass = new HashMap<Class<?>, BapiMapping>();

    private final Set<ExecutionInterceptor> executionInterceptors = new HashSet<ExecutionInterceptor>();

    private final Set<BapiInterceptor> bapiInterceptors = new HashSet<BapiInterceptor>();

    ConfigurationData( SessionManagerConfig sessionManagerConfig )
    {
        this.sessionManagerConfig = sessionManagerConfig;
    }

    public SessionManagerConfig getSessionManagerConfig()
    {
        return sessionManagerConfig;
    }

    public void setExecutionInterceptors( Set<ExecutionInterceptor> executionInterceptors )
    {
        this.executionInterceptors.clear();
        this.executionInterceptors.addAll( executionInterceptors );
    }

    public void setBapiInterceptors( Set<BapiInterceptor> bapiInterceptors )
    {
        this.bapiInterceptors.clear();
        this.bapiInterceptors.addAll( bapiInterceptors );
    }

    public void setBapiMappingsForClass( Map<Class<?>, BapiMapping> bapiMappings )
    {
        this.bapiMappingsForClass.clear();
        bapiMappingsForClass.putAll( bapiMappings );
    }

    public Set<ExecutionInterceptor> getExecutionInterceptors()
    {
        return executionInterceptors;
    }

    public Set<BapiInterceptor> getBapiInterceptors()
    {
        return bapiInterceptors;
    }

    public Map<Class<?>, BapiMapping> getBapiMappingsForClass()
    {
        return bapiMappingsForClass;
    }
}
