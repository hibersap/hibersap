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

package org.hibersap.execution.jco;

import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.HibersapException;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * The Hibersap implementation of the JCo DestinationDataProvider. JCo destinations can be added and
 * removed at runtime.
 *
 * @author Carsten Erker
 */
public class JCoDataProvider implements DestinationDataProvider
{
    private static final Log LOG = LogFactory.getLog( JCoDataProvider.class );

    private final Map<String, Properties> propertiesForDestinationName = new HashMap<String, Properties>();

    private DestinationDataEventListener eventListener;

    public void addDestination( String destinationName, Properties properties )
    {
        LOG.debug( "Add destination " + destinationName + " to " + propertiesForDestinationName );

        // if ( wasAdded( destinationName ) )
        // {
        // throw new HibersapException( "A JCo destination named '" + destinationName +
        // "' was already registered" );
        // }
        propertiesForDestinationName.put( destinationName, properties );
        fireDestinationUpdatedEvent( destinationName );
    }

    public void removeDestination( String destinationName )
    {
        LOG.debug( "Remove destination " + destinationName + " from " + propertiesForDestinationName );

        propertiesForDestinationName.remove( destinationName );
        fireDestinationDeletedEvent( destinationName );
    }

    /**
     * {@inheritDoc}
     */
    public Properties getDestinationProperties( String destinationName )
    {
        if ( wasAdded( destinationName ) )
        {
            return propertiesForDestinationName.get( destinationName );
        }
        else
        {
            throw new HibersapException( "No JCo destination with name " + destinationName + " found" );
        }
    }

    public boolean wasAdded( String destinationName )
    {
        return propertiesForDestinationName.containsKey( destinationName );
    }

    public boolean hasDestinations()
    {
        return !propertiesForDestinationName.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public void setDestinationDataEventListener( DestinationDataEventListener eventListener )
    {
        this.eventListener = eventListener;
    }

    /**
     * {@inheritDoc}
     */
    public boolean supportsEvents()
    {
        return true;
    }

    private void fireDestinationUpdatedEvent( String destinationName )
    {
        if ( eventListener != null )
        {
            eventListener.updated( destinationName );
        }
    }

    private void fireDestinationDeletedEvent( String destinationName )
    {
        if ( eventListener != null )
        {
            eventListener.deleted( destinationName );
        }
    }
}