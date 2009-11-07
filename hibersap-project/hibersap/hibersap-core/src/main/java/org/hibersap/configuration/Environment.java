package org.hibersap.configuration;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.configuration.xml.HibersapConfig;
import org.hibersap.configuration.xml.HibersapJaxbXmlParser;

/*
 * @author Carsten Erker
 */
public final class Environment
{
    /*
     * The Hibersap Version.
     */
    public static final String VERSION = "1.0";

    /*
     * Where to find the hibersap.xml configuration file in the classpath.
     */
    public static final String HIBERSAP_XML_FILE = "/META-INF/hibersap.xml";

    private static final Log LOG = LogFactory.getLog( Environment.class );

    private static final HibersapConfig CONFIG = readConfig();

    private Environment()
    {
        // should not be instantiated
    }

    private static HibersapConfig readConfig()
    {
        LOG.info( "Hibersap " + VERSION );
        return readXMLProperties( HIBERSAP_XML_FILE );
    }

    static HibersapConfig readXMLProperties( final String hibersapXmlFile )
    {
        LOG.debug( "Trying to read properties from " + hibersapXmlFile );
        HibersapConfig hibersapConfig;
        try
        {
            final HibersapJaxbXmlParser hibersapXMLParser = new HibersapJaxbXmlParser();
            hibersapConfig = hibersapXMLParser.parseResource( hibersapXmlFile );
            LOG.debug( "Properties read from " + hibersapXmlFile + ": " + hibersapConfig );
            return hibersapConfig;
        }
        catch ( final Exception e )
        {
            LOG.error( "Problems with reading/parsing " + hibersapXmlFile, e );
            return null;
        }
    }

    public static HibersapConfig getHibersapConfig()
    {
        return CONFIG;
    }
}
