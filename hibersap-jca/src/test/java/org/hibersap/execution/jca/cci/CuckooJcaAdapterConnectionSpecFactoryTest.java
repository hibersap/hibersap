/*
 * Copyright (c) 2008-2017 akquinet tech@spree GmbH
 *
 * This file is part of Hibersap.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hibersap.execution.jca.cci;

import org.cuckoo.ra.cci.ApplicationProperties;
import org.hibersap.session.Credentials;
import org.junit.Test;
import static org.fest.assertions.Assertions.assertThat;

public class CuckooJcaAdapterConnectionSpecFactoryTest {

    private final CuckooJcaAdapterConnectionSpecFactory factory = new CuckooJcaAdapterConnectionSpecFactory();
    private final Credentials credentials = new Credentials()
            .setUser("user")
            .setPassword("password")
            .setLanguage("language")
            .setClient("client")
            .setAliasUser("aliasUser")
            .setSsoTicket("ssoTicket")
            .setX509Certificate("x509Certificate");

    @Test
    public void createConnectionSpec() throws Exception {
        ApplicationProperties connectionSpec = (ApplicationProperties) factory.createConnectionSpec(credentials);

        assertThat(connectionSpec.getUser()).isEqualTo("user");
        assertThat(connectionSpec.getPassword()).isEqualTo("password");
        // TODO comment in when Cuckoo is fixed
//        assertThat( connectionSpec.getLanguage() ).isEqualTo( "language" );
//        assertThat( connectionSpec.getClient() ).isEqualTo( "client" );
//        assertThat( connectionSpec.getAliasUser() ).isEqualTo( "aliasUser" );
//        assertThat( connectionSpec.getSsoTicket() ).isEqualTo( "ssoTicket" );
//        assertThat( connectionSpec.getX509Certificate() ).isEqualTo( "x509Certificate" );
    }

    @Test
    public void createConnectionSpecWithNullValues() throws Exception {
        ApplicationProperties connectionSpec = (ApplicationProperties) factory
                .createConnectionSpec(new Credentials());

        assertThat(connectionSpec.getUser()).isNull();
        assertThat(connectionSpec.getPassword()).isNull();
        assertThat(connectionSpec.getLanguage()).isNull();
        assertThat(connectionSpec.getClient()).isNull();
        assertThat(connectionSpec.getAliasUser()).isNull();
        assertThat(connectionSpec.getSsoTicket()).isNull();
        assertThat(connectionSpec.getX509Certificate()).isNull();
    }
}
