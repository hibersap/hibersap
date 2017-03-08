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

package org.hibersap.configuration.xml;

import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.fest.assertions.Assertions.assertThat;

public class HiberSapJaxbXmlParserTest {

    private static HibersapConfig config;
    private static SessionManagerConfig sessionManagerA12;
    private static SessionManagerConfig sessionManagerB34;

    @BeforeClass
    public static void createHibersapConfig() {
        final HibersapJaxbXmlParser hiberSapJaxbXmlParser = new HibersapJaxbXmlParser();
        config = hiberSapJaxbXmlParser.parseResource("/xml-configurations/hibersapOK.xml");
        sessionManagerA12 = config.getSessionManager("A12");
        sessionManagerB34 = config.getSessionManager("B34");
    }

    @Test
    public void createsTwoSessionManagers() {
        List<SessionManagerConfig> sessionManagers = config.getSessionManagers();

        assertThat(sessionManagers).hasSize(2);
    }

    @Test
    public void sessionManagersHaveCorrectNames() {
        final String a12Name = sessionManagerA12.getName();
        final String b34Name = sessionManagerB34.getName();

        assertThat(a12Name).isEqualTo("A12");
        assertThat(b34Name).isEqualTo("B34");
    }

    @Test
    public void sessionManagersHaveCorrectHibersapContext() {

        final String a12Context = sessionManagerA12.getContext();
        final String b34Context = sessionManagerB34.getContext();

        assertThat(a12Context).isEqualTo("org.hibersap.execution.jco.JCoContext");
        assertThat(b34Context).isEqualTo("org.hibersap.execution.jca.JCAContext");
    }

    @Test
    public void sessionManagerHasCorrectJcaConnectionFactory() {
        final String connectionSpecFactory = sessionManagerB34.getJcaConnectionFactory();

        assertThat(connectionSpecFactory).isEqualTo("java:/eis/sap/B34");
    }

    @Test
    public void sessionManagerHasCorrectJcaConnectionSpecFactory() {
        final SessionManagerConfig manager = sessionManagerB34;

        assertThat(manager.getJcaConnectionSpecFactory())
                .isEqualTo("org.hibersap.test.MyTestConnectionSpecFactory");
    }

    @Test
    public void sessionManagerHasCorrectProperties() {
        List<Property> properties = sessionManagerB34.getProperties();

        assertThat(properties).hasSize(2);
        assertThat(properties).contains(
                new Property("property1_name", "property1_value"),
                new Property("property2_name", "property2_value"));
    }

    @Test
    public void sessionManagerHasCorrectAnnotatedClasses() {
        final List<String> classes = sessionManagerB34.getAnnotatedClasses();

        assertThat(classes).hasSize(2);
        assertThat(classes).contains("org.test.Class1", "org.test.Class3");
    }

    @Test
    public void sessionManagerHasCorrectExecutionInterceptors() {
        final List<String> classes = sessionManagerB34.getExecutionInterceptorClasses();

        assertThat(classes).hasSize(2);
        assertThat(classes).contains("org.test.Class4", "org.test.Class5");
    }

    @Test
    public void sessionManagerHasCorrectBapiInterceptors() {
        final List<String> classes = sessionManagerB34.getBapiInterceptorClasses();

        assertThat(classes).hasSize(2);
        assertThat(classes).contains("org.test.Class6", "org.test.Class7");
    }

    @Test
    public void sessionManagerHasCorrectValidationMode() {
        final ValidationMode validationMode = sessionManagerB34.getValidationMode();
        assertThat(validationMode).isSameAs(ValidationMode.CALLBACK);
    }

    @Test
    public void simpleHibersapXmlWithANamespaceCanBeBuilt() {
        final HibersapJaxbXmlParser hiberSapJaxbXmlParser = new HibersapJaxbXmlParser();
        HibersapConfig config = hiberSapJaxbXmlParser.parseResource("/xml-configurations/hibersapSample.xml");
        SessionManagerConfig sessionManagerNSP = config.getSessionManager("NSP");

        assertThat(sessionManagerNSP).isNotNull();
        assertThat(sessionManagerNSP.getJcaConnectionFactory()).isEqualTo("java:jboss/eis/sap/NSP");
    }
}
