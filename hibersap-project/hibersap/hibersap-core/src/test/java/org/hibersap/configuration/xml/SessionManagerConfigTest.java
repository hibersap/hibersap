package org.hibersap.configuration.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

public class SessionManagerConfigTest
{
    @Test
    public void buildUsingMethodChaining()
        throws Exception
    {
        SessionManagerConfig cfg = new SessionManagerConfig( "name" ).setContext( "context" )
            .setJcaConnectionFactory( "jcaConnectionFactory" ).setName( "newName" ).setProperty( "key1", "value1" )
            .setProperty( "key2", "value2" ).addClass( String.class ).addClass( Integer.class );
        assertEquals( "context", cfg.getContext() );
        assertEquals( "jcaConnectionFactory", cfg.getJcaConnectionFactory() );
        assertEquals( "newName", cfg.getName() );
        assertEquals( 2, cfg.getProperties().size() );
        assertEquals( "value1", cfg.getProperty( "key1" ) );
        assertEquals( "value2", cfg.getProperty( "key2" ) );
        Set<String> annotatedClasses = cfg.getClasses();
        assertEquals( 2, annotatedClasses.size() );
        assertTrue( annotatedClasses.contains( Integer.class.getName() ) );
        assertTrue( annotatedClasses.contains( String.class.getName() ) );
    }
}
