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

import org.hibersap.configuration.ConfigurationTest;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SessionManagerConfigTest
{
    @Test
    public void testBuild()
            throws Exception
    {
        SessionManagerConfig cfg = new SessionManagerConfig( "name" )
                .setContext( "context" )
                .setJcaConnectionFactory( "jcaConnectionFactory" )
                .setJcaConnectionSpecFactory( "jcaConnectionSpecFactory" )
                .setName( "newName" )
                .setProperty( "key1", "value1" )
                .setProperty( "key2", "value2" )
                .addAnnotatedClass( String.class )
                .addAnnotatedClass( Integer.class )
                .addExecutionInterceptorClass( ConfigurationTest.ExecutionInterceptorDummy.class )
                .addBapiInterceptorClass( ConfigurationTest.BapiInterceptorDummy.class )
                .setValidationMode( ValidationMode.CALLBACK );

        assertThat( cfg.getContext(), equalTo( "context" ) );
        assertThat( cfg.getJcaConnectionFactory(), equalTo( "jcaConnectionFactory" ) );
        assertThat( cfg.getJcaConnectionSpecFactory(), equalTo( "jcaConnectionSpecFactory" ) );
        assertThat( cfg.getName(), equalTo( "newName" ) );
        assertThat( cfg.getProperties().size(), is( 2 ) );
        assertThat( cfg.getProperty( "key1" ), equalTo( "value1" ) );
        assertThat( cfg.getProperty( "key2" ), equalTo( "value2" ) );

        List<String> annotatedClasses = cfg.getAnnotatedClasses();
        assertThat( annotatedClasses.size(), is( 2 ) );
        assertThat( annotatedClasses, hasItems( Integer.class.getName(), String.class.getName() ) );

        assertThat( cfg.getValidationMode(), is( ValidationMode.CALLBACK ) );
    }

    @Test
    public void testDefaultValues()
            throws Exception
    {
        SessionManagerConfig cfg = new SessionManagerConfig( "name" );
        assertThat( cfg.getContext(), equalTo( "org.hibersap.execution.jco.JCoContext" ) );
        assertThat( cfg.getJcaConnectionSpecFactory(),
                equalTo( "org.hibersap.execution.jca.cci.SapBapiJcaAdapterConnectionSpecFactory" ) );
        assertThat( cfg.getValidationMode(), is( ValidationMode.AUTO ) );
    }
}
