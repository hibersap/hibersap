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

package org.hibersap.ejb.util;

import javax.naming.Context;
import javax.naming.NamingException;
import org.hibersap.HibersapException;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.session.Credentials;
import org.hibersap.session.Session;
import org.hibersap.session.SessionManager;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class JndiUtilTest {

    private static final String JNDI_NAME = "jndiName";

    private final SessionManagerDummy sessionManager = new SessionManagerDummy();
    private final Context initialCtx = mock(Context.class);

    @Before
    public void setUp() {
        System.setProperty("java.naming.factory.initial", MockInitialContextFactory.NAME);
        MockInitialContextFactory.setMockContext(initialCtx);
    }

    @Test
    public void rebindSessionManagerActuallyBindsItToJndi() throws Exception {

        JndiUtil.rebindSessionManager(sessionManager, JNDI_NAME);

        verify(initialCtx).rebind(JNDI_NAME, sessionManager);
    }

    @Test(expected = HibersapException.class)
    public void rebindSessionManagerWithNamingExceptionThrowsHibersapException() throws Exception {
        doThrow(new NamingException()).when(initialCtx).rebind(JNDI_NAME, sessionManager);

        JndiUtil.rebindSessionManager(sessionManager, JNDI_NAME);
    }

    @Test
    public void unbindSessionManagerActuallyUnbindsItFromJndi() throws Exception {
        JndiUtil.unbindSessionManager(JNDI_NAME);

        verify(initialCtx).unbind(JNDI_NAME);
    }

    @Test
    public void unbindSessionManagerWithNamingExceptionDoesNotThrowException() throws Exception {
        JndiUtil.unbindSessionManager(JNDI_NAME);

        verify(initialCtx).unbind(JNDI_NAME);
    }

    private static class SessionManagerDummy implements SessionManager {

        public SessionManagerConfig getConfig() {
            return new SessionManagerConfig("sessionManagerName");
        }

        public Session openSession() {
            return null;
        }

        public Session openSession(Credentials credentials) {
            return null;
        }

        public void close() {
        }

        public boolean isClosed() {
            return false;
        }
    }
}
