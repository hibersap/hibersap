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

import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.ext.Environment;
import org.fest.assertions.Condition;
import org.junit.After;
import org.junit.Test;

import java.util.Properties;

import static org.fest.assertions.Assertions.assertThat;

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
        assertThat( Environment.isDestinationDataProviderRegistered() ).isFalse();
    }

    @Test
    public void destinationProviderIsRegisteredWithJCoWhenFirstDestinationGetsRegistered()
    {
        JCoEnvironment.registerDestination( DESTINATION_1, JCO_PROPERTIES_DUMMY );

        assertThat( Environment.isDestinationDataProviderRegistered() ).isTrue();
    }

    @Test
    public void destinationIsAddedToJCoWhenDestinationGetsRegistered()
    {
        JCoEnvironment.registerDestination( DESTINATION_1, JCO_PROPERTIES_DUMMY );

        assertThat( DESTINATION_1 ).is( registeredWithJCo );
    }

    @Test
    public void destinationProviderIsUnregisteredFromJCoWhenLastDestinationGetsUnregistered()
    {
        assertThat( Environment.isDestinationDataProviderRegistered() ).isFalse();

        JCoEnvironment.registerDestination( DESTINATION_1, JCO_PROPERTIES_DUMMY );
        JCoEnvironment.unregisterDestination( DESTINATION_1 );

        assertThat( Environment.isDestinationDataProviderRegistered() ).isFalse();
    }

    @Test
    public void destinationIsRemovedFromJCoWhenDestinationGetsUnregistered()
    {
        JCoEnvironment.registerDestination( DESTINATION_1, JCO_PROPERTIES_DUMMY );
        JCoEnvironment.unregisterDestination( DESTINATION_1 );

        assertThat( DESTINATION_1 ).isNot( registeredWithJCo );
    }

    @Test
    public void canRegisterTwoDestinations()
    {
        JCoEnvironment.registerDestination( DESTINATION_1, JCO_PROPERTIES_DUMMY );
        JCoEnvironment.registerDestination( DESTINATION_2, JCO_PROPERTIES_DUMMY );

        assertThat( DESTINATION_1 ).is( registeredWithJCo );
        assertThat( DESTINATION_2 ).is( registeredWithJCo );
    }

    @Test
    public void destinationProviderIsUnregisteredFromJCoWhenTwoDestinationsAreRegisteredAndUnregistered()
    {
        JCoEnvironment.registerDestination( DESTINATION_1, JCO_PROPERTIES_DUMMY );
        JCoEnvironment.registerDestination( DESTINATION_2, JCO_PROPERTIES_DUMMY );
        JCoEnvironment.unregisterDestination( DESTINATION_1 );
        JCoEnvironment.unregisterDestination( DESTINATION_2 );

        assertThat( Environment.isDestinationDataProviderRegistered()).isFalse();
    }

    private final Condition<String> registeredWithJCo = new Condition<String>( "registeredWithJCo" )
    {
        @Override
        public boolean matches( String name )
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
    };
}
