/*
 * JBoss, Home of Professional Open Source Copyright 2006, JBoss Inc., and individual contributors
 * as indicated by the @authors tag. See the copyright.txt in the distribution for a full listing of
 * individual contributors.
 * 
 * This is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation; either version 2.1 of the
 * License, or (at your option) any later version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this
 * software; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor,
 * Boston, MA 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.hibersap.configuration.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.ConfigurationException;

@XmlRootElement(name = "hibersap", namespace = HibersapConfig.NAMESPACE)
@XmlAccessorType(XmlAccessType.PROPERTY)
public class HibersapConfig
{
    public final static String NAMESPACE = "urn:hibersap:hibersap-configuration:1.0";

    @XmlTransient
    private static Log LOG = LogFactory.getLog( HibersapConfig.class );

    @XmlTransient
    private static final long serialVersionUID = 1;

    private final List<SessionManagerConfig> sessionManagers = new ArrayList<SessionManagerConfig>();

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

    @XmlElement(name = "session-manager", required = true, namespace = HibersapConfig.NAMESPACE)
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
}
