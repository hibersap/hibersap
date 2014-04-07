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

package org.hibersap.execution;

import java.util.Map;

import org.hibersap.session.Credentials;
import org.hibersap.session.SessionImplementor;
import org.hibersap.session.Transaction;

/*
 * Implementations of this interface define the functionality how to communicate with SAP, using for
 * example the SAP Java Connector or a JCA resource adapter. The implementation to be used by a
 * session manager is specified by the <code>context</code> element of the hibersap.xml. The default
 * implementation is org.hibersap.execution.jco.JCoConnection. Implementations must provide a
 * default constructor.
 * 
 * @author Carsten Erker
 */
public interface Connection
{
    /**
     * Set the credentials for the session. If custom credentials are provided, this method must be
     * called before the execute() method is called for the first time on the Session this
     * Connection belongs to.
     * 
     * The credentials overwrite the configured properties. Only the credential fields that are set
     * (i.e. that are not null) will change the default behavior.
     * 
     * @param credentials The Credentials
     */
    void setCredentials( Credentials credentials );

    /**
     * Begins a logical unit of work.
     * 
     * @param session The Session this Connection belongs to.
     * @return The Transaction
     */
    Transaction beginTransaction( SessionImplementor session );

    /**
     * Returns the current transaction.
     * 
     * @return The Transaction
     */
    Transaction getTransaction();

    /**
     * Calls a remote function module in the SAP system.
     * 
     * @param bapiName The function module name
     * @param functionMap The function module parameters
     */
    void execute( String bapiName, Map<String, Object> functionMap );

    /**
     * Closes this connection. Implementing classes must do everything that is needed to free
     * resources etc.
     */
    void close();
}
