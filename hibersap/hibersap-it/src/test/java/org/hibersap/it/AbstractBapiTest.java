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

package org.hibersap.it;

import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.session.Session;
import org.hibersap.session.SessionManager;
import org.junit.After;
import org.junit.Before;

public class AbstractBapiTest
{
    protected Session session;
    protected SessionManager sessionManager;

    @Before
    public void openSession() throws Exception
    {
        sessionManager = new AnnotationConfiguration( "A12" ).buildSessionManager();
        session = sessionManager.openSession();
    }

    @After
    public void closeSession() throws Exception
    {
        if ( session != null )
        {
            session.close();
        }
        if ( sessionManager != null )
        {
            sessionManager.close();
        }
    }
}
