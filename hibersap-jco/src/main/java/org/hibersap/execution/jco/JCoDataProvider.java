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
public class JCoDataProvider implements DestinationDataProvider {

    private static final Log LOG = LogFactory.getLog( JCoDataProvider.class );

    private final Map<String, Properties> propertiesForDestinationName = new HashMap<String, Properties>();

    private DestinationDataEventListener eventListener;

    public void addDestination( final String destinationName, final Properties properties ) {
        LOG.debug( "Add destination " + destinationName + " to " + propertiesForDestinationName );

        propertiesForDestinationName.put( destinationName, properties );
        fireDestinationUpdatedEvent( destinationName );
    }

    public void removeDestination( final String destinationName ) {
        LOG.debug( "Remove destination " + destinationName + " from " + propertiesForDestinationName );

        propertiesForDestinationName.remove( destinationName );
        fireDestinationDeletedEvent( destinationName );
    }

    /**
     * {@inheritDoc}
     */
    public Properties getDestinationProperties( final String destinationName ) {
        if ( wasAdded( destinationName ) ) {
            return propertiesForDestinationName.get( destinationName );
        } else {
            throw new HibersapException( "No JCo destination with name " + destinationName + " found" );
        }
    }

    public boolean wasAdded( final String destinationName ) {
        return propertiesForDestinationName.containsKey( destinationName );
    }

    public boolean hasDestinations() {
        return !propertiesForDestinationName.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public void setDestinationDataEventListener( final DestinationDataEventListener eventListener ) {
        this.eventListener = eventListener;
    }

    /**
     * {@inheritDoc}
     */
    public boolean supportsEvents() {
        return true;
    }

    private void fireDestinationUpdatedEvent( final String destinationName ) {
        if ( eventListener != null ) {
            eventListener.updated( destinationName );
        }
    }

    private void fireDestinationDeletedEvent( final String destinationName ) {
        if ( eventListener != null ) {
            eventListener.deleted( destinationName );
        }
    }
}