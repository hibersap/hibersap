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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

public class NamespaceFilter extends XMLFilterImpl
{
    private static final Log LOG = LogFactory.getLog( NamespaceFilter.class );

    private String usedNamespaceUri;
    private boolean addNamespace;

    //State variable
    private boolean addedNamespace = false;

    public NamespaceFilter( String namespaceUri,
                            boolean addNamespace )
    {
        super();

        if ( addNamespace )
        {
            this.usedNamespaceUri = namespaceUri;
        }
        else
        {
            this.usedNamespaceUri = "";
        }
        this.addNamespace = addNamespace;
    }


    @Override
    public void startDocument() throws SAXException
    {
        super.startDocument();

        if ( addNamespace )
        {
            startControlledPrefixMapping();
        }
    }


    @Override
    public void startElement( String arg0, String arg1, String arg2,
                              Attributes arg3 ) throws SAXException
    {
        super.startElement( this.usedNamespaceUri, arg1, arg2, arg3 );
    }

    @Override
    public void endElement( String arg0, String arg1, String arg2 )
            throws SAXException
    {
        super.endElement( this.usedNamespaceUri, arg1, arg2 );
    }

    @Override
    public void startPrefixMapping( String prefix, String url )
            throws SAXException
    {
        if ( addNamespace )
        {
            this.startControlledPrefixMapping();
        }
        else
        {
            //Remove the namespace, i.e. don´t call startPrefixMapping for parent!
        }
    }

    private void startControlledPrefixMapping() throws SAXException
    {
        if ( this.addNamespace && !this.addedNamespace )
        {
            //We should add namespace since it is set and has not yet been done.
            super.startPrefixMapping( "", this.usedNamespaceUri );

            //Make sure we dont do it twice
            this.addedNamespace = true;
        }
    }
}
