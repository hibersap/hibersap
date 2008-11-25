package org.hibersap.configuration.xml;

/*
 * Copyright (C) 2008 akquinet tech@spree GmbH
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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.configuration.Environment;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Parse hibersap.xml and build properties with its contents.
 * 
 *  @author dahm
 */
public class HibersapXMLParser
{
    private static Log LOG = LogFactory.getLog( HibersapXMLParser.class );

    private static final String INVALID_CONTENT = "????";

    public static final String PROPERTY_NAME_ATTRIBUTE = "name";

    public static final String PROPERTY_VALUE_ATTRIBUTE = "value";

    public static final String CONTEXT_TAG = "context";

    public static final String PROPERTIES_TAG = "properties";

    public static final String PROPERTY_TAG = "property";

    public static final String SESSION_FACTORY_TAG = "session-factory";

    public static final String SESSION_FACTORY_NAME_ATTRIBUTE = PROPERTY_NAME_ATTRIBUTE;

    public static final String JTA_CONNECTION_FACTORY_TAG = "jta-connection-factory";

    private final String hibersapXmlFile;

    private final Properties properties = new Properties();

    private final InputStream stream;

    public HibersapXMLParser( final String hibersapXmlFile, final InputStream stream )
    {
        this.hibersapXmlFile = hibersapXmlFile;
        this.stream = stream;
    }

    public Properties parseXML()
    {
        try
        {
            final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            final Document doc = builder.parse( stream );
            parseXML( doc );
        }
        catch ( final ParserConfigurationException e )
        {
            LOG.error( "problem reading XML file " + hibersapXmlFile, e );
            properties.clear();
        }
        catch ( final SAXException e )
        {
            LOG.error( "parsing problem in XML file " + hibersapXmlFile, e );
            properties.clear();
        }
        catch ( final IOException e )
        {
            LOG.error( "IO problem in XML file " + hibersapXmlFile, e );
            properties.clear();
        }
        catch ( final HibersapParseException e )
        {
            LOG.error( "invalid data found in " + hibersapXmlFile, e );
            properties.clear();
        }

        return properties;
    }

    private void parseXML( final Document doc )
        throws HibersapParseException
    {
        final NodeList nodes = doc.getElementsByTagName( SESSION_FACTORY_TAG );
        final int length = checkSessionFactoryNodesCount( nodes );

        for ( int i = 0; i < length; i++ )
        {
            final Element sessionFactoryElement = (Element) nodes.item( i );
            parseSessionFactoryName( sessionFactoryElement );

            final NodeList contextNodes = sessionFactoryElement.getElementsByTagName( CONTEXT_TAG );
            parseContextNodes( contextNodes );

            final NodeList connectionFactory = sessionFactoryElement.getElementsByTagName( JTA_CONNECTION_FACTORY_TAG );
            parseConnectionFactoryNodesCount( connectionFactory );

            final NodeList propertiesNodes = sessionFactoryElement.getElementsByTagName( PROPERTIES_TAG );
            parsePropertiesNodes( propertiesNodes );
        }
    }

    private void parseContextNodes( final NodeList contextNodes )
        throws HibersapParseException
    {
        final int length = contextNodes.getLength();

        switch ( length )
        {
            case 0:
                throw new HibersapParseException( "no <" + CONTEXT_TAG + "> tag found" );
            case 1:
                break;
            default:
                throw new HibersapParseException( "only one <" + CONTEXT_TAG + "> tag is allowed" );
        }

        final Element contextElement = (Element) contextNodes.item( 0 );
        final String content = getCharacterDataFromElement( contextElement );

        if ( StringUtils.isBlank( content ) || INVALID_CONTENT.equals( content ) )
        {
            throw new HibersapParseException( "content of <" + CONTEXT_TAG + "> tag is invalid" );
        }
        else
        {
            properties.put( Environment.CONTEXT_CLASS, content.trim() );
        }
    }

    private void parsePropertiesNodes( final NodeList propertiesNodes )
        throws HibersapParseException
    {
        final int length = propertiesNodes.getLength();

        if ( length > 1 )
        {
            throw new HibersapParseException( "only one <" + PROPERTIES_TAG + "> tag is allowed" );
        }
        else if ( length == 1 )
        {
            final Element propertiesElement = (Element) propertiesNodes.item( 0 );
            final NodeList propertyNodes = propertiesElement.getElementsByTagName( PROPERTY_TAG );

            for ( int i = 0; i < propertyNodes.getLength(); i++ )
            {
                final Element propertyElement = (Element) propertyNodes.item( i );
                final String name = checkForAttribute( propertyElement, PROPERTY_NAME_ATTRIBUTE, PROPERTY_TAG );
                final String value = checkForAttribute( propertyElement, PROPERTY_VALUE_ATTRIBUTE, PROPERTY_TAG );
                properties.put( name, value );
            }
        }
    }

    private int checkSessionFactoryNodesCount( final NodeList nodes )
        throws HibersapParseException
    {
        final int length = nodes.getLength();

        switch ( length )
        {
            case 0:
                throw new HibersapParseException( "no <" + SESSION_FACTORY_TAG + "> tag found" );
            case 1:
                break;
            default:
                throw new HibersapParseException( "only one <" + SESSION_FACTORY_TAG + "> tag is currently supported" );
        }

        return length;
    }

    private void parseConnectionFactoryNodesCount( final NodeList nodes )
        throws HibersapParseException
    {
        final int length = nodes.getLength();

        if ( length > 0 )
        {
            throw new HibersapParseException( "<" + JTA_CONNECTION_FACTORY_TAG + "> tag is currently not supported" );
        }
    }

    private void parseSessionFactoryName( final Element element )
        throws HibersapParseException
    {
        final String sessionFactoryName = checkForAttribute( element, SESSION_FACTORY_NAME_ATTRIBUTE,
                                                             SESSION_FACTORY_TAG );
        properties.put( Environment.SESSION_FACTORY_NAME, sessionFactoryName );
    }

    private static String getCharacterDataFromElement( final Element e )
    {
        final Node child = e.getFirstChild();

        if ( child instanceof CharacterData )
        {
            final CharacterData cd = (CharacterData) child;
            return cd.getData();
        }

        return INVALID_CONTENT;
    }

    private static String checkForAttribute( final Element propertyElement, final String attributeName,
                                             final String tagName )
        throws HibersapParseException
    {
        final String name = propertyElement.getAttribute( attributeName );

        if ( StringUtils.isBlank( name ) )
        {
            throw new HibersapParseException( "<" + tagName + "> tag has no valid '" + attributeName + "' attribute" );
        }
        else
        {
            return name.trim();
        }
    }
}
