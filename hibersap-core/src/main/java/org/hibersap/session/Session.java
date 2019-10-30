/*
 * Copyright (c) 2008-2019 akquinet tech@spree GmbH
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

import java.io.Closeable;

/**
 * @author Carsten Erker
 */
public interface Session extends Closeable {

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
     * @param bapi
     *            The BAPI class
     */
    void execute(final Object bapi);

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
