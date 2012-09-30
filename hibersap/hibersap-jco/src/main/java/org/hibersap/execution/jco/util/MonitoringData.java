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

package org.hibersap.execution.jco.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MonitoringData
{
    private String systemDescription;
    private int peakLimit;
    private int poolCapacity;
    private int maxUsedCount;
    private int pooledConnectionCount;
    private int usedConnectionCount;
    private Date lastActivity;
    private final List<ConnectionData> connections = new ArrayList<ConnectionData>(  );

    public List<ConnectionData> getConnections()
    {
        return connections;
    }

    public Date getLastActivity()
    {
        return lastActivity;
    }

    public int getMaxUsedCount()
    {
        return maxUsedCount;
    }

    public int getPeakLimit()
    {
        return peakLimit;
    }

    public int getPoolCapacity()
    {
        return poolCapacity;
    }

    public int getPooledConnectionCount()
    {
        return pooledConnectionCount;
    }

    public String getSystemDescription()
    {
        return systemDescription;
    }

    public int getUsedConnectionCount()
    {
        return usedConnectionCount;
    }

    public void setSystemDescription( String systemDescription )
    {
        this.systemDescription = systemDescription;
    }

    public void setPeakLimit( int peakLimit )
    {
        this.peakLimit = peakLimit;
    }

    public void setPoolCapacity( int poolCapacity )
    {
        this.poolCapacity = poolCapacity;
    }

    public void setMaxUsedCount( int maxUsedCount )
    {
        this.maxUsedCount = maxUsedCount;
    }

    public void setPooledConnectionCount( int pooledConnectionCount )
    {
        this.pooledConnectionCount = pooledConnectionCount;
    }

    public void setUsedConnectionCount( int usedConnectionCount )
    {
        this.usedConnectionCount = usedConnectionCount;
    }

    public void setLastActivity( Date lastActivity )
    {
        this.lastActivity = lastActivity;
    }

    public void addConnectionData( ConnectionData connectionData )
    {
        this.connections.add( connectionData );
    }

    @Override
    public String toString()
    {
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

    public static class ConnectionData
    {
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

        public String getAbapClient()
        {
            return abapClient;
        }

        public String getAbapHost()
        {
            return abapHost;
        }

        public String getAbapLanguage()
        {
            return abapLanguage;
        }

        public String getAbapSystemNumber()
        {
            return abapSystemNumber;
        }

        public String getAbapUser()
        {
            return abapUser;
        }

        public String getApplicationName()
        {
            return applicationName;
        }

        public String getConnectionHandle()
        {
            return connectionHandle;
        }

        public String getConnectionType()
        {
            return connectionType;
        }

        public String getConvId()
        {
            return convId;
        }

        public String getDsrPassport()
        {
            return dsrPassport;
        }

        public String getFunctionModuleName()
        {
            return functionModuleName;
        }

        public String getGroupName()
        {
            return groupName;
        }

        public Date getLastActivity()
        {
            return lastActivity;
        }

        public String getProtocol()
        {
            return protocol;
        }

        public String getSessionId()
        {
            return sessionId;
        }

        public String getState()
        {
            return state;
        }

        public String getSystemId()
        {
            return systemId;
        }

        public String getThreadId()
        {
            return threadId;
        }

        public String getThreadName()
        {
            return threadName;
        }

        public void setAbapClient( String abapClient )
        {
            this.abapClient = abapClient;
        }

        public void setAbapHost( String abapHost )
        {
            this.abapHost = abapHost;
        }

        public void setAbapLanguage( String abapLanguage )
        {
            this.abapLanguage = abapLanguage;
        }

        public void setAbapSystemNumber( String abapSystemNumber )
        {
            this.abapSystemNumber = abapSystemNumber;
        }

        public void setAbapUser( String abapUser )
        {
            this.abapUser = abapUser;
        }

        public void setApplicationName( String applicationName )
        {
            this.applicationName = applicationName;
        }

        public void setConnectionHandle( String connectionHandle )
        {
            this.connectionHandle = connectionHandle;
        }

        public void setConnectionType( String connectionType )
        {
            this.connectionType = connectionType;
        }

        public void setConvId( String convId )
        {
            this.convId = convId;
        }

        public void setDSRPassport( String dsrPassport )
        {
            this.dsrPassport = dsrPassport;
        }

        public void setFunctionModuleName( String functionModuleName )
        {
            this.functionModuleName = functionModuleName;
        }

        public void setGroupName( String groupName )
        {
            this.groupName = groupName;
        }

        public void setLastActivity( Date lastActivity )
        {
            this.lastActivity = lastActivity;
        }

        public void setProtocol( String protocol )
        {
            this.protocol = protocol;
        }

        public void setSessionId( String sessionId )
        {
            this.sessionId = sessionId;
        }

        public void setState( String state )
        {
            this.state = state;
        }

        public void setSystemId( String systemId )
        {
            this.systemId = systemId;
        }

        public void setThreadId( String threadId )
        {
            this.threadId = threadId;
        }

        public void setThreadName( String threadName )
        {
            this.threadName = threadName;
        }

        @Override
        public String toString()
        {
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
