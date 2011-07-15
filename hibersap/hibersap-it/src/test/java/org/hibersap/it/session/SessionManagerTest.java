/*
 * Copyright (c) 2009, 2011 akquinet tech@spree GmbH.
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

package org.hibersap.it.session;

import com.sap.conn.jco.ext.Environment;
import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.session.SessionManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SessionManagerTest
{
    private final AnnotationConfiguration configuration = new AnnotationConfiguration( "A12" );

    private SessionManager sessionManager;

    @Before
    public void buildSessionManager()
    {
        sessionManager = configuration.buildSessionManager();
    }

    @After
    public void closeSessionManager()
    {
        if ( sessionManager != null )
        {
            sessionManager.close();
        }
    }

    @Test
    public void sessionManagerUnregistersJCoDestinationWhenClosing() throws Exception
    {
        assertThat( Environment.isDestinationDataProviderRegistered(), is( true ) );

        sessionManager.close();

        assertThat( Environment.isDestinationDataProviderRegistered(), is( false ) );
    }
}
