/*
 * Copyright (c) 2008-2025 tech@spree GmbH
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

package org.hibersap.execution.jco;

import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.ext.DestinationDataProvider;
import com.sap.conn.jco.ext.Environment;
import java.util.Properties;
import org.assertj.core.api.Condition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class JCoEnvironmentTest {

    private static final Properties JCO_PROPERTIES_DUMMY = new Properties();

    private static final String DESTINATION_1 = "dest_1";
    private static final String DESTINATION_2 = "dest_2";
    private final Condition<String> registeredWithJCo = new Condition<String>("registeredWithJCo") {
        @Override
        public boolean matches(String name) {
            try {
                JCoDestinationManager.getDestination(name);
                return true;
            } catch (JCoException e) {
                return false;
            }
        }
    };

    @Before
    public void addDestinationProperties() {
        // since JCo 3.1.2, Properties must not be empty
        JCO_PROPERTIES_DUMMY.setProperty(DestinationDataProvider.JCO_ASHOST, "1.2.3.4");
    }

    @After
    public void unregisterDestinationDataProvidersAndDestinations() {
        JCoEnvironment.unregisterDestination(DESTINATION_1);
        JCoEnvironment.unregisterDestination(DESTINATION_2);
        assertThat(Environment.isDestinationDataProviderRegistered()).isFalse();
    }

    @Test
    public void destinationProviderIsRegisteredWithJCoWhenFirstDestinationGetsRegistered() {
        JCoEnvironment.registerDestination(DESTINATION_1, JCO_PROPERTIES_DUMMY);

        assertThat(Environment.isDestinationDataProviderRegistered()).isTrue();
    }

    @Test
    public void destinationIsAddedToJCoWhenDestinationGetsRegistered() {
        JCoEnvironment.registerDestination(DESTINATION_1, JCO_PROPERTIES_DUMMY);

        assertThat(DESTINATION_1).is(registeredWithJCo);
    }

    @Test
    public void destinationProviderIsUnregisteredFromJCoWhenLastDestinationGetsUnregistered() {
        assertThat(Environment.isDestinationDataProviderRegistered()).isFalse();

        JCoEnvironment.registerDestination(DESTINATION_1, JCO_PROPERTIES_DUMMY);
        JCoEnvironment.unregisterDestination(DESTINATION_1);

        assertThat(Environment.isDestinationDataProviderRegistered()).isFalse();
    }

    @Test
    public void destinationIsRemovedFromJCoWhenDestinationGetsUnregistered() {
        JCoEnvironment.registerDestination(DESTINATION_1, JCO_PROPERTIES_DUMMY);
        JCoEnvironment.unregisterDestination(DESTINATION_1);

        assertThat(DESTINATION_1).isNot(registeredWithJCo);
    }

    @Test
    public void canRegisterTwoDestinations() {
        JCoEnvironment.registerDestination(DESTINATION_1, JCO_PROPERTIES_DUMMY);
        JCoEnvironment.registerDestination(DESTINATION_2, JCO_PROPERTIES_DUMMY);

        assertThat(DESTINATION_1).is(registeredWithJCo);
        assertThat(DESTINATION_2).is(registeredWithJCo);
    }

    @Test
    public void destinationProviderIsUnregisteredFromJCoWhenTwoDestinationsAreRegisteredAndUnregistered() {
        JCoEnvironment.registerDestination(DESTINATION_1, JCO_PROPERTIES_DUMMY);
        JCoEnvironment.registerDestination(DESTINATION_2, JCO_PROPERTIES_DUMMY);
        JCoEnvironment.unregisterDestination(DESTINATION_1);
        JCoEnvironment.unregisterDestination(DESTINATION_2);

        assertThat(Environment.isDestinationDataProviderRegistered()).isFalse();
    }
}
