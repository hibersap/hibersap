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
import org.hibersap.interceptor.impl.SapErrorInterceptor;
import org.hibersap.session.Context;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hibersap.mapping.ReflectionHelper.createInstances;
import static org.hibersap.validation.BeanValidationActivator.activateBeanValidation;

public class ConfigurationHelper
{
    private ConfigurationHelper()
    {
        // Utility class with static methods
    }

    public static Context createContext( SessionManagerConfig sessionManagerConfig )
    {
        return ContextFactory.create( sessionManagerConfig );
    }

    public static Set<ExecutionInterceptor> createExecutionInterceptors( SessionManagerConfig sessionManagerConfig )
    {
        final List<String> classNames = sessionManagerConfig.getExecutionInterceptorClasses();

        final Set<ExecutionInterceptor> executionInterceptors = new HashSet<ExecutionInterceptor>();
        executionInterceptors.addAll( createInstances( classNames, ExecutionInterceptor.class ) );
        executionInterceptors.add( new SapErrorInterceptor() );

        return executionInterceptors;
    }

    public static Set<BapiInterceptor> createBapiInterceptors( SessionManagerConfig sessionManagerConfig )
    {
        final List<String> classNames = sessionManagerConfig.getBapiInterceptorClasses();

        final Set<BapiInterceptor> bapiInterceptors = new HashSet<BapiInterceptor>();
        bapiInterceptors.addAll( createInstances( classNames, BapiInterceptor.class ) );
        activateBeanValidation( bapiInterceptors, sessionManagerConfig );

        return bapiInterceptors;
    }
}
