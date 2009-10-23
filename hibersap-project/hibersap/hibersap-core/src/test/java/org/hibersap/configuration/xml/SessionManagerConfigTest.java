package org.hibersap.configuration.xml;

/**
 * Copyright (C) 2008-2009 akquinet tech@spree GmbH
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

public class SessionManagerConfigTest
{
    @Test
    public void testBuild()
        throws Exception
    {
        SessionManagerConfig cfg = new SessionManagerConfig( "name" ).setContext( "context" )
            .setJcaConnectionFactory( "jcaConnectionFactory" ).setJcaConnectionSpecFactory( "jcaConnectionSpecFactory" )
            .setName( "newName" ).setProperty( "key1", "value1" ).setProperty( "key2", "value2" )
            .addAnnotatedClass( String.class ).addAnnotatedClass( Integer.class );
        assertEquals( "context", cfg.getContext() );
        assertEquals( "jcaConnectionFactory", cfg.getJcaConnectionFactory() );
        assertEquals( "jcaConnectionSpecFactory", cfg.getJcaConnectionSpecFactory() );
        assertEquals( "newName", cfg.getName() );
        assertEquals( 2, cfg.getProperties().size() );
        assertEquals( "value1", cfg.getProperty( "key1" ) );
        assertEquals( "value2", cfg.getProperty( "key2" ) );
        Set<String> annotatedClasses = cfg.getAnnotatedClasses();
        assertEquals( 2, annotatedClasses.size() );
        assertTrue( annotatedClasses.contains( Integer.class.getName() ) );
        assertTrue( annotatedClasses.contains( String.class.getName() ) );
    }

    @Test
    public void testDefaultValues()
        throws Exception
    {
        SessionManagerConfig cfg = new SessionManagerConfig( "name" );
        assertEquals( "org.hibersap.execution.jco.JCoContext", cfg.getContext() );
        assertEquals( "org.hibersap.execution.jca.cci.SapBapiJcaAdapterConnectionSpecFactory", cfg
            .getJcaConnectionSpecFactory() );
    }
}
