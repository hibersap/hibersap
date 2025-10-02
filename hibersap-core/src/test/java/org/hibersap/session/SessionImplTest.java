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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.hibersap.HibersapException;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.conversion.ConverterCache;
import org.hibersap.execution.Connection;
import org.hibersap.interceptor.BapiInterceptor;
import org.hibersap.interceptor.ExecutionInterceptor;
import org.hibersap.mapping.model.BapiMapping;
import org.jspecify.annotations.Nullable;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SessionImplTest {

    private final BapiInterceptor bapiInterceptor = mock(BapiInterceptor.class);
    private final ExecutionInterceptor executionInterceptor = mock(ExecutionInterceptor.class);

    private final SessionImpl session = new SessionImpl(new SessionManagerStub());

    @Test
    public void bapiInterceptorFromConfigurationIsCalledWhenExecutingFunction() {
        final Object bapiObject = new Object();

        session.execute(bapiObject);

        verify(bapiInterceptor).beforeExecution(bapiObject);
        verify(bapiInterceptor).afterExecution(bapiObject);
    }

    @Test
    public void bapiInterceptorThatIsAddedAtRuntimeAreCalledWhenExecutingFunction() {
        BapiInterceptor myInterceptor = mock(BapiInterceptor.class);
        final Object bapiObject = new Object();

        session.addBapiInterceptor(myInterceptor);
        session.execute(bapiObject);

        verify(myInterceptor).beforeExecution(bapiObject);
        verify(myInterceptor).afterExecution(bapiObject);
    }

    @Test
    public void executionInterceptorsFromConfigurationIsCalledWhenExecutingFunction() {
        final Object bapiObject = new Object();

        session.execute(bapiObject);

        verify(executionInterceptor).beforeExecution(any(), any());
        verify(executionInterceptor).afterExecution(any(), any());
    }

    @Test
    public void executionInterceptorThatIsAddedAtRuntimeAreCalledWhenExecutingFunction() {
        ExecutionInterceptor myInterceptor = mock(ExecutionInterceptor.class);
        final Object bapiObject = new Object();

        session.addExecutionInterceptor(myInterceptor);
        session.execute(bapiObject);

        verify(myInterceptor).beforeExecution(any(), any());
        verify(myInterceptor).beforeExecution(any(), any());
    }

    @SuppressWarnings("unused")
    private class SessionManagerStub implements SessionManagerImplementor {

        public Map<String, BapiMapping> getBapiMappings() {
            final HashMap<String, BapiMapping> mappings = new HashMap<>();
            mappings.put(Object.class.getName(), new BapiMapping(Object.class, "BAPI_NAME", null));
            return mappings;
        }

        public ConverterCache getConverterCache() {
            return new ConverterCache();
        }

        public SessionManagerConfig getConfig() {
            return new SessionManagerConfig();
        }

        public Context getContext() {
            return new ContextStub();
        }

        public @Nullable Session openSession() {
            return null;
        }

        public Set<ExecutionInterceptor> getExecutionInterceptors() {

            return Collections.singleton(executionInterceptor);
        }

        public Set<BapiInterceptor> getBapiInterceptors() {
            return Collections.singleton(bapiInterceptor);
        }
    }

    private static class ContextStub implements Context {

        public void configure(SessionManagerConfig config) throws HibersapException {
        }

        public void close() {
        }

        public Connection getConnection() {
            return new ConnectionStub();
        }
    }

    private static class ConnectionStub implements Connection {

        public void setCredentials(Credentials credentials) {
        }

        public @Nullable Transaction beginTransaction(SessionImplementor session) {
            return null;
        }

        public @Nullable Transaction getTransaction() {
            return null;
        }

        public void execute(BapiMapping bapiMapping, Map<String, Object> functionMap) {
        }

        public void close() {
        }
    }
}