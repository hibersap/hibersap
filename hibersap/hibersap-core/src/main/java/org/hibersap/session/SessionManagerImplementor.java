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

package org.hibersap.session;

import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.conversion.ConverterCache;
import org.hibersap.interceptor.BapiInterceptor;
import org.hibersap.interceptor.ExecutionInterceptor;
import org.hibersap.mapping.model.BapiMapping;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * The client's interface to the SessionManager. A SessionManager is used to create Hibersap
 * sessions.
 *
 * @author Carsten Erker
 */
public interface SessionManagerImplementor extends Serializable
{
    /**
     * Returns this SessionManager's BapiMappings.
     *
     * @return A Map with the Class as key and the corresponding BapiMapping object as value.
     */
    Map<Class<?>, BapiMapping> getBapiMappings();

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
     * Returns this SessionManager's Execution interceptors
     *
     * @return A Set with the ExecutionInterceptor instances.
     */
    Set<ExecutionInterceptor> getExecutionInterceptors();

    /**
     * Returns this SessionManager's Bapi interceptors
     *
     * @return A Set with the BapiInterceptor instances.
     */
    Set<BapiInterceptor> getBapiInterceptors();
}
