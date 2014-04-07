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

public class HibersapJaxbXmlParser
{
    private final JAXBContext jaxbContext;

    public HibersapJaxbXmlParser()
    {
        try
        {
            jaxbContext = JAXBContext.newInstance( HibersapConfig.class );
        }
        catch ( final JAXBException e )
        {
            throw new ConfigurationException( "Cannot not create a JAXB context.", e );
        }
    }

    public HibersapConfig parseResource( final String resourceName )
            throws HibersapParseException
    {
        final InputStream resourceStream = findResource( resourceName );
        return parseResource( resourceStream, resourceName );
    }

    public HibersapConfig parseResource( final InputStream resourceStream, final String resourceName )
            throws HibersapParseException
    {
        Object unmarshalledObject;
        try
        {
            SAXSource saxSource = createSaxSource( resourceStream );
            unmarshalledObject = createUnmarshaller().unmarshal( saxSource );
        }
        catch ( final JAXBException e )
        {
            throw new HibersapParseException( "Cannot parse the resource " + resourceName, e );
        }

        if ( unmarshalledObject == null )
        {
            throw new HibersapParseException( "Resource " + resourceName + " is empty." );
        }
        if ( !( unmarshalledObject instanceof HibersapConfig ) )
        {
            throw new HibersapParseException( "Resource " + resourceName
                    + " does not consist of a hibersap specification. I found a "
                    + unmarshalledObject.getClass().getSimpleName() );
        }
        return ( HibersapConfig ) unmarshalledObject;
    }

    private SAXSource createSaxSource( InputStream resourceStream )
    {
        SAXSource source;
        try
        {
            XMLReader reader = XMLReaderFactory.createXMLReader();

            // NamespaceFilter to remove namespaces for each element
            NamespaceFilter inFilter = new NamespaceFilter( "", false );
            inFilter.setParent( reader );

            InputSource is = new InputSource( resourceStream );
            source = new SAXSource( inFilter, is );
        }
        catch ( SAXException e )
        {
            throw new InternalHiberSapException( "Cannot create NamespaceFilter. ", e );
        }
        return source;
    }

    private Unmarshaller createUnmarshaller()
    {
        Unmarshaller unmarshaller;
        try
        {
            unmarshaller = jaxbContext.createUnmarshaller();
        }
        catch ( final JAXBException e )
        {
            throw new InternalHiberSapException( "Cannot create an unmarshaller. ", e );
        }
        return unmarshaller;
    }

    private InputStream findResource( final String resourceName )
    {
        URL resource = Thread.currentThread().getContextClassLoader().getResource( resourceName );
        if ( resource == null )
        {
            resource = getClass().getResource( resourceName );
        }
        if ( resource == null )
        {
            throw new InternalHiberSapException( "Cannot locate resource " + resourceName );
        }
        final InputStream resourceStream;
        try
        {
            resourceStream = resource.openStream();
        }
        catch ( final IOException e )
        {
            throw new InternalHiberSapException( "Cannot open resource " + resourceName, e );
        }
        return resourceStream;
    }
}
