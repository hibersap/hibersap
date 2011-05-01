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

/*
 * @author Carsten Erker
 */
public final class Environment
{
    /*
     * The Hibersap Version.
     */
    public static final String VERSION = "1.1";

    /*
     * Where to find the hibersap.xml configuration file in the classpath.
     */
    public static final String HIBERSAP_XML_FILE = "/META-INF/hibersap.xml";

    private Environment()
    {
        // should not be instantiated
    }
}
