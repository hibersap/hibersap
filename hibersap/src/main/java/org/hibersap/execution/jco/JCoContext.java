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

import org.apache.commons.lang.StringUtils;
import org.hibersap.HibersapException;
import org.hibersap.configuration.Environment;
import org.hibersap.execution.Connection;
import org.hibersap.session.Context;

/**
 * @author Carsten Erker
 */
public class JCoContext
    implements Context
{
    private String destinationName;

    /*
     * (non-Javadoc)
     * 
     * @see org.hibersap.execution.jco.DestinationProvider#configure(java.util.Properties)
     */
    public void configure( Properties props )
        throws HibersapException
    {
        Properties jcoProperties = new Properties();
        for ( Object key : props.keySet() )
        {
            String keyStr = (String) key;
            if ( keyStr.startsWith( "hibersap.jco" ) )
            {
                String jcoKey = keyStr.substring( "hibersap.".length() );
                String value = props.getProperty( keyStr );
                jcoProperties.put( jcoKey, value );
            }
        }

        destinationName = props.getProperty( Environment.SESSION_FACTORY_NAME );
        if ( StringUtils.isEmpty( destinationName ) )
        {
            throw new HibersapException( "A session factory name must be specified in property "
                + Environment.SESSION_FACTORY_NAME );
        }

        JCoEnvironment.registerDestination( destinationName, jcoProperties );
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hibersap.execution.jco.DestinationProvider#reset()
     */
    public void reset()
    {
        JCoEnvironment.unregisterDestination( destinationName );
        destinationName = null;
    }

    public Connection getConnection()
    {
        return new JCoConnection( destinationName );
    }
}
