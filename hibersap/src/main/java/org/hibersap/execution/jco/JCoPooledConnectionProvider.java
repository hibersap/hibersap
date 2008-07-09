package org.hibersap.execution.jco;

/*
 * Copyright (C) 2008 akquinet tech@spree GmbH
 * 
 * This file is part of Hibersap.
 *
 * Hibersap is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hibersap is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Hibersap.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.Properties;

import org.hibersap.HibersapException;
import org.hibersap.configuration.Environment;

import com.sap.mw.jco.JCO;


/**
 * @author Carsten Erker
 */
public class JCoPooledConnectionProvider
    extends JCoConnectionProvider
{
    private String poolName = null;

    private int poolSize = -1;

    @Override
    public void configure( Properties props )
        throws HibersapException
    {
        super.configure( props );
        poolName = props.getProperty( Environment.CONNECTION_POOL_NAME );
        poolSize = getMaxPoolSize();
        createPoolIfNecessary();
    }

    private void createPoolIfNecessary()
    {
        JCO.Pool pool = JCO.getClientPoolManager().getPool( poolName );
        if ( pool == null )
        {
            JCO.addClientPool( poolName, poolSize, getJcoProperties() );
        }
    }

    public JCO.Client getClient()
    {
        return JCO.getClient( getJcoProperties().getProperty( Environment.CONNECTION_POOL_NAME ) );
    }

    private int getMaxPoolSize()
    {
        String maxSizeStr = getJcoProperties().getProperty( Environment.CONNECTION_POOL_SIZE );
        int maxSize = Integer.parseInt( maxSizeStr );
        return maxSize;
    }

    public void releaseClient( JCO.Client client )
    {
        JCO.releaseClient( client );
    }
}
