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

package org.hibersap.it.session;

import com.sap.conn.jco.monitor.JCoConnectionData;
import com.sap.conn.jco.monitor.JCoConnectionMonitor;
import org.hibersap.SapException;
import org.hibersap.it.AbstractBapiTest;
import org.hibersap.it.bapi.RfcPing;
import org.hibersap.session.Credentials;
import org.hibersap.session.Session;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CustomCredentialsTest extends AbstractBapiTest
{
    @Test
    public void connectToSapUsingCustomCredentials() throws Exception
    {

        final Credentials credentials = new Credentials().setUser( "sapuser2" )
                .setPassword( "password" ).setLanguage( "DE" );
        callBapiWith( credentials );

        final JCoConnectionData connectionUsed = getConnectionDataUsed();

        // these parameters should be used instead of the configured ones
        assertNotNull( connectionUsed );
        assertEquals( "SAPUSER2", connectionUsed.getAbapUser() );
        assertEquals( "DE", connectionUsed.getAbapLanguage() );
    }

    private void callBapiWith( Credentials credentials )
    {
        final Session session = sessionManager.openSession( credentials );

        // make sure a connection to SAP was established
        try
        {
            session.execute( new RfcPing() );
        }
        catch ( final SapException e )
        {
            // expected when there are no lines found
        }
        finally
        {
            session.close();
        }
    }

    private JCoConnectionData getConnectionDataUsed()
    {
        final List<? extends JCoConnectionData> list = JCoConnectionMonitor
                .getConnectionsData();
        for ( final JCoConnectionData data : list )
        {
            if ( "RFC_PING".equals( data.getFunctionModuleName() ) )
            {
                return data;
            }
        }
        return null;
    }
}
