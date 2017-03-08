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

import java.util.ArrayList;
import java.util.List;

/**
 * May be used by implementors of the Transaction interface to unify the handling of
 * synchronizations.
 *
 * @author Carsten Erker
 */
public abstract class AbstractTransaction
        implements Transaction {

    private final List<Synchronization> synchronizations = new ArrayList<Synchronization>();

    /**
     * Register application-defined Synchronization callback.
     *
     * @param synchronization The callback object.
     */
    public final void registerSynchronization( final Synchronization synchronization ) {
        synchronizations.add( synchronization );
    }

    protected final void notifySynchronizationsBeforeCompletion() {
        for ( Synchronization synchronization : synchronizations ) {
            synchronization.beforeCompletion();
        }
    }

    protected final void notifySynchronizationsAfterCompletion( final boolean committed ) {
        for ( Synchronization synchronization : synchronizations ) {
            synchronization.afterCompletion( committed );
        }
    }
}
