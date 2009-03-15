package org.hibersap.session;

/**
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Iterator;
import java.util.List;

import org.hibersap.SapException;
import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.examples.AbstractHibersapTest;
import org.hibersap.examples.flightlist.FlightListBapi;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sap.conn.jco.monitor.JCoConnectionData;
import com.sap.conn.jco.monitor.JCoConnectionMonitor;

public class CustomCredentialsTest
    extends AbstractHibersapTest
{
    private AnnotationConfiguration configuration = new AnnotationConfiguration( "A12" );

    private SessionManagerImpl sessionManager;

    @Before
    public void setup()
    {
        sessionManager = (SessionManagerImpl) configuration.buildSessionManager();
    };

    @After
    public void reset()
    {
        if ( sessionManager != null )
            sessionManager.reset();
    }

    @Test
    public void connectToSapWithCustomCredentials()
        throws Exception
    {

        Credentials credentials = new Credentials().setUser( "CERKER" ).setPassword( "password" ).setLanguage( "DE" );

        Session session = sessionManager.openSession( credentials );

        // make sure a connection to SAP was established
        try
        {
            session.execute( new FlightListBapi( "BERLIN", "DE", "FRANKFURT", "DE", "LH", false, 1 ) );
        }
        catch ( SapException e )
        {
            // expected when there are no lines found
        }
        finally
        {
            session.close();
        }

        JCoConnectionData connectionUsed = getConnectionDataUsed();
        assertNotNull( connectionUsed );

        // these parameters should be used instead of the configured ones
        assertEquals( "CERKER", connectionUsed.getAbapUser() );
        assertEquals( "DE", connectionUsed.getAbapLanguage() );

        // this parameter should not be overwritten, the configuration should be used
        assertEquals( "800", connectionUsed.getAbapClient() );
    }

    private JCoConnectionData getConnectionDataUsed()
    {
        List<? extends JCoConnectionData> list = JCoConnectionMonitor.getConnectionsData();
        for ( Iterator<? extends JCoConnectionData> iterator = list.iterator(); iterator.hasNext(); )
        {
            JCoConnectionData data = iterator.next();
            if ( "BAPI_SFLIGHT_GETLIST".equals( data.getFunctionModuleName() ) )
            {
                return data;
            }
        }
        return null;
    }
}
