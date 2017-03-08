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
public interface Context {

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
    void configure(SessionManagerConfig config) throws HibersapException;

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