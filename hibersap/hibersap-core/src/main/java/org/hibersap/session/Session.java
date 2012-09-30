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

/**
 * @author Carsten Erker
 */
public interface Session
{
    /**
     * starts a transaction.
     * 
     * @return The Hibersap transaction
     */
    Transaction beginTransaction();

    /**
     * releases all resources.
     */
    void close();

    /**
     * executes a funtion module in SAP.
     * 
     * @param bapi The BAPI class
     */
    void execute( Object bapi );

    /**
     * returns the transaction.
     * 
     * @return The hibersap transaction or null if no transaction started.
     */
    Transaction getTransaction();

    /**
     * is session already closed?
     * 
     * @return true if closed, else false.
     */
    boolean isClosed();
}
