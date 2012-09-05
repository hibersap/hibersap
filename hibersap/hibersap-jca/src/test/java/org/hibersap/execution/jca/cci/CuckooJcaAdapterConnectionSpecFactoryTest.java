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
