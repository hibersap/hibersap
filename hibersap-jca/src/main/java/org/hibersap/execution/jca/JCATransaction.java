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

package org.hibersap.execution.jca;

import org.hibersap.HibersapException;
import org.hibersap.session.AbstractTransaction;

import javax.resource.ResourceException;
import javax.resource.cci.LocalTransaction;

/**
 * Implementation for JCA, i.e. it uses a deployed resource adapter to connect to SAP.
 * 
 * @author dahm
 */
public class JCATransaction
    extends AbstractTransaction
{
    private final LocalTransaction transaction;

    public JCATransaction( final LocalTransaction transaction )
    {
        this.transaction = transaction;
    }

    public void begin()
    {
        try
        {
            transaction.begin();
        }
        catch ( final ResourceException e )
        {
            throw new HibersapException( "Error beginning a local transaction", e );
        }
    }

    public void commit()
    {
        notifySynchronizationsBeforeCompletion();
        try
        {
            transaction.commit();
            notifySynchronizationsAfterCompletion( true );
        }
        catch ( final ResourceException e )
        {
            notifySynchronizationsAfterCompletion( false );
            throw new HibersapException( "Error committing a local transaction", e );
        }
    }

    public void rollback()
    {
        try
        {
            transaction.rollback();
        }
        catch ( final ResourceException e )
        {
            throw new HibersapException( "Error rolling back a local transaction", e );
        }
        finally
        {
            notifySynchronizationsAfterCompletion( false );
        }
    }
}
