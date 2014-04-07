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

import org.hibersap.interceptor.impl.SapErrorInterceptor;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HibersapConfigTest
{
    @Test
    public void testCreate()
            throws Exception
    {
        HibersapConfig config = new HibersapConfig();

        config.addSessionManager( "Sm1" ).setContext( "org.hibersap.execution.jco.JCoContext" )
                .setJcaConnectionFactory( "java:/eis/sap/A12" ).setProperty( "key1", "value1" ).setProperty( "key2",
                "value2" )
                .addAnnotatedClass( Integer.class ).addAnnotatedClass( String.class );

        config.addSessionManager( "Sm2" ).setContext( "org.hibersap.execution.jca.JCAContext" )
                .setJcaConnectionFactory( "java:/eis/sap/B34" ).setProperty( "key3", "value3" ).setProperty( "key4",
                "value4" )
                .addAnnotatedClass( String.class ).addAnnotatedClass( Integer.class )
                .addExecutionInterceptorClass( SapErrorInterceptor.class );

        assertProperties( config, "Sm1", "org.hibersap.execution.jco.JCoContext", "java:/eis/sap/A12", "key1",
                "value1", "key2", "value2" );
        assertProperties( config, "Sm2", "org.hibersap.execution.jca.JCAContext", "java:/eis/sap/B34", "key3",
                "value3", "key4", "value4" );

        List<String> interceptors = config.getSessionManager( "Sm2" ).getExecutionInterceptorClasses();
        assertEquals( 1, interceptors.size() );
        assertEquals( SapErrorInterceptor.class.getName(), interceptors.iterator().next() );
    }

    private void assertProperties( HibersapConfig config, String smName, String smContext, String smConnFact,
                                   String smPropKey1, String smPropVal1, String smPropKey2, String smPropVal2 )
    {
        SessionManagerConfig sessionManager = config.getSessionManager( smName );
        assertEquals( smName, sessionManager.getName() );
        assertEquals( smContext, sessionManager.getContext() );
        assertEquals( smConnFact, sessionManager.getJcaConnectionFactory() );
        assertEquals( 2, sessionManager.getProperties().size() );
        assertEquals( smPropVal1, sessionManager.getProperty( smPropKey1 ) );
        assertEquals( smPropVal2, sessionManager.getProperty( smPropKey2 ) );
        List<String> annotatedClasses = sessionManager.getAnnotatedClasses();
        assertEquals( 2, annotatedClasses.size() );
        assertTrue( "SessionManager " + smName, annotatedClasses.contains( String.class.getName() ) );
        assertTrue( annotatedClasses.contains( Integer.class.getName() ) );
    }
}
