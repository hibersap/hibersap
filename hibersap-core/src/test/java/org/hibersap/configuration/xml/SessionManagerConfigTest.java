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

package org.hibersap.configuration.xml;

import org.hibersap.configuration.ConfigurationTest;
import org.junit.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

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

        assertThat( cfg.getContext() ).isEqualTo( "context" );
        assertThat( cfg.getJcaConnectionFactory() ).isEqualTo( "jcaConnectionFactory" );
        assertThat( cfg.getJcaConnectionSpecFactory() ).isEqualTo( "jcaConnectionSpecFactory" );
        assertThat( cfg.getName() ).isEqualTo( "newName" );
        assertThat( cfg.getProperties() ).hasSize( 2 );
        assertThat( cfg.getProperty( "key1" ) ).isEqualTo( "value1" );
        assertThat( cfg.getProperty( "key2" ) ).isEqualTo( "value2" );

        List<String> annotatedClasses = cfg.getAnnotatedClasses();
        assertThat( annotatedClasses ).hasSize( 2 );
        assertThat( annotatedClasses ).contains( Integer.class.getName(), String.class.getName() );

        assertThat( cfg.getValidationMode() ).isSameAs( ValidationMode.CALLBACK );
    }

    @Test
    public void testDefaultValues()
            throws Exception
    {
        SessionManagerConfig cfg = new SessionManagerConfig( "name" );
        assertThat( cfg.getContext() ).isEqualTo( "org.hibersap.execution.jco.JCoContext" );
        assertThat( cfg.getJcaConnectionSpecFactory() ).isEqualTo(
                "org.hibersap.execution.jca.cci.SapBapiJcaAdapterConnectionSpecFactory" );
        assertThat( cfg.getValidationMode() ).isSameAs( ValidationMode.AUTO );
    }
}
