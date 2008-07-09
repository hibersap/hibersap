package org.hibersap.configuration;

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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.util.Properties;

import org.junit.Test;


/**
 * @author Carsten Erker
 */
public class ConfigurationTest
{
    private Configuration config = new Configuration()
    {
        // nothing to overwrite
    };

    @Test
    public void getProperties()
        throws Exception
    {
        // initializes with system properties
        assertNotNull( config.getProperty( "java.runtime.name" ) );

        // overwrites property
        config.setProperty( "java.runtime.name", "test" );
        assertEquals( "test", config.getProperty( "java.runtime.name" ) );

        // overwrites all properties
        Properties properties = new Properties();
        properties.setProperty( "testkey", "testvalue" );
        config.setProperties( properties );
        assertEquals( 1, config.getProperties().size() );
        assertEquals( "testvalue", config.getProperty( "testkey" ) );
    }
}
