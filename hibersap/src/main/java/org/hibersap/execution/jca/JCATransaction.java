package org.hibersap.execution.jca;

/*
 * Copyright (C) 2008 akquinet tech@spree GmbH
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

import javax.resource.ResourceException;
import javax.resource.cci.LocalTransaction;

import org.hibersap.HibersapException;
import org.hibersap.session.Transaction;

/**
 * Implementation for JCA, i.e. it uses a deployed resource adapter to connect to SAP.
 * 
 * @author dahm
 */
public class JCATransaction
    implements Transaction
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
            throw new HibersapException( "begin transaction", e );
        }
    }

    public void commit()
    {
        try
        {
            transaction.commit();
        }
        catch ( final ResourceException e )
        {
            throw new HibersapException( "commit transaction", e );
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
            throw new HibersapException( "rollback transaction", e );
        }
    }
}
