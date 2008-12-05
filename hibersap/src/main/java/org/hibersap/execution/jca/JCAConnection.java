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

import java.util.Map;

import javax.resource.ResourceException;

import org.hibersap.HibersapException;
import org.hibersap.execution.Connection;
import org.hibersap.session.Session;
import org.hibersap.session.Transaction;

/**
 * Implementation for JCA, i.e. it uses a deployed resource adapter to connect to SAP.
 * 
 * @author dahm
 */
public class JCAConnection
    implements Connection
{
    private final javax.resource.cci.Connection connection;

    private Transaction transaction;

    public JCAConnection( final javax.resource.cci.Connection connection )
    {
        this.connection = connection;
    }

    public Transaction beginTransaction( final Session session )
    {
        if ( transaction == null )
        {
            try
            {
                transaction = new JCATransaction( connection.getLocalTransaction() );
            }
            catch ( final ResourceException e )
            {
                throw new HibersapException( "new JCATransaction", e );
            }

            transaction.begin();
        }

        return transaction;
    }

    public void close()
    {
        try
        {
            connection.close();
        }
        catch ( final ResourceException e )
        {
            throw new HibersapException( "While closing JCA connection", e );
        }
    }

    // TODO Implement me
    public void execute( final String bapiName, final Map<String, Object> functionMap )
    {

    }

    public Transaction getTransaction()
    {
        return transaction;
    }
}
