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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MonitoringData {

    private final List<ConnectionData> connections = new ArrayList<ConnectionData>();
    private String systemDescription;
    private int peakLimit;
    private int poolCapacity;
    private int maxUsedCount;
    private int pooledConnectionCount;
    private int usedConnectionCount;
    private Date lastActivity;

    public List<ConnectionData> getConnections() {
        return connections;
    }

    public Date getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity( final Date lastActivity ) {
        this.lastActivity = lastActivity;
    }

    public int getMaxUsedCount() {
        return maxUsedCount;
    }

    public void setMaxUsedCount( final int maxUsedCount ) {
        this.maxUsedCount = maxUsedCount;
    }

    public int getPeakLimit() {
        return peakLimit;
    }

    public void setPeakLimit( final int peakLimit ) {
        this.peakLimit = peakLimit;
    }

    public int getPoolCapacity() {
        return poolCapacity;
    }

    public void setPoolCapacity( final int poolCapacity ) {
        this.poolCapacity = poolCapacity;
    }

    public int getPooledConnectionCount() {
        return pooledConnectionCount;
    }

    public void setPooledConnectionCount( final int pooledConnectionCount ) {
        this.pooledConnectionCount = pooledConnectionCount;
    }

    public String getSystemDescription() {
        return systemDescription;
    }

    public void setSystemDescription( final String systemDescription ) {
        this.systemDescription = systemDescription;
    }

    public int getUsedConnectionCount() {
        return usedConnectionCount;
    }

    public void setUsedConnectionCount( final int usedConnectionCount ) {
        this.usedConnectionCount = usedConnectionCount;
    }

    public void addConnectionData( final ConnectionData connectionData ) {
        this.connections.add( connectionData );
    }

    @Override
    public String toString() {
        return "MonitoringData{" +
                "connections=" + connections +
                ", systemDescription='" + systemDescription + '\'' +
                ", peakLimit=" + peakLimit +
                ", poolCapacity=" + poolCapacity +
                ", maxUsedCount=" + maxUsedCount +
                ", pooledConnectionCount=" + pooledConnectionCount +
                ", usedConnectionCount=" + usedConnectionCount +
                ", lastActivity=" + lastActivity +
                '}';
    }

    public static class ConnectionData {

        private String abapClient;
        private String abapHost;
        private String abapLanguage;
        private String abapSystemNumber;
        private String abapUser;
        private String applicationName;
        private String connectionHandle;
        private String connectionType;
        private String convId;
        private String dsrPassport;
        private String functionModuleName;
        private String groupName;
        private Date lastActivity;
        private String protocol;
        private String sessionId;
        private String state;
        private String systemId;
        private String threadId;
        private String threadName;

        public String getAbapClient() {
            return abapClient;
        }

        public void setAbapClient( final String abapClient ) {
            this.abapClient = abapClient;
        }

        public String getAbapHost() {
            return abapHost;
        }

        public void setAbapHost( final String abapHost ) {
            this.abapHost = abapHost;
        }

        public String getAbapLanguage() {
            return abapLanguage;
        }

        public void setAbapLanguage( final String abapLanguage ) {
            this.abapLanguage = abapLanguage;
        }

        public String getAbapSystemNumber() {
            return abapSystemNumber;
        }

        public void setAbapSystemNumber( final String abapSystemNumber ) {
            this.abapSystemNumber = abapSystemNumber;
        }

        public String getAbapUser() {
            return abapUser;
        }

        public void setAbapUser( final String abapUser ) {
            this.abapUser = abapUser;
        }

        public String getApplicationName() {
            return applicationName;
        }

        public void setApplicationName( final String applicationName ) {
            this.applicationName = applicationName;
        }

        public String getConnectionHandle() {
            return connectionHandle;
        }

        public void setConnectionHandle( final String connectionHandle ) {
            this.connectionHandle = connectionHandle;
        }

        public String getConnectionType() {
            return connectionType;
        }

        public void setConnectionType( final String connectionType ) {
            this.connectionType = connectionType;
        }

        public String getConvId() {
            return convId;
        }

        public void setConvId( final String convId ) {
            this.convId = convId;
        }

        public String getDsrPassport() {
            return dsrPassport;
        }

        public String getFunctionModuleName() {
            return functionModuleName;
        }

        public void setFunctionModuleName( final String functionModuleName ) {
            this.functionModuleName = functionModuleName;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName( final String groupName ) {
            this.groupName = groupName;
        }

        public Date getLastActivity() {
            return lastActivity;
        }

        public void setLastActivity( final Date lastActivity ) {
            this.lastActivity = lastActivity;
        }

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol( final String protocol ) {
            this.protocol = protocol;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId( final String sessionId ) {
            this.sessionId = sessionId;
        }

        public String getState() {
            return state;
        }

        public void setState( final String state ) {
            this.state = state;
        }

        public String getSystemId() {
            return systemId;
        }

        public void setSystemId( final String systemId ) {
            this.systemId = systemId;
        }

        public String getThreadId() {
            return threadId;
        }

        public void setThreadId( final String threadId ) {
            this.threadId = threadId;
        }

        public String getThreadName() {
            return threadName;
        }

        public void setThreadName( final String threadName ) {
            this.threadName = threadName;
        }

        public void setDSRPassport( final String dsrPassport ) {
            this.dsrPassport = dsrPassport;
        }

        @Override
        public String toString() {
            return "ConnectionData{" +
                    "abapClient='" + abapClient + '\'' +
                    ", abapHost='" + abapHost + '\'' +
                    ", abapLanguage='" + abapLanguage + '\'' +
                    ", abapSystemNumber='" + abapSystemNumber + '\'' +
                    ", abapUser='" + abapUser + '\'' +
                    ", applicationName='" + applicationName + '\'' +
                    ", connectionHandle='" + connectionHandle + '\'' +
                    ", connectionType='" + connectionType + '\'' +
                    ", convId='" + convId + '\'' +
                    ", dsrPassport='" + dsrPassport + '\'' +
                    ", functionModuleName='" + functionModuleName + '\'' +
                    ", groupName='" + groupName + '\'' +
                    ", lastActivity=" + lastActivity +
                    ", protocol='" + protocol + '\'' +
                    ", sessionId='" + sessionId + '\'' +
                    ", state='" + state + '\'' +
                    ", systemId='" + systemId + '\'' +
                    ", threadId='" + threadId + '\'' +
                    ", threadName='" + threadName + '\'' +
                    '}';
        }
    }
}
