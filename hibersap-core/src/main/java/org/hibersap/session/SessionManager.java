/*
 * Copyright (c) 2008-2017 akquinet tech@spree GmbH
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

package org.hibersap.session;

import java.io.Serializable;
import org.hibersap.configuration.xml.SessionManagerConfig;

/*
 * The client's interface to the SessionManager. A SessionManager is used to create Hibersap
 * sessions.
 * 
 * @author Carsten Erker
 */
public interface SessionManager extends Serializable {

    /**
     * Get Configuration.
     *
     * @return The Configuration object
     */
    SessionManagerConfig getConfig();

    /**
     * Open a Session using a newly created connection to SAP.
     *
     * @return The Session
     */
    Session openSession();

    /**
     * Open a Session using a newly created connection to SAP, providing user credentials.
     *
     * @param credentials User Credentials for the SAP system to be called.
     * @return The Session
     */
    Session openSession(Credentials credentials);

    /**
     * Closes a SessionManager, freeing resources held by the framework.
     * After calling this method, the SessionManager should not be used anymore and will throw Exceptions
     * on certain method calls.
     */
    void close();

    /**
     * Returns the status of this SessionManager.
     *
     * @return true, if this SessionManager is closed, false if it can still be used.
     */
    boolean isClosed();
}
