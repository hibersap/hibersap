package org.hibersap.configuration.xml;

import org.junit.Test;

public class HibersapConfigTest
{
    @Test
    public void create()
        throws Exception
    {
        HibersapConfig config = new HibersapConfig();

        config.addSessionFactory( "Sf1" ).setContext( "org.hibersap.execution.jco.JCoContext" )
            .setJcaConnectionFactory( "java:/eis/sap/A12" ).setProperty( "", "" ).setProperty( "", "" )
            .addClass( Integer.class ).addClass( String.class );

        config.addSessionFactory( "Sf2" ).setContext( "org.hibersap.execution.jco.JCAContext" )
            .setJcaConnectionFactory( "java:/eis/sap/B34" ).setProperty( "", "" ).setProperty( "", "" )
            .addClass( String.class ).addClass( Integer.class );
    }
}
