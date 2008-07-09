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

import org.apache.commons.lang.StringUtils;
import org.hibersap.HibersapException;
import org.hibersap.configuration.Environment;

import com.sap.mw.jco.IRepository;
import com.sap.mw.jco.JCO;


/**
 * @author Carsten Erker
 */
public abstract class JCoConnectionProvider
{
    private final Properties jcoProperties = new Properties();

    public void configure( Properties props )
        throws HibersapException
    {
        jcoProperties.clear();
        for ( Object key : props.keySet() )
        {
            String keyStr = (String) key;
            if ( keyStr.startsWith( "hibersap.jco.client" ) )
            {
                String jcoKey = keyStr.substring( "hibersap.".length() );
                jcoProperties.put( jcoKey, props.getProperty( keyStr ) );
            }
        }
        String repository = props.getProperty( Environment.REPOSITORY_NAME );
        if ( StringUtils.isEmpty( repository ) )
        {
            throw new HibersapException( "A repository name must be specified in property "
                + Environment.REPOSITORY_NAME );
        }
        jcoProperties.put( Environment.REPOSITORY_NAME, repository );
    }

    protected Properties getJcoProperties()
    {
        return this.jcoProperties;
    }

    public IRepository getRepository( JCO.Client client )
    {
        String name = getJcoProperties().getProperty( Environment.REPOSITORY_NAME );
        return JCO.createRepository( name, client );
    }
}
