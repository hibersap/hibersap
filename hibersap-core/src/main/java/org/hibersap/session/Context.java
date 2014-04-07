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

import org.hibersap.HibersapException;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.execution.Connection;

/*
 * One of the interfaces (the other being org.hibersap.execution.Connection) to interact with the
 * concrete subsystem that implements the communication with SAP (e.g. JCo, JCA).
 * 
 * Implementations of this class are called by the hibersap core (which is independent of the
 * subsystem).
 *
 * @author Carsten Erker
 */
public interface Context
{
    /*
     * Called by the framework at initialization time, i.e. when the SessionManager is built. Allows
     * the subsystem to initialize itself. The implementation of this class must be specified in the
     * Hibersap configuration.
     * 
     * @param config The Hibersap configuration properties.
     * 
     * @throws HibersapException The implementation of this method should throw a HibersapException
     * if anything goes wrong.
     */
    void configure( SessionManagerConfig config ) throws HibersapException;

    /*
     * Called by the framework when the method org.hibersap.session.SessionManager.close() is
     * called. Allows the subsystem to free resources etc.
     */
    void close();

    /*
     * Called by the framework when a new Session is created. The subsystem must provide an
     * implementation of the interface org.hibersap.execution.Connection.
     * 
     * @return The Connection
     * 
     * @see org.hibersap.execution.Connection
     */
    Connection getConnection();
}