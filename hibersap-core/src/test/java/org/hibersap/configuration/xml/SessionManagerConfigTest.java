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

package org.hibersap.configuration.xml;

import java.util.List;
import org.hibersap.configuration.ConfigurationTest;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SessionManagerConfigTest {

    @Test
    public void testBuild() {
        SessionManagerConfig cfg = new SessionManagerConfig("name")
                .setContext("context")
                .setJcaConnectionFactory("jcaConnectionFactory")
                .setJcaConnectionSpecFactory("jcaConnectionSpecFactory")
                .setName("newName")
                .setProperty("key1", "value1")
                .setProperty("key2", "value2")
                .addAnnotatedClass(String.class)
                .addAnnotatedClass(Integer.class)
                .addExecutionInterceptorClass(ConfigurationTest.ExecutionInterceptorDummy.class)
                .addBapiInterceptorClass(ConfigurationTest.BapiInterceptorDummy.class)
                .setValidationMode(ValidationMode.CALLBACK);

        assertThat(cfg.getContext()).isEqualTo("context");
        assertThat(cfg.getJcaConnectionFactory()).isEqualTo("jcaConnectionFactory");
        assertThat(cfg.getJcaConnectionSpecFactory()).isEqualTo("jcaConnectionSpecFactory");
        assertThat(cfg.getName()).isEqualTo("newName");
        assertThat(cfg.getProperties()).hasSize(2);
        assertThat(cfg.getProperty("key1")).isEqualTo("value1");
        assertThat(cfg.getProperty("key2")).isEqualTo("value2");

        List<String> annotatedClasses = cfg.getAnnotatedClasses();
        assertThat(annotatedClasses).hasSize(2);
        assertThat(annotatedClasses).contains(Integer.class.getName(), String.class.getName());

        assertThat(cfg.getValidationMode()).isSameAs(ValidationMode.CALLBACK);
    }

    @Test
    public void testDefaultValues() {
        SessionManagerConfig cfg = new SessionManagerConfig("name");
        assertThat(cfg.getContext()).isEqualTo("org.hibersap.execution.jco.JCoContext");
        assertThat(cfg.getJcaConnectionSpecFactory()).isEqualTo(
                "org.hibersap.execution.jca.cci.SapBapiJcaAdapterConnectionSpecFactory");
        assertThat(cfg.getValidationMode()).isSameAs(ValidationMode.AUTO);
    }
}
