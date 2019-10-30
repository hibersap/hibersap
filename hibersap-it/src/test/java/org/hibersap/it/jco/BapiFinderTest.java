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

package org.hibersap.it.jco;

import org.hibersap.it.AbstractBapiTest;
import org.junit.Test;
import static org.fest.assertions.Assertions.assertThat;

public class BapiFinderTest extends AbstractBapiTest {

    @Test
    public void findsFlightBapis() {
        BapiFinder bapiFinder = new BapiFinder("BC-DWB");
        session.execute(bapiFinder);

        assertThat(bapiFinder.getBapiDescriptions().size()).isGreaterThan(0);
        assertThat(bapiFinder.getBapiDescriptions()).contains(
                new BapiDescription("Flight", "Flight with connection data (SAP training)", "GetList",
                        "Find list of flights", "BAPI_FLIGHT_GETLIST")
        );
    }
}
