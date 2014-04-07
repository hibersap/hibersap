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

package org.hibersap.execution.jca.cci;

import org.cuckoo.ra.cci.ApplicationProperties;
import org.hibersap.session.Credentials;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class CuckooJcaAdapterConnectionSpecFactoryTest
{
    private final CuckooJcaAdapterConnectionSpecFactory factory = new CuckooJcaAdapterConnectionSpecFactory();
    private final Credentials credentials = new Credentials()
            .setUser( "user" )
            .setPassword( "password" )
            .setLanguage( "language" )
            .setClient( "client" )
            .setAliasUser( "aliasUser" )
            .setSsoTicket( "ssoTicket" )
            .setX509Certificate( "x509Certificate" );

    @Test
    public void createConnectionSpec() throws Exception
    {
        ApplicationProperties connectionSpec = ( ApplicationProperties ) factory.createConnectionSpec( credentials );

        assertThat( connectionSpec.getUser() ).isEqualTo( "user" );
        assertThat( connectionSpec.getPassword() ).isEqualTo( "password" );
        assertThat( connectionSpec.getLanguage() ).isEqualTo( "language" );
        assertThat( connectionSpec.getClient() ).isEqualTo( "client" );
        assertThat( connectionSpec.getAliasUser() ).isEqualTo( "aliasUser" );
        assertThat( connectionSpec.getSsoTicket() ).isEqualTo( "ssoTicket" );
        assertThat( connectionSpec.getX509Certificate() ).isEqualTo( "x509Certificate" );
    }

    @Test
    public void createConnectionSpecWithNullValues() throws Exception
    {
        ApplicationProperties connectionSpec = ( ApplicationProperties ) factory
                .createConnectionSpec( new Credentials() );

        assertThat( connectionSpec.getUser() ).isNull();
        assertThat( connectionSpec.getPassword() ).isNull();
        assertThat( connectionSpec.getLanguage() ).isNull();
        assertThat( connectionSpec.getClient() ).isNull();
        assertThat( connectionSpec.getAliasUser() ).isNull();
        assertThat( connectionSpec.getSsoTicket() ).isNull();
        assertThat( connectionSpec.getX509Certificate() ).isNull();
    }
}
