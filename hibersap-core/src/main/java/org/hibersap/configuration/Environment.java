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

package org.hibersap.configuration;

import org.hibersap.HibersapException;

import java.io.IOException;
import java.util.Properties;

/*
* @author Carsten Erker
*/
public final class Environment
{
    public static final String HIBERSAP_XML_FILE = "/META-INF/hibersap.xml";

    private static final String HIBERSAP_VERSION_FILE = "hibersap-version.properties";

    private static final String HIBERSAP_VERSION_PROPERTY_KEY = "hibersap-version";

    public static final String VERSION = readHibersapVersion();

    private static String readHibersapVersion()
    {
        String version;
        try
        {
            final Properties properties = new Properties();
            properties.load( Environment.class.getResourceAsStream( "/" + HIBERSAP_VERSION_FILE ) );
            version = properties.getProperty( HIBERSAP_VERSION_PROPERTY_KEY );
        }
        catch ( IOException e )
        {
            throw new HibersapException( "Can not load file " + HIBERSAP_VERSION_FILE
                    + ". This file is part of the hibersap-core library and should always be there." );
        }
        return version;
    }

    private Environment()
    {
        // should not be instantiated
    }
}
