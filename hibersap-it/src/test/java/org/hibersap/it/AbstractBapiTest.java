/*
 * Copyright (c) 2008-2014 akquinet tech@spree GmbH
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

package org.hibersap.it;

import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.session.Session;
import org.hibersap.session.SessionManager;
import org.junit.After;
import org.junit.Before;

public class AbstractBapiTest {

    protected Session session;
    protected SessionManager sessionManager;

    @Before
    public void openSession() throws Exception {
        sessionManager = new AnnotationConfiguration( "A12" ).buildSessionManager();
        session = sessionManager.openSession();
    }

    @After
    public void closeSession() throws Exception {
        if ( session != null ) {
            session.close();
        }
        if ( sessionManager != null ) {
            sessionManager.close();
        }
    }
}
