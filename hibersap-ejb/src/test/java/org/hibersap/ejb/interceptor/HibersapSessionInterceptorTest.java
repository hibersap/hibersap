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

package org.hibersap.ejb.interceptor;

import java.util.HashMap;
import javax.interceptor.InvocationContext;
import javax.naming.Context;
import org.hibersap.ejb.util.MockInitialContextFactory;
import org.hibersap.session.Session;
import org.hibersap.session.SessionManager;
import org.junit.Before;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HibersapSessionInterceptorTest {

    private final HibersapSessionInterceptor interceptor = new HibersapSessionInterceptor();

    private final InvocationContext invocationContext = mock(InvocationContext.class);
    private final Context initialCtx = mock(Context.class);
    private final SessionManager sessionManager = mock(SessionManager.class);
    private final Session session = mock(Session.class);

    private final HashMap<String, Object> contextData = new HashMap<>();
    private final TestEjb targetBean = new TestEjb();

    @Before
    public void setUp() {
        System.setProperty("java.naming.factory.initial", MockInitialContextFactory.NAME);
        MockInitialContextFactory.setMockContext(initialCtx);

        when(invocationContext.getTarget()).thenReturn(targetBean);
        when(invocationContext.getContextData()).thenReturn(contextData);
        when(sessionManager.openSession()).thenReturn(session);
        when(session.isClosed()).thenReturn(true);
    }

    @Test
    public void injectsTwoSessionsIntoBeanInstanceWhereOneIsAlreadyInInjectionContextAndTheOtherGetsCreated() throws Exception {
        Session session2 = mock(Session.class);
        contextData.put("hibersap.session.jndiName2", session2);
        when(initialCtx.lookup("jndiName1")).thenReturn(sessionManager);

        interceptor.injectSessionsIntoEjb(invocationContext);

        assertThat(targetBean.session1).isSameAs(session);
        assertThat(targetBean.session2).isSameAs(session2);
    }

    @Test
    public void removesTheCreatedSessionFromInvocationContextAndKeepsTheOtherOne() throws Exception {
        contextData.put("hibersap.session.jndiName1", mock(Session.class));
        when(initialCtx.lookup("jndiName2")).thenReturn(sessionManager);

        interceptor.injectSessionsIntoEjb(invocationContext);

        assertThat(contextData).hasSize(1);
    }

    private static class TestEjb {

        @HibersapSession("jndiName1")
        private Session session1;

        @HibersapSession("jndiName2")
        private Session session2;
    }
}
