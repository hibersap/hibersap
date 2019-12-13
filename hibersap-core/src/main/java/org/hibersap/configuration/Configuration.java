/*
 * Copyright (c) 2008-2019 akquinet tech@spree GmbH
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

package org.hibersap.configuration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.ConfigurationException;
import org.hibersap.configuration.xml.HibersapConfig;
import org.hibersap.configuration.xml.HibersapJaxbXmlParser;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.interceptor.BapiInterceptor;
import org.hibersap.interceptor.ExecutionInterceptor;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.session.Context;
import org.hibersap.session.SessionManager;
import org.hibersap.session.SessionManagerImpl;

/**
 * Abstract Superclass for different configuration strategies.
 * Implements properties / settings handling.
 * The concrete class' responsibility is to provide the BapiMappings,
 * e.g. by analyzing annotated classes or XML mappings.
 *
 * @author Carsten Erker
 */
public abstract class Configuration {

    private static final Log LOG = LogFactory.getLog(Configuration.class);

    static {
        LOG.info("Hibersap Version " + Environment.VERSION);
    }

    private final ConfigurationData data;

    /**
     * Creates a Configuration for programmatic configuration of Hibersap. You have to create a
     * org.hibersap.configuration.xml.SessionManagerConfig first.
     *
     * @param sessionManagerConfig The session manager configuration
     */
    public Configuration(final SessionManagerConfig sessionManagerConfig) {
        this.data = new ConfigurationData(sessionManagerConfig);
    }

    /**
     * Creates a Configuration for a concrete SessionManager. The SessionManager must be configured
     * in the hibersap.xml file.
     *
     * @param sessionManagerName The SessionManager name as specified in the hibersap.xml file
     */
    public Configuration(final String sessionManagerName) {
        SessionManagerConfig sessionManagerConfig = readHibersapConfig().getSessionManager(sessionManagerName);
        this.data = new ConfigurationData(sessionManagerConfig);
    }

    /**
     * Creates a Configuration for the first SessionManager specified in the hibersap.xml file. This
     * method should only be used when there is exactly one SessionManager specified in
     * hibersap.xml.
     */
    public Configuration() {
        List<SessionManagerConfig> sessionManagers = readHibersapConfig().getSessionManagers();

        if (sessionManagers.size() > 0) {
            this.data = new ConfigurationData(sessionManagers.get(0));

            LOG.warn("Only the first session manager (" + data.getSessionManagerConfig().getName()
                    + ") specified in hibersap.xml was configured. To configure the other specified session managers "
                    + "you must explicitly call the constructor of the org.hibersap.configuration.Configuration "
                    + "implementation with the sessionManagerName argument.");
        } else {
            throw new ConfigurationException("Can not find a configured SessionManager");
        }
    }

    public void addExecutionInterceptors(final ExecutionInterceptor... executionInterceptors) {
        data.addExecutionInterceptors(new HashSet<>(Arrays.asList(executionInterceptors)));
    }

    public void addBapiInterceptors(final BapiInterceptor... bapiInterceptors) {
        data.addBapiInterceptors(new HashSet<>(Arrays.asList(bapiInterceptors)));
    }

    /**
     * Builds the session manager for this Configuration.
     *
     * @param context The Hibersap Context
     * @return The session manager
     */
    public SessionManager buildSessionManager(final Context context) {
        final SessionManagerConfig config = data.getSessionManagerConfig();

        LOG.info("Building SessionManager '" + config.getName() + "'");

        data.addExecutionInterceptors(ConfigurationHelper.createExecutionInterceptors(config));
        data.addBapiInterceptors(ConfigurationHelper.createBapiInterceptors(config));
        return new SessionManagerImpl(data, context);
    }

    /**
     * Builds the session manager for this Configuration.
     *
     * @return The session manager
     */
    public SessionManager buildSessionManager() {
        return buildSessionManager(ConfigurationHelper.createContext(data.getSessionManagerConfig()));
    }

    /**
     * Concrete subclasses should use this method to provide the BAPI mapping information.
     *
     * @param bapiMappings A Map with the BAPI class as key and the BapiMappings as value.
     */
    protected void addBapiMappings(final Map<String, BapiMapping> bapiMappings) {
        data.addBapiMappingsForClass(bapiMappings);
    }

    protected SessionManagerConfig getSessionManagerConfig() {
        return data.getSessionManagerConfig();
    }

    /**
     * Load hibersap.xml file from default location (see Environment.HIBERSAP_XML_FILE)
     * or from -D Parameter (see Environment.HIBERSAP_XML_FILE)
     *
     * @return The hibersap config
     */
    private HibersapConfig readHibersapConfig() {

        String configFile;
        try {
            // Get path to xml from system property or as fallback use default location
            // see: https://github.com/hibersap/hibersap/issues/9
            configFile = System.getProperty(Environment.HIBERSAP_XML_FILE_PARAMETER, Environment.HIBERSAP_XML_FILE);
        } catch (NullPointerException | IllegalArgumentException e) {
            configFile = Environment.HIBERSAP_XML_FILE;
        }
        LOG.debug("Reading HibersapConfig from configuration file: '" + configFile + "'");

        final HibersapJaxbXmlParser parser = new HibersapJaxbXmlParser();
        return parser.parseResource(configFile);
    }
}
