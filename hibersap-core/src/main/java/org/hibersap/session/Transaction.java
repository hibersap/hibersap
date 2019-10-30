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

/**
 * Represents a transaction. Implementors must call beforeCompletion() on all registered
 * Synchronizations before committing a transaction and afterCompletion() after committing or
 * rolling back a transaction.
 *
 * @author Carsten Erker
 */
public interface Transaction {

    void begin();

    void commit();

    void rollback();

    void registerSynchronization(Synchronization synchronization);
}
