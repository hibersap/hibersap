/*
 * Copyright (c) 2008-2014 akquinet tech@spree GmbH
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

import org.hibersap.ConfigurationException;
import org.hibersap.InternalHiberSapException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.sax.SAXSource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class HibersapJaxbXmlParser {

    private final JAXBContext jaxbContext;

    public HibersapJaxbXmlParser() {
        try {
            jaxbContext = JAXBContext.newInstance( HibersapConfig.class );
        } catch ( final JAXBException e ) {
            throw new ConfigurationException( "Cannot not create a JAXB context.", e );
        }
    }

    public HibersapConfig parseResource( final String resourceName ) throws HibersapParseException {
        final InputStream resourceStream = findResource( resourceName );
        return parseResource( resourceStream, resourceName );
    }

    public HibersapConfig parseResource( final ClassLoader classLoader, final String resourceName ) throws HibersapParseException {
        final InputStream resourceStream = classLoader.getResourceAsStream( resourceName );

        if ( resourceStream == null ) {
            throw new HibersapParseException( "Resource " + resourceName + " not found in ClassLoader " + classLoader.toString() );
        }

        return parseResource( resourceStream, resourceName );
    }

    public HibersapConfig parseResource( final InputStream resourceStream, final String resourceName )
            throws HibersapParseException {
        Object unmarshalledObject;
        try {
            SAXSource saxSource = createSaxSource( resourceStream );
            unmarshalledObject = createUnmarshaller().unmarshal( saxSource );
        } catch ( final JAXBException e ) {
            throw new HibersapParseException( "Cannot parse the resource " + resourceName, e );
        }

        if ( unmarshalledObject == null ) {
            throw new HibersapParseException( "Resource " + resourceName + " is empty." );
        }
        if ( !( unmarshalledObject instanceof HibersapConfig ) ) {
            throw new HibersapParseException( "Resource " + resourceName
                                                      + " does not consist of a hibersap specification. I found a "
                                                      + unmarshalledObject.getClass().getSimpleName() );
        }
        return (HibersapConfig) unmarshalledObject;
    }

    private SAXSource createSaxSource( final InputStream resourceStream ) {
        SAXSource source;
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();

            // NamespaceFilter to remove namespaces for each element
            NamespaceFilter inFilter = new NamespaceFilter( "", false );
            inFilter.setParent( reader );

            InputSource is = new InputSource( resourceStream );
            source = new SAXSource( inFilter, is );
        } catch ( SAXException e ) {
            throw new InternalHiberSapException( "Cannot create NamespaceFilter. ", e );
        }
        return source;
    }

    private Unmarshaller createUnmarshaller() {
        Unmarshaller unmarshaller;
        try {
            unmarshaller = jaxbContext.createUnmarshaller();
        } catch ( final JAXBException e ) {
            throw new InternalHiberSapException( "Cannot create an unmarshaller. ", e );
        }
        return unmarshaller;
    }

    private InputStream findResource( final String resourceName ) {
        URL resource = Thread.currentThread().getContextClassLoader().getResource( resourceName );
        if ( resource == null ) {
            resource = getClass().getResource( resourceName );
        }
        if ( resource == null ) {
            throw new InternalHiberSapException( "Cannot locate resource " + resourceName );
        }
        final InputStream resourceStream;
        try {
            resourceStream = resource.openStream();
        } catch ( final IOException e ) {
            throw new InternalHiberSapException( "Cannot open resource " + resourceName, e );
        }
        return resourceStream;
    }
}
