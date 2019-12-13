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

package org.hibersap.configuration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.interceptor.BapiInterceptor;
import org.hibersap.interceptor.ExecutionInterceptor;
import org.hibersap.interceptor.impl.SapErrorInterceptor;
import org.hibersap.session.Context;
import static org.hibersap.mapping.ReflectionHelper.createInstances;
import static org.hibersap.validation.BeanValidationActivator.activateBeanValidation;

public final class ConfigurationHelper {

    private ConfigurationHelper() {
        // Utility class with static methods
    }

    public static Context createContext(final SessionManagerConfig sessionManagerConfig) {
        return ContextFactory.create(sessionManagerConfig);
    }

    public static Set<ExecutionInterceptor> createExecutionInterceptors(final SessionManagerConfig sessionManagerConfig) {
        final List<String> classNames = sessionManagerConfig.getExecutionInterceptorClasses();

        final Set<ExecutionInterceptor> executionInterceptors
                = new HashSet<>(createInstances(classNames, ExecutionInterceptor.class));
        executionInterceptors.add(new SapErrorInterceptor());

        return executionInterceptors;
    }

    public static Set<BapiInterceptor> createBapiInterceptors(final SessionManagerConfig sessionManagerConfig) {
        final List<String> classNames = sessionManagerConfig.getBapiInterceptorClasses();

        final Set<BapiInterceptor> bapiInterceptors
                = new HashSet<>(createInstances(classNames, BapiInterceptor.class));
        activateBeanValidation(bapiInterceptors, sessionManagerConfig);

        return bapiInterceptors;
    }
}
