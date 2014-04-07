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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.HibersapException;
import org.hibersap.configuration.xml.Property;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.execution.Connection;
import org.hibersap.session.Context;

import java.util.List;
import java.util.Properties;

/*
 * Uses the SAP Java Connector to connect to SAP.
 * 
 * @author Carsten Erker
 */
public class JCoContext implements Context
{
    private static final Log LOG = LogFactory.getLog( JCoContext.class );

    private static final String JCO_PROPERTIES_PREFIX = "jco.";

    private String destinationName;

    /*
     * {@inheritDoc}
     */
    public void configure( final SessionManagerConfig config )
            throws HibersapException
    {
        LOG.trace( "configure JCo context" );

        final Properties jcoProperties = new Properties();
        List<Property> properties = config.getProperties();

        for ( Property property : properties )
        {
            String name = property.getName();
            if ( name.startsWith( JCO_PROPERTIES_PREFIX ) )
            {
                jcoProperties.put( name, property.getValue() );
            }
        }

        destinationName = config.getName();
        if ( StringUtils.isEmpty( destinationName ) )
        {
            throw new HibersapException( "A session manager name must be specified in Hibersap configuration" );
        }

        JCoEnvironment.registerDestination( destinationName, jcoProperties );
    }

    /*
     * {@inheritDoc}
     */
    public void close()
    {
        JCoEnvironment.unregisterDestination( destinationName );
        destinationName = null;
    }

    /*
     * {@inheritDoc}
     */
    public Connection getConnection()
    {
        return new JCoConnection( destinationName );
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( destinationName == null ) ? 0 : destinationName.hashCode() );
        return result;
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
        {
            return true;
        }
        if ( obj == null )
        {
            return false;
        }
        if ( getClass() != obj.getClass() )
        {
            return false;
        }
        JCoContext other = ( JCoContext ) obj;
        if ( destinationName == null )
        {
            if ( other.destinationName != null )
            {
                return false;
            }
        }
        else if ( !destinationName.equals( other.destinationName ) )
        {
            return false;
        }
        return true;
    }
}
