/*
 * Copyright (c) 2008-2025 tech@spree GmbH
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

package org.hibersap.configuration.xml;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConfigurationMarshallTest {

    private JAXBContext jaxbContext;

    @Before
    public void setup()
            throws JAXBException {
        jaxbContext = JAXBContext.newInstance(HibersapConfig.class, SessionManagerConfig.class, Property.class);
    }

    @Test
    public void testParseOkConfiguration()
            throws Exception {
        final InputStream configurationAsStream = getClass()
                .getResourceAsStream("/xml-configurations/hibersapOK.xml");
        assertNotNull(configurationAsStream);

        final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        final Object unmarshalledObject = unmarshaller.unmarshal(configurationAsStream);
        final HibersapConfig hiberSapMetaData = (HibersapConfig) unmarshalledObject;

        final List<SessionManagerConfig> sessionManagers = hiberSapMetaData.getSessionManagers();
        assertNotNull(sessionManagers);
        assertEquals(2, sessionManagers.size());

        assertEquals("A12", sessionManagers.get(0).getName());
        assertEquals("B34", sessionManagers.get(1).getName());
    }

    // TODO create complete xml and verify against xsd
    @Test
    public void testMarshalling()
            throws Exception {
        final List<Property> properties = new ArrayList<>();
        final Property jcoProperty = new Property("name", "value");
        properties.add(jcoProperty);
        final SessionManagerConfig sessionManagerMetaData = new SessionManagerConfig("session-name")
                .setContext("ContextClass").setProperties(properties);

        final List<String> classes = new ArrayList<>();
        classes.add("package.Class1");
        classes.add("package.Class2");
        sessionManagerMetaData.setAnnotatedClasses(classes);

        final HibersapConfig hiberSapMetaData = new HibersapConfig(sessionManagerMetaData);

        final Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty("jaxb.formatted.output", Boolean.TRUE);

        final StringWriter stringWriter = new StringWriter();
        marshaller.marshal(hiberSapMetaData, stringWriter);
        System.out.println(stringWriter.toString());
    }
}
