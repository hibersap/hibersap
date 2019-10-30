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

package org.hibersap.session;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;
import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.configuration.DummyContext;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.conversion.BooleanConverter;
import org.hibersap.interceptor.BapiInterceptor;
import org.hibersap.interceptor.ExecutionInterceptor;
import org.hibersap.interceptor.impl.SapErrorInterceptor;
import org.hibersap.validation.BeanValidationInterceptor;
import org.junit.Before;
import org.junit.Test;
import static org.fest.assertions.Assertions.assertThat;

public class SessionManagerImplTest {

    private SessionManagerImpl sessionManager;

    @Before
    public void createSessionManager() {
        SessionManagerConfig smConfig = new SessionManagerConfig("name").setContext(DummyContext.class.getName());

        AnnotationConfiguration config = new AnnotationConfiguration(smConfig);

        sessionManager = (SessionManagerImpl) config.buildSessionManager();
    }

    @Test
    public void hasConfiguredAndStandardValuesWhenCreated() throws Exception {
        assertThat(sessionManager.getContext()).isInstanceOf(DummyContext.class);
        assertThat(sessionManager.getConfig().getName()).isEqualTo("name");
        assertThat(sessionManager.getConverterCache()).isNotNull();
        assertThat(sessionManager.getBapiMappings()).isNotNull();
        assertThat(sessionManager.getExecutionInterceptors()).isNotNull();
    }

    @Test
    public void hasSameConfigurationAfterDeserialization() throws Exception {
        SessionManagerImpl managerRead = serializeAndDeserializeSessionManager();

        assertThat(managerRead.getConfig()).isEqualTo(sessionManager.getConfig());
    }

    @Test
    public void hasConverterCacheAfterDeserialization() throws Exception {
        SessionManagerImpl managerRead = serializeAndDeserializeSessionManager();

        assertThat(managerRead.getConverterCache()).isNotNull();
    }

    @Test
    public void hasSameContextAfterDeserialization() throws Exception {
        SessionManagerImpl managerRead = serializeAndDeserializeSessionManager();

        assertThat(managerRead.getContext()).isNotNull();
        assertThat(managerRead.getContext()).isEqualTo(sessionManager.getContext());
    }

    @Test
    public void hasSameBapiInterceptorsAfterDeserialization() throws Exception {
        SessionManagerImpl managerRead = serializeAndDeserializeSessionManager();

        final Set<BapiInterceptor> bapiInterceptors = managerRead.getBapiInterceptors();
        assertThat(bapiInterceptors).isNotNull();
        assertThat(bapiInterceptors).hasSize(1);
        assertThat(bapiInterceptors.iterator().next()).isInstanceOf(BeanValidationInterceptor.class);
    }

    @Test
    public void hasSameExecutionInterceptorsAfterDeserialization() throws Exception {
        SessionManagerImpl managerRead = serializeAndDeserializeSessionManager();

        final Set<ExecutionInterceptor> executionInterceptors = managerRead.getExecutionInterceptors();
        assertThat(executionInterceptors).isNotNull();
        assertThat(executionInterceptors).hasSize(1);
        assertThat(executionInterceptors.iterator().next()).isInstanceOf(SapErrorInterceptor.class);
    }

    @Test
    public void isClosedAfterDeserializationWhenItWasClosedBefore() throws Exception {
        sessionManager.close();
        SessionManagerImpl managerRead = serializeAndDeserializeSessionManager();

        assertThat(managerRead.isClosed()).isTrue();
    }

    private SessionManagerImpl serializeAndDeserializeSessionManager() throws IOException, ClassNotFoundException {
        // Converter gets created lazily on first access
        if (!sessionManager.isClosed()) {
            sessionManager.getConverterCache().getConverter(BooleanConverter.class);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(sessionManager);
        out.close();

        ByteArrayInputStream bain = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bain);
        SessionManagerImpl managerRead = (SessionManagerImpl) in.readObject();
        in.close();
        return managerRead;
    }
}
