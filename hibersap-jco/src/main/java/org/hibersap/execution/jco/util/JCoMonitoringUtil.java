/*
 * Copyright (c) 2008-2014 akquinet tech@spree GmbH
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

package org.hibersap.execution.jco.util;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.monitor.JCoConnectionData;
import com.sap.conn.jco.monitor.JCoDestinationMonitor;

import java.util.Date;

public class JCoMonitoringUtil {

    public MonitoringData getMonitoringData( final String destinationName ) throws JCoException {
        JCoDestination dest = JCoDestinationManager.getDestination( destinationName );

        MonitoringData data = new MonitoringData();

        data.setSystemDescription( dest.getAttributes().toString() );

        JCoDestinationMonitor monitor = dest.getMonitor();

        // Returns the maximum number of active connections that can be used simultaneously
        data.setPeakLimit( monitor.getPeakLimit() );

        // Maximum number of idle connections kept open by the destination.
        // A value of 0 has the effect that there is no connection pooling,
        // i.e. connections will be closed after each request
        data.setPoolCapacity( monitor.getPoolCapacity() );

        // Returns the maximum number of simultaneously used client connections
        data.setMaxUsedCount( monitor.getMaxUsedCount() );

        // Returns the number of client connections that are currently held open
        data.setPooledConnectionCount( monitor.getPooledConnectionCount() );

        // Returns the number of client connections that are currently being used
        data.setUsedConnectionCount( monitor.getUsedConnectionCount() );

        // Returns the time stamp of the last activity done
        data.setLastActivity( new Date( monitor.getLastActivityTimestamp() ) );

        addConnectionData( data, monitor );

        return data;
    }

    private void addConnectionData( final MonitoringData data, final JCoDestinationMonitor monitor ) {
        for ( JCoConnectionData jCoConnectionData : monitor.getConnectionsData() ) {
            MonitoringData.ConnectionData connectionData = new MonitoringData.ConnectionData();

            connectionData.setAbapClient( jCoConnectionData.getAbapClient() );
            connectionData.setAbapHost( jCoConnectionData.getAbapHost() );
            connectionData.setAbapLanguage( jCoConnectionData.getAbapLanguage() );
            connectionData.setAbapSystemNumber( jCoConnectionData.getAbapSystemNumber() );
            connectionData.setAbapUser( jCoConnectionData.getAbapUser() );
            connectionData.setApplicationName( jCoConnectionData.getApplicationName() );
            connectionData.setConnectionHandle( jCoConnectionData.getConnectionHandleAsString() );
            connectionData.setConnectionType( jCoConnectionData.getConnectionType() );
            connectionData.setConvId( jCoConnectionData.getConvId() );
            connectionData.setDSRPassport( jCoConnectionData.getDSRPassportAsString() );
            connectionData.setFunctionModuleName( jCoConnectionData.getFunctionModuleName() );
            connectionData.setGroupName( jCoConnectionData.getGroupName() );
            connectionData.setLastActivity( new Date( jCoConnectionData.getLastActivityTimestamp() ) );
            connectionData.setProtocol( jCoConnectionData.getProtocol() );
            connectionData.setSessionId( jCoConnectionData.getSessionId() );
            connectionData.setState( jCoConnectionData.getStateAsString() );
            connectionData.setSystemId( jCoConnectionData.getSystemID() );
            connectionData.setThreadId( jCoConnectionData.getThreadIdAsString() );
            connectionData.setThreadName( jCoConnectionData.getThreadName() );
            data.addConnectionData( connectionData );
        }
    }
}
