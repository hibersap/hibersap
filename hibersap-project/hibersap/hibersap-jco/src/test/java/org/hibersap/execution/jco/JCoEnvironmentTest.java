/*
 * Copyright (c) 2009, 2011 akquinet tech@spree GmbH.
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

import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.ext.Environment;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Test;

import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hibersap.execution.jco.JCoEnvironmentTest.IsJCoDestinationRegistered.isRegisteredWithJCo;

public class JCoEnvironmentTest
{
    private static final Properties JCO_PROPERTIES_DUMMY = new Properties();

    private static final String DESTINATION_1 = "dest_1";
    private static final String DESTINATION_2 = "dest_2";

    @After
    public void unregisterDestinationDataProvidersAndDestinations()
    {
        JCoEnvironment.unregisterDestination( DESTINATION_1 );
        JCoEnvironment.unregisterDestination( DESTINATION_2 );
        assertThat( Environment.isDestinationDataProviderRegistered(), is( false ) );
    }

    @Test
    public void destinationProviderIsRegisteredWithJCoWhenFirstDestinationGetsRegistered()
    {
        JCoEnvironment.registerDestination( DESTINATION_1, JCO_PROPERTIES_DUMMY );

        assertThat( Environment.isDestinationDataProviderRegistered(), is( true ) );
    }

    @Test
    public void destinationIsAddedToJCoWhenDestinationGetsRegistered()
    {
        JCoEnvironment.registerDestination( DESTINATION_1, JCO_PROPERTIES_DUMMY );

        assertThat( DESTINATION_1, isRegisteredWithJCo() );
    }

    @Test
    public void destinationProviderIsUnregisteredFromJCoWhenLastDestinationGetsUnregistered()
    {
        assertThat( Environment.isDestinationDataProviderRegistered(), is( false ) );

        JCoEnvironment.registerDestination( DESTINATION_1, JCO_PROPERTIES_DUMMY );
        JCoEnvironment.unregisterDestination( DESTINATION_1 );

        assertThat( Environment.isDestinationDataProviderRegistered(), is( false ) );
    }

    @Test
    public void destinationIsRemovedFromJCoWhenDestinationGetsUnregistered()
    {
        JCoEnvironment.registerDestination( DESTINATION_1, JCO_PROPERTIES_DUMMY );
        JCoEnvironment.unregisterDestination( DESTINATION_1 );

        assertThat( DESTINATION_1, not( isRegisteredWithJCo() ) );
    }

    @Test
    public void canRegisterTwoDestinations()
    {
        JCoEnvironment.registerDestination( DESTINATION_1, JCO_PROPERTIES_DUMMY );
        JCoEnvironment.registerDestination( DESTINATION_2, JCO_PROPERTIES_DUMMY );

        assertThat( DESTINATION_1, isRegisteredWithJCo() );
        assertThat( DESTINATION_2, isRegisteredWithJCo() );
    }

    @Test
    public void destinationProviderIsUnregisteredFromJCoWhenTwoDestinationsAreRegisteredAndUnregistered()
    {
        JCoEnvironment.registerDestination( DESTINATION_1, JCO_PROPERTIES_DUMMY );
        JCoEnvironment.registerDestination( DESTINATION_2, JCO_PROPERTIES_DUMMY );
        JCoEnvironment.unregisterDestination( DESTINATION_1 );
        JCoEnvironment.unregisterDestination( DESTINATION_2 );

        assertThat( Environment.isDestinationDataProviderRegistered(), is( false ) );
    }

    static class IsJCoDestinationRegistered extends TypeSafeMatcher<String>
    {
        public boolean matchesSafely( String name )
        {
            try
            {
                JCoDestinationManager.getDestination( name );
                return true;
            }
            catch ( JCoException e )
            {
                return false;
            }
        }

        public void describeTo( Description description )
        {
            description.appendText( "registered" );
        }

        @Factory
        public static Matcher<String> isRegisteredWithJCo()
        {
            return new IsJCoDestinationRegistered();
        }
    }
}
