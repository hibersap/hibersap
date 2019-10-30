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

import java.util.Map;
import java.util.Set;
import org.hibersap.ConfigurationException;
import org.hibersap.MappingException;
import org.hibersap.bapi.BapiTransactionCommit;
import org.hibersap.interceptor.ExecutionInterceptor;
import org.hibersap.interceptor.impl.SapErrorInterceptor;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.session.SessionManager;
import org.hibersap.session.SessionManagerImpl;
import org.junit.Test;
import static org.fest.assertions.Assertions.assertThat;

public class AnnotationConfigurationTest {

    private static final Class<BapiTransactionCommit> BAPI_CLASS = BapiTransactionCommit.class;

    private AnnotationConfiguration configuration = new AnnotationConfiguration();

    @Test
    public void addsSapErrorInterceptorToSessionManagerPerDefault() {
        SessionManagerImpl sessionManager = configureAndBuildSessionManager();

        final Set<ExecutionInterceptor> interceptors = sessionManager.getExecutionInterceptors();

        assertThat(interceptors.toArray()).hasAtLeastOneElementOfType(SapErrorInterceptor.class);
    }

    @Test
    public void addsAnnotatedClassToSessionManager() {
        configuration.getSessionManagerConfig().addAnnotatedClass(BAPI_CLASS.getName());

        SessionManagerImpl sessionManager = configureAndBuildSessionManager();

        Map<String, BapiMapping> bapiMappings = sessionManager.getBapiMappings();
        assertThat(bapiMappings).hasSize(1);
        assertThat(bapiMappings.get(BAPI_CLASS.getName())).isNotNull();
    }

    @Test(expected = MappingException.class)
    public void throwsMappingExceptionWhenClassWasAddedThatIsNotAnnotated() {
        configuration.getSessionManagerConfig().addAnnotatedClass(Object.class);
        configureAndBuildSessionManager();
    }

    @Test(expected = ConfigurationException.class)
    public void throwsConfigurationExceptionWhenBapiClassWasAddedThatIsNotAnnotated() {
        configuration.getSessionManagerConfig().getAnnotatedClasses().add("does.not.Exist");
        configureAndBuildSessionManager();
    }

    private SessionManagerImpl configureAndBuildSessionManager() {
        configuration.getSessionManagerConfig().setContext(DummyContext.class.getName());
        SessionManager sessionManager = configuration.buildSessionManager();
        return (SessionManagerImpl) sessionManager;
    }
}
