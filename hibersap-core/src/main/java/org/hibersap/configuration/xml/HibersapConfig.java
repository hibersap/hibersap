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

package org.hibersap.configuration.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.ConfigurationException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( name = "", propOrder = {"sessionManagers"} )
@XmlRootElement( name = "hibersap" )
public final class HibersapConfig implements Serializable
{
    @XmlTransient
    public static final String NAMESPACE = "http://hibersap.org/xml/ns/hibersap-config";

    @XmlTransient
    private static final Log LOG = LogFactory.getLog( HibersapConfig.class );

    @XmlTransient
    private static final long serialVersionUID = 1;

    @XmlElement( name = "session-manager", required = true )
    protected List<SessionManagerConfig> sessionManagers = new ArrayList<SessionManagerConfig>();

    public HibersapConfig()
    {
        LOG.trace( "created" );
    }

    HibersapConfig( final SessionManagerConfig sessionManager )
    {
        this.sessionManagers.add( sessionManager );
    }

    public void setSessionManagers( final List<SessionManagerConfig> sessionManagers )
    {
        this.sessionManagers.clear();
        this.sessionManagers.addAll( sessionManagers );
    }

    public List<SessionManagerConfig> getSessionManagers()
    {
        return sessionManagers;
    }

    public SessionManagerConfig getSessionManager( String name )
    {
        for ( SessionManagerConfig config : sessionManagers )
        {
            if ( config.getName().equals( name ) )
            {
                return config;
            }
        }
        throw new ConfigurationException( "No configuration found for Session Manager name '" + name + "'" );
    }

    public SessionManagerConfig addSessionManager( String name )
    {
        SessionManagerConfig config = new SessionManagerConfig( name );
        sessionManagers.add( config );
        return config;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }

        HibersapConfig that = ( HibersapConfig ) o;

        if ( sessionManagers != null ? !sessionManagers.equals( that.sessionManagers ) : that.sessionManagers != null )
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return sessionManagers != null ? sessionManagers.hashCode() : 0;
    }
}
