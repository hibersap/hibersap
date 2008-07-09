package org.hibersap.execution.jco;

/*
 * Copyright (C) 2008 akquinet tech@spree GmbH
 * 
 * This file is part of Hibersap.
 *
 * Hibersap is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hibersap is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Hibersap.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.Map;

import org.hibersap.HibersapException;
import org.hibersap.execution.Executor;
import org.hibersap.session.Session;
import org.hibersap.session.Transaction;

import com.sap.mw.jco.IFunctionTemplate;
import com.sap.mw.jco.IRepository;
import com.sap.mw.jco.JCO.Client;
import com.sap.mw.jco.JCO.Function;


/**
 * @author Carsten Erker
 */
public class JCoExecutor
    implements Executor
{
    private JCoDirectConnectionProvider connectionProvider;

    private JCoMapper jcoMapper;

    private JCoTransaction transaction = null;

    private Client client;

    private Session session;

    public Transaction beginTransaction()
    {
        if ( transaction != null )
        {
            return transaction;
        }
        transaction = new JCoTransaction( session );
        return transaction;
    }

    public void close()
    {
        connectionProvider.releaseClient( client );
    }

    public void configure( Session owningSession )
    {
        this.session = owningSession;
        connectionProvider = new JCoDirectConnectionProvider();
        connectionProvider.configure( owningSession.getSessionFactory().getProperties() );
        jcoMapper = new JCoMapper();
    }

    public void execute( String bapiName, Map<String, Object> functionMap )
    {
        getClientIfNecessary();

        IRepository repository = connectionProvider.getRepository( client );
        IFunctionTemplate template = repository.getFunctionTemplate( bapiName );
        if ( template == null )
        {
            throw new HibersapException( "The function module '" + bapiName + "' does not exist in SAP" );
        }

        Function function = template.getFunction();
        jcoMapper.putFunctionMapValuesToFunction( function, functionMap );
        client.execute( function );
        jcoMapper.putFunctionValuesToFunctionMap( function, functionMap );
    }

    private Client getClientIfNecessary()
    {
        if ( client == null )
        {
            client = connectionProvider.getClient();
        }
        return client;
    }

    public Transaction getTransaction()
    {
        return transaction;
    }
}