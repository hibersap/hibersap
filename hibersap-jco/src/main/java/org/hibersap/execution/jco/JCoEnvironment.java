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

package org.hibersap.execution.jco;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.ext.Environment;
import java.util.Properties;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.HibersapException;

/**
 * This class acts as a wrapper for the ugly static JCo classes.
 *
 * @author Carsten Erker
 */
public final class JCoEnvironment {

    private static final Log LOG = LogFactory.getLog(JCoEnvironment.class);

    /**
     * JCo's Environment class doesn't offer any methods to check if a provider class is already
     * registered, but we need to dynamically register destinations
     */
    private static final JCoDataProvider destinationDataProvider = new JCoDataProvider();

    static {
        LOG.info("Using SAP JCo - java.library.path=" + SystemUtils.JAVA_LIBRARY_PATH);
    }

    private JCoEnvironment() {
        // should not be instantiated
    }

    public static void registerDestination(final String destinationName, final Properties jcoProperties) {
        LOG.info("Registering destination " + destinationName);

        if (!destinationDataProvider.hasDestinations()) {
            registerDestinationDataProvider();
        }

        destinationDataProvider.addDestination(destinationName, jcoProperties);
    }

    public static void unregisterDestination(final String destinationName) {
        LOG.info("Unregistering destination " + destinationName);

        destinationDataProvider.removeDestination(destinationName);

        if (!destinationDataProvider.hasDestinations()) {
            unregisterDestinationDataProvider();
        }
    }

    public static JCoDestination getDestination(final String destinationName) {
        try {
            return JCoDestinationManager.getDestination(destinationName);
        } catch (JCoException e) {
            throw new HibersapException("Destination named '" + destinationName + "' is not registered with JCo", e);
        }
    }

    private static void registerDestinationDataProvider() {
        LOG.info("Registering DestinationDataProvider with JCo");

        Environment.registerDestinationDataProvider(destinationDataProvider);
    }

    private static void unregisterDestinationDataProvider() {
        LOG.info("Unregistering DestinationDataProvider from JCo");

        Environment.unregisterDestinationDataProvider(destinationDataProvider);
    }
}
