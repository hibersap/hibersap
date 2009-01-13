package org.hibersap.execution.jco;

/*
 * Copyright (C) 2008 akquinet tech@spree GmbH
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

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.HibersapException;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.ext.Environment;

/**
 * This class acts as a wrapper for the ugly static JCo classes.
 *
 * @author Carsten Erker
 */
public class JCoEnvironment
{
    private static final Log LOG = LogFactory.getLog( JCoEnvironment.class );

    /**
     * JCo's Environment class doesn't offer any methods to check if a provider class is already registered,
     * but we need to dynamically register destinations
     */
    private static final JCoDataProvider destinationDataProvider;

    static
    {
        destinationDataProvider = new JCoDataProvider();
        Environment.registerDestinationDataProvider( destinationDataProvider );
    }

    public static void registerDestination( String destinationName, Properties jcoProperties )
    {
        LOG.info( "Registering destination " + destinationName );
        destinationDataProvider.addDestination( destinationName, jcoProperties );
    }

    public static void unregisterDestination( String destinationName )
    {
        LOG.info( "Unregistering destination " + destinationName );
        destinationDataProvider.removeDestination( destinationName );
    }

    public static JCoDestination getDestination( String destinationName )
    {
        try
        {
            return JCoDestinationManager.getDestination( destinationName );
        }
        catch ( JCoException e )
        {
            throw new HibersapException( "Can not get the destination named '" + destinationName + "'" );
        }
    }
}
