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

package org.hibersap.it.jco.monitoring;

import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.execution.jco.util.JCoMonitoringUtil;
import org.hibersap.execution.jco.util.MonitoringData;
import org.junit.Before;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class JCoMonitoringUtilTest {

    private final JCoMonitoringUtil util = new JCoMonitoringUtil();

    @Before
    public void setUp() {
        // Hibersap is just used here to register the JCo destination, we don't really need the SessionManager
        new AnnotationConfiguration("A12").buildSessionManager();
    }

    @Test
    public void showMonitoringData() throws Exception {
        MonitoringData data = util.getMonitoringData("A12");

        assertThat(data.getSystemDescription()).startsWith("DEST:").contains("A12");
    }
}
