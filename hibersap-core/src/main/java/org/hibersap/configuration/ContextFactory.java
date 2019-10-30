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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.ConfigurationException;
import org.hibersap.HibersapException;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.session.Context;

/**
 * TODO merge Reflection stuff with ReflectionHelper.
 *
 * @author Carsten Erker
 */
public final class ContextFactory {

    private static final Log LOG = LogFactory.getLog(ContextFactory.class);

    private ContextFactory() {
        // should not be instantiated
    }

    public static Context create(final SessionManagerConfig config) {
        // init Context
        final Class<? extends Context> contextClass = getContextClass(config);
        final Context context = getNewInstance(contextClass);
        context.configure(config);

        return context;
    }

    private static Context getNewInstance(final Class<? extends Context> clazz) {
        try {
            return clazz.newInstance();
        } catch (final IllegalAccessException e) {
            throw new ConfigurationException("The class " + clazz
                    + " must be accessible.", e);
        } catch (final InstantiationException e) {
            throw new ConfigurationException("The class " + clazz
                    + " must be accessible and must have a public default constructor.", e);
        } catch (final ConfigurationException e) {
            throw new HibersapException("The class " + clazz + " must hava a public default constructor.", e);
        }
    }

    private static Class<? extends Context> getContextClass(final SessionManagerConfig config) {
        String contextClassName = config.getContext();

        if (StringUtils.isEmpty(contextClassName)) {
            contextClassName = "org.hibersap.execution.jco.JCoContext";
            LOG.info("No context class specified in properties. Default class " + contextClassName + " will be used");
        }
        return getContextClassForName(contextClassName);
    }

    private static Class<? extends Context> getContextClassForName(final String contextClassName) {
        Class<?> contextClass = getClassForName(contextClassName);
        if (Context.class.isAssignableFrom(contextClass)) {
            //noinspection unchecked
            return (Class<? extends Context>) contextClass;
        }
        throw new ConfigurationException(
                "The configured context class (" + contextClass.getName() + ") does not implement " +
                        Context.class.getName()
        );
    }

    public static Class<?> getClassForName(final String contextClassName) {
        try {
            return Class.forName(contextClassName, true, Thread.currentThread().getContextClassLoader());
        } catch (Exception exc) {
            try {
                return Class.forName(contextClassName);
            } catch (final ClassNotFoundException e) {
                throw new ConfigurationException("Class " + contextClassName + " not found.", e);
            } catch (final Exception e) {
                throw new ConfigurationException("Class " + contextClassName + " could not be loaded", e);
            }
        }
    }
}
