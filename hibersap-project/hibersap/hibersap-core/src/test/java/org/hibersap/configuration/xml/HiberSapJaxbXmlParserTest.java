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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.Test;

public class HiberSapJaxbXmlParserTest
{
    @Test
    public void testOK()
        throws Exception
    {
        final HibersapJaxbXmlParser hiberSapJaxbXmlParser = new HibersapJaxbXmlParser();
        HibersapConfig config = hiberSapJaxbXmlParser.parseResource( "/xml-configurations/hibersapOK.xml" );

        assertNotNull( config );

        List<SessionManagerConfig> sessionManagers = config.getSessionManagers();
        assertEquals( 2, sessionManagers.size() );

        SessionManagerConfig sf1 = sessionManagers.get( 0 );
        assertEquals( "A12", sf1.getName() );
        assertEquals( "org.hibersap.execution.jco.JCoContext", sf1.getContext() );
        assertEquals( "org.hibersap.execution.jca.cci.MyTestConnectionSpecFactory", sf1.getJcaConnectionSpecFactory() );
        assertEquals( "java:/eis/sap/A12", sf1.getJcaConnectionFactory() );

        Set<Property> properties = sf1.getProperties();
        assertEquals( 7, properties.size() );

        Set<String> classes = sf1.getClasses();
        assertEquals( 2, classes.size() );

        assertEquals( 0, sf1.getInterceptorClasses().size() );

        SessionManagerConfig sf2 = sessionManagers.get( 1 );
        assertEquals( "B34", sf2.getName() );
        assertEquals( "org.hibersap.execution.jco.JCAContext", sf2.getContext() );
        assertEquals( "java:/eis/sap/B34", sf2.getJcaConnectionFactory() );
        assertEquals( "org.hibersap.execution.jca.cci.SapBapiJcaAdapterConnectionSpecFactory", sf2
            .getJcaConnectionSpecFactory() );

        properties = sf2.getProperties();
        assertEquals( 7, properties.size() );

        classes = sf2.getClasses();
        assertEquals( 2, classes.size() );

        Set<String> interceptors = sf2.getInterceptorClasses();
        assertEquals( 2, interceptors.size() );
        assertTrue( interceptors.contains( "org.test.Class4" ) );
        assertTrue( interceptors.contains( "org.test.Class5" ) );
    }
}
