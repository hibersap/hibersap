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

import java.util.ArrayList;
import java.util.List;

/**
 * May be used by implementors of the Transaction interface to unify the handling of
 * synchronizations.
 * 
 * @author Carsten Erker
 */
public abstract class AbstractTransaction
    implements Transaction
{
    private final List<Synchronization> synchronizations = new ArrayList<Synchronization>();

    /**
     * Register application-defined Synchronization callback.
     *
     * @param synchronization The callback object.
     */
    public final void registerSynchronization( Synchronization synchronization )
    {
        synchronizations.add( synchronization );
    }

    protected final void notifySynchronizationsBeforeCompletion()
    {
        for ( Synchronization synchronization : synchronizations )
        {
            synchronization.beforeCompletion();
        }
    }

    protected final void notifySynchronizationsAfterCompletion( boolean committed )
    {
        for ( Synchronization synchronization : synchronizations )
        {
            synchronization.afterCompletion( committed );
        }
    }
}
