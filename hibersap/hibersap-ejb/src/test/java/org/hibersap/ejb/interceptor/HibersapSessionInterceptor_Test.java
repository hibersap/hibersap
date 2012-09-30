/*
 * Copyright (c) 2008-2012 akquinet tech@spree GmbH
 *
 * This file is part of Hibersap.
 *
 * Hibersap is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Hibersap is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Hibersap. If
 * not, see <http://www.gnu.org/licenses/>.
 */

package org.hibersap.ejb.interceptor;

import org.hibersap.ejb.util.MockInitialContextFactory;
import org.hibersap.session.Session;
import org.hibersap.session.SessionManager;
import org.junit.Before;
import org.junit.Test;

import javax.interceptor.InvocationContext;
import javax.naming.Context;
import java.util.HashMap;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.fest.assertions.Assertions.assertThat;

public class HibersapSessionInterceptor_Test
{
    private final HibersapSessionInterceptor _interceptor = new HibersapSessionInterceptor();

    private final InvocationContext _ctx = createNiceMock( InvocationContext.class );
    private final Context _initialCtx = createNiceMock( Context.class );
    private final SessionManager _sessionManager = createNiceMock( SessionManager.class );
    private final Session _session = createNiceMock( Session.class );

    private final HashMap<String, Object> _contextData = new HashMap<String, Object>();
    private final TestEjb _targetBean = new TestEjb();

    @Before
    public void setUp() throws Exception
    {
        System.setProperty( "java.naming.factory.initial", MockInitialContextFactory.NAME );
        MockInitialContextFactory.setMockContext( _initialCtx );

        expect( _ctx.getTarget() ).andReturn( _targetBean ).anyTimes();
        expect( _ctx.getContextData() ).andReturn( _contextData ).anyTimes();
        expect( _sessionManager.openSession() ).andReturn( _session );
        expect( _session.isClosed() ).andReturn( true );
        replay( _ctx, _sessionManager, _session );
    }

    @Test
    public void injectsTwoSessionsIntoBeanInstanceWhereOneIsAlreadyInInjectionContextAndTheOtherGetsCreated() throws
                                                                                                              Exception
    {
        Session session2 = createNiceMock( Session.class );
        _contextData.put( "hibersap.session.jndiName2", session2 );

        expect( _initialCtx.lookup( "jndiName1" ) ).andReturn( _sessionManager );
        replay( _initialCtx );

        _interceptor.injectSessionsIntoEjb( _ctx );

        assertThat( _targetBean.session1 ).isSameAs( _session );
        assertThat( _targetBean.session2 ).isSameAs( session2 );
    }

    @Test
    public void removesTheCreatedSessionFromInvocationContextAndKeepsTheOtherOne() throws Exception
    {
        _contextData.put( "hibersap.session.jndiName1", createNiceMock( Session.class ) );

        expect( _initialCtx.lookup( "jndiName2" ) ).andReturn( _sessionManager );
        replay( _initialCtx );

        _interceptor.injectSessionsIntoEjb( _ctx );

        assertThat( _contextData ).hasSize( 1 );
    }

    private static class TestEjb
    {
        @HibersapSession( "jndiName1" )
        private Session session1;

        @HibersapSession( "jndiName2" )
        private Session session2;
    }
}
