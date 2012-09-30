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

package org.hibersap.session;

import java.io.Serializable;

import org.hibersap.configuration.xml.SessionManagerConfig;

/*
 * The client's interface to the SessionManager. A SessionManager is used to create Hibersap
 * sessions.
 * 
 * @author Carsten Erker
 */
public interface SessionManager extends Serializable
{
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
    Session openSession( Credentials credentials );

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
