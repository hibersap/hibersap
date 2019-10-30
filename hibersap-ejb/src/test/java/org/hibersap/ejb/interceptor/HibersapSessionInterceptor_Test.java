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

package org.hibersap.ejb.interceptor;

import java.util.HashMap;
import javax.interceptor.InvocationContext;
import javax.naming.Context;
import org.hibersap.ejb.util.MockInitialContextFactory;
import org.hibersap.session.Session;
import org.hibersap.session.SessionManager;
import org.junit.Before;
import org.junit.Test;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.fest.assertions.Assertions.assertThat;

public class HibersapSessionInterceptor_Test {

    private final HibersapSessionInterceptor interceptor = new HibersapSessionInterceptor();

    private final InvocationContext invocationContext = createNiceMock(InvocationContext.class);
    private final Context initialCtx = createNiceMock(Context.class);
    private final SessionManager sessionManager = createNiceMock(SessionManager.class);
    private final Session session = createNiceMock(Session.class);

    private final HashMap<String, Object> contextData = new HashMap<String, Object>();
    private final TestEjb targetBean = new TestEjb();

    @Before
    public void setUp() throws Exception {
        System.setProperty("java.naming.factory.initial", MockInitialContextFactory.NAME);
        MockInitialContextFactory.setMockContext(initialCtx);

        expect(invocationContext.getTarget()).andReturn(targetBean).anyTimes();
        expect(invocationContext.getContextData()).andReturn(contextData).anyTimes();
        expect(sessionManager.openSession()).andReturn(session);
        expect(session.isClosed()).andReturn(true);
        replay(invocationContext, sessionManager, session);
    }

    @Test
    public void injectsTwoSessionsIntoBeanInstanceWhereOneIsAlreadyInInjectionContextAndTheOtherGetsCreated() throws
            Exception {
        Session session2 = createNiceMock(Session.class);
        contextData.put("hibersap.session.jndiName2", session2);

        expect(initialCtx.lookup("jndiName1")).andReturn(sessionManager);
        replay(initialCtx);

        interceptor.injectSessionsIntoEjb(invocationContext);

        assertThat(targetBean.session1).isSameAs(session);
        assertThat(targetBean.session2).isSameAs(session2);
    }

    @Test
    public void removesTheCreatedSessionFromInvocationContextAndKeepsTheOtherOne() throws Exception {
        contextData.put("hibersap.session.jndiName1", createNiceMock(Session.class));

        expect(initialCtx.lookup("jndiName2")).andReturn(sessionManager);
        replay(initialCtx);

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
