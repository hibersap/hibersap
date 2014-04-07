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

import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConfigurationMarshallTest
{
    private JAXBContext jaxbContext;

    @Before
    public void setup()
            throws JAXBException
    {
        jaxbContext = JAXBContext.newInstance( HibersapConfig.class, SessionManagerConfig.class, Property.class );
    }


    @Test
    public void testParseOkConfiguration()
            throws Exception
    {
        final InputStream configurationAsStream = getClass()
                .getResourceAsStream( "/xml-configurations/hibersapOK.xml" );
        assertNotNull( configurationAsStream );

        final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        final Object unmarshalledObject = unmarshaller.unmarshal( configurationAsStream );
        final HibersapConfig hiberSapMetaData = ( HibersapConfig ) unmarshalledObject;

        final List<SessionManagerConfig> sessionManagers = hiberSapMetaData.getSessionManagers();
        assertNotNull( sessionManagers );
        assertEquals( 2, sessionManagers.size() );

        assertEquals( "A12", sessionManagers.get( 0 ).getName() );
        assertEquals( "B34", sessionManagers.get( 1 ).getName() );
    }

    // TODO create complete xml and verify against xsd
    @Test
    public void testMarshalling()
            throws Exception
    {
        final List<Property> properties = new ArrayList<Property>();
        final Property jcoProperty = new Property( "name", "value" );
        properties.add( jcoProperty );
        final SessionManagerConfig sessionManagerMetaData = new SessionManagerConfig( "session-name" )
                .setContext( "ContextClass" ).setProperties( properties );

        final List<String> classes = new ArrayList<String>();
        classes.add( "package.Class1" );
        classes.add( "package.Class2" );
        sessionManagerMetaData.setAnnotatedClasses( classes );

        final HibersapConfig hiberSapMetaData = new HibersapConfig( sessionManagerMetaData );

        final Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty( "jaxb.formatted.output", Boolean.TRUE );

        final StringWriter stringWriter = new StringWriter();
        marshaller.marshal( hiberSapMetaData, stringWriter );
        System.out.println( stringWriter.toString() );
    }
}