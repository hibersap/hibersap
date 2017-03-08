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

import java.util.Map;
import java.util.Set;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.interceptor.BapiInterceptor;
import org.hibersap.interceptor.ExecutionInterceptor;
import org.hibersap.interceptor.impl.SapErrorInterceptor;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.session.SessionManagerImplementor;
import org.hibersap.validation.BeanValidationInterceptor;
import org.junit.Before;
import org.junit.Test;
import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Carsten Erker
 */
public class ConfigurationTest {

    private SessionManagerConfig sessionManagerConfig;
    private SessionManagerImplementor sessionManager;

    @Before
    public void createConfiguration() {
        Configuration configuration = new Configuration( "TEST" ) {
            // create instance of abstract class
        };
        sessionManagerConfig = configuration.getSessionManagerConfig();
        sessionManagerConfig.setContext( DummyContext.class.getName() );
        sessionManager = (SessionManagerImplementor) configuration.buildSessionManager();
    }

    @Test
    public void overwritesContextClass() throws Exception {
        sessionManagerConfig.setContext( "test" );

        assertThat( sessionManagerConfig.getContext() ).isEqualTo( "test" );
    }

    @Test
    public void overwritesProperties()
            throws Exception {
        sessionManagerConfig.setProperty( "jco.client.user", "test" );

        assertThat( sessionManagerConfig.getProperty( "jco.client.user" ) ).isEqualTo( "test" );
    }

    @Test
    public void createsBapiInterceptorsFromXmlConfig() {
        final Set<BapiInterceptor> interceptors = sessionManager.getBapiInterceptors();

        assertThat( interceptors.toArray() ).hasSize( 2 )
                                            .hasAtLeastOneElementOfType( BapiInterceptorDummy.class )
                                            .hasAtLeastOneElementOfType( BeanValidationInterceptor.class );
    }

    @Test
    public void initializesStandardExecutionInterceptorsAutomatically() {
        final Set<ExecutionInterceptor> interceptors = sessionManager.getExecutionInterceptors();

        assertThat( interceptors.toArray() ).hasAtLeastOneElementOfType( SapErrorInterceptor.class );
    }

    @Test
    public void createsExecutionInterceptorsFromXmlConfig() {
        final Set<ExecutionInterceptor> interceptors = sessionManager.getExecutionInterceptors();
        assertThat( interceptors ).hasSize( 2 );
        assertThat( interceptors.toArray() ).hasAtLeastOneElementOfType( ExecutionInterceptorDummy.class );
    }

    public static class ExecutionInterceptorDummy implements ExecutionInterceptor {

        public void afterExecution( BapiMapping bapiMapping, Map<String, Object> functionMap ) {
            // dummy
        }

        public void beforeExecution( BapiMapping bapiMapping, Map<String, Object> functionMap ) {
            // dummy
        }
    }

    public static class BapiInterceptorDummy implements BapiInterceptor {

        public void beforeExecution( Object bapiObject ) {
            // dummy
        }

        public void afterExecution( Object bapiObject ) {
            // dummy
        }
    }
}
