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

package org.hibersap.ejb.util;

import org.hibersap.HibersapException;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.session.Credentials;
import org.hibersap.session.Session;
import org.hibersap.session.SessionManager;
import org.junit.Before;
import org.junit.Test;

import javax.naming.Context;
import javax.naming.NamingException;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

public class JndiUtilTest {

    private static final String JNDI_NAME = "jndiName";

    private final SessionManagerDummy sessionManager = new SessionManagerDummy();
    private final Context initialCtx = createNiceMock( Context.class );

    @Before
    public void setUp() throws Exception {
        System.setProperty( "java.naming.factory.initial", MockInitialContextFactory.NAME );
        MockInitialContextFactory.setMockContext( initialCtx );
    }

    @Test
    public void rebindSessionManagerActuallyBindsItToJndi() throws Exception {
        initialCtx.rebind( JNDI_NAME, sessionManager );
        replay( initialCtx );

        JndiUtil.rebindSessionManager( sessionManager, JNDI_NAME );

        verify( initialCtx );
    }

    @Test(expected = HibersapException.class)
    public void rebindSessionManagerWithNamingExceptionThrowsHibersapException() throws Exception {
        initialCtx.rebind( JNDI_NAME, sessionManager );
        expectLastCall().andThrow( new NamingException() );
        replay( initialCtx );

        JndiUtil.rebindSessionManager( sessionManager, JNDI_NAME );

        verify( initialCtx );
    }

    @Test
    public void unbindSessionManagerActuallyUnbindsItFromJndi() throws Exception {
        initialCtx.unbind( JNDI_NAME );
        replay( initialCtx );

        JndiUtil.unbindSessionManager( JNDI_NAME );

        verify( initialCtx );
    }

    @Test
    public void unbindSessionManagerWithNamingExceptionDoesNotThrowException() throws Exception {
        initialCtx.unbind( JNDI_NAME );
        replay( initialCtx );

        JndiUtil.unbindSessionManager( JNDI_NAME );

        verify( initialCtx );
    }

    private static class SessionManagerDummy implements SessionManager {

        public SessionManagerConfig getConfig() {
            return new SessionManagerConfig( "sessionManagerName" );
        }

        public Session openSession() {
            return null;
        }

        public Session openSession( Credentials credentials ) {
            return null;
        }

        public void close() {
        }

        public boolean isClosed() {
            return false;
        }
    }
}
