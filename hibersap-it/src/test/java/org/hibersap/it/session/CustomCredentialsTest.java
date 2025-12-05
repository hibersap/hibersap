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

package org.hibersap.it.session;

import com.sap.conn.jco.monitor.JCoConnectionData;
import com.sap.conn.jco.monitor.JCoConnectionMonitor;
import java.util.List;
import org.hibersap.SapException;
import org.hibersap.it.AbstractBapiTest;
import org.hibersap.it.bapi.RfcPing;
import org.hibersap.session.Credentials;
import org.hibersap.session.Session;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class CustomCredentialsTest extends AbstractBapiTest {

    @Test
    public void connectToSapUsingCustomCredentials() {

        final Credentials credentials = new Credentials().setUser("sapuser2")
                .setPassword("password2").setLanguage("DE");
        callBapiWith(credentials);

        final JCoConnectionData connectionUsed = getConnectionDataUsed();

        // these parameters should be used instead of the configured ones
        assertNotNull(connectionUsed);
        assertEquals("SAPUSER2", connectionUsed.getAbapUser());
        assertEquals("DE", connectionUsed.getAbapLanguage());
    }

    private void callBapiWith(Credentials credentials) {
        // make sure a connection to SAP was established
        try (final Session session = sessionManager.openSession(credentials)) {
            session.execute(new RfcPing());
        } catch (final SapException e) {
            // expected when there are no lines found
        }
    }

    private JCoConnectionData getConnectionDataUsed() {
        final List<? extends JCoConnectionData> list = JCoConnectionMonitor
                .getConnectionsData();
        for (final JCoConnectionData data : list) {
            if ("RFC_PING".equals(data.getFunctionModuleName())) {
                return data;
            }
        }
        return null;
    }
}
