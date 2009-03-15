package org.hibersap.execution.jco;

/*
 * Copyright (C) 2008-2009 akquinet tech@spree GmbH
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

import org.hibersap.HibersapException;
import org.hibersap.execution.Connection;
import org.hibersap.session.Credentials;
import org.hibersap.session.SessionImplementor;
import org.hibersap.session.Transaction;

import com.sap.conn.jco.JCoCustomDestination;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoCustomDestination.UserData;

/*
 * @author Carsten Erker
 */
public class JCoConnection
    implements Connection
{
    private JCoDestination destination = null;

    private final JCoContextAdapter jcoContext = new JCoContextAdapterImpl();

    private final JCoMapper jcoMapper;

    private JCoTransaction transaction = null;

    private final String destinationName;

    private Credentials credentials;

    public JCoConnection( String destinationName )
    {
        this.jcoMapper = new JCoMapper();
        this.destinationName = destinationName;
    }

    /**
     * {@inheritDoc}
     */
    public Transaction beginTransaction( SessionImplementor session )
    {
        if ( transaction != null )
        {
            return transaction;
        }
        transaction = new JCoTransaction( session );
        transaction.begin();

        return transaction;
    }

    public void close()
    {
        endStatefulConnection();
    }

    public void execute( String bapiName, Map<String, Object> functionMap )
    {
        if ( destination == null )
        {
            startStatefulConnection();
        }

        JCoFunction function;

        try
        {
            function = getRepository().getFunction( bapiName );
        }
        catch ( JCoException e )
        {
            throw new HibersapException( "The function " + bapiName + " is not available in the SAP system", e );
        }
        if ( function == null )
        {
            throw new HibersapException( "The function module '" + bapiName + "' does not exist in SAP" );
        }

        jcoMapper.putFunctionMapValuesToFunction( function, functionMap );

        try
        {
            function.execute( destination );
        }
        catch ( JCoException e )
        {
            throw new HibersapException( "Error executing function module " + bapiName, e );
        }

        jcoMapper.putFunctionValuesToFunctionMap( function, functionMap );
    }

    /**
     * {@inheritDoc}
     */
    public Transaction getTransaction()
    {
        return transaction;
    }

    /**
     * {@inheritDoc}
     */
    public void setCredentials( Credentials credentials )
    {
        this.credentials = credentials;
    }

    private void copyCredentialsToUserData( Credentials cred, UserData data )
    {
        if ( cred.getAliasUser() != null )
        {
            data.setAliasUser( cred.getAliasUser() );
        }
        if ( cred.getClient() != null )
        {
            data.setClient( cred.getClient() );
        }
        if ( cred.getLanguage() != null )
        {
            data.setLanguage( cred.getLanguage() );
        }
        if ( cred.getPassword() != null )
        {
            data.setPassword( cred.getPassword() );
        }
        if ( cred.getSsoTicket() != null )
        {
            data.setSSOTicket( cred.getSsoTicket() );
        }
        if ( cred.getUser() != null )
        {
            data.setUser( cred.getUser() );
        }
        if ( cred.getX509Certificate() != null )
        {
            data.setX509Certificate( cred.getX509Certificate() );
        }
    }

    private void endStatefulConnection()
    {
        try
        {
            jcoContext.end( destination );
        }
        catch ( JCoException e )
        {
            // TODO maybe just log this error? Can we go on though? Write test for JCo
            throw new HibersapException( "JCo connection could not be ended", e );
        }
        finally
        {
            destination = null;
        }
    }

    private JCoCustomDestination getCustomDestination( JCoDestination dest, Credentials cred )
    {
        JCoCustomDestination custDest = dest.createCustomDestination();
        UserData data = custDest.getUserLogonData();
        copyCredentialsToUserData( cred, data );
        return custDest;
    }

    private JCoRepository getRepository()
    {
        try
        {
            return destination.getRepository();
        }
        catch ( JCoException e )
        {
            throw new HibersapException( "Can not get repository from destination " + destination.getDestinationName(),
                                         e );
        }
    }

    private void startStatefulConnection()
    {
        destination = JCoEnvironment.getDestination( destinationName );

        if ( jcoContext.isStateful( destination ) )
        {
            throw new RuntimeException( "A stateful JCo session was already started for the given destination "
                + "in the current thread." );
        }

        if ( credentials != null )
        {
            destination = getCustomDestination( destination, credentials );
        }

        jcoContext.begin( destination );
    }
}