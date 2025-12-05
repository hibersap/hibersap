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

package org.hibersap.configuration;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.ConfigurationException;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.mapping.AnnotationBapiMapper;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.session.SessionManager;

/**
 * Configures Hibersap using annotated BAPI classes.
 * <p/>
 * There are two possibilities to add annotated classes:
 * <ol>
 * <li>In hibersap.xml:
 * &lt;annotated-class&gt;org.hibersap.examples.flightlist.FlightListBapi&lt;/anotated-class&gt;</li>
 * <li>programmatically via addAnnotatedClass().</li>
 * </ol>
 * <p/>
 * After calling buildSessionManager() this instance can be discarded. The SessionManager will be
 * used to interact with the back-end system. Properties may be overwritten using the methods in
 * this class' superclass, e.g. to specify different SAP systems in a test environment. For each SAP
 * system which will be accessed by the client application, one SessionManager has to be built.
 *
 * @author Carsten Erker
 */
public class AnnotationConfiguration extends Configuration {

    private static final Log LOG = LogFactory.getLog(AnnotationConfiguration.class);

    private AnnotationBapiMapper bapiMapper = new AnnotationBapiMapper();

    public AnnotationConfiguration() {
        super();
    }

    public AnnotationConfiguration(final SessionManagerConfig config) {
        super(config);
    }

    public AnnotationConfiguration(final String name) {
        super(name);
    }

    /**
     * Programmatic configuration of bapi classes. This works in any class loader
     *
     * @param bapiClasses The @Bapi annotated classes
     */
    public void addBapiClasses(final Class<?>... bapiClasses) {
        final Map<String, BapiMapping> bapiMappings = new HashMap<>();

        for (Class<?> bapiClass : bapiClasses) {

            final BapiMapping bapiMapping = bapiMapper.mapBapi(bapiClass);

            bapiMappings.put(bapiClass.getName(), bapiMapping);
        }

        addBapiMappings(bapiMappings);
    }

    /**
     * Builds a SessionManager object.
     *
     * @return The SessionManager
     */
    @Override
    public SessionManager buildSessionManager() {
        addBapiMappingsFromConfig();
        return super.buildSessionManager();
    }

    /**
     * Add bapi mappings from configured class names in config file; checks if classes exist in the classpath.
     */
    private void addBapiMappingsFromConfig() {

        final Map<String, BapiMapping> bapiMappings = new HashMap<>();

        for (final String className : getSessionManagerConfig().getAnnotatedClasses()) {
            try {
                LOG.info("Mapping BAPI class " + className);
                Class<?> clazz = Class.forName(className);
                final BapiMapping bapiMapping = bapiMapper.mapBapi(clazz);
                bapiMappings.put(clazz.getName(), bapiMapping);
            } catch (ClassNotFoundException e) {
                String message = "Cannot find class " + className + " in classpath";
                LOG.error(message);
                throw new ConfigurationException(message, e);
            }
        }
        addBapiMappings(bapiMappings);
    }

    public String getSessionManagerName() {
        return getSessionManagerConfig().getName();
    }
}
