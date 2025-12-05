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

package org.hibersap.session;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.hibersap.HibersapException;
import org.hibersap.configuration.ConfigurationData;
import org.hibersap.configuration.ConfigurationHelper;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.conversion.ConverterCache;
import org.hibersap.interceptor.BapiInterceptor;
import org.hibersap.interceptor.ExecutionInterceptor;
import org.hibersap.mapping.model.BapiMapping;

/**
 * Implementation of the SessionManager. A client uses the SessionManager to create Hibersap
 * Sessions.
 *
 * @author Carsten Erker
 */
public final class SessionManagerImpl implements SessionManager, SessionManagerImplementor {

    private static final long serialVersionUID = -541810809624063050L;
    private final SessionManagerConfig config;
    private boolean closed;
    private Map<String, BapiMapping> bapiMappings;

    private transient Context context;

    private transient ConverterCache converterCache;

    private transient Set<ExecutionInterceptor> executionInterceptors;

    private transient Set<BapiInterceptor> bapiInterceptors = new HashSet<>();

    public SessionManagerImpl(final ConfigurationData data, final Context context) {
        closed = false;
        config = data.getSessionManagerConfig();
        bapiMappings = new HashMap<>(data.getBapiMappingsForClass());
        initializeTransientFields(data, context);
    }

    private void initializeTransientFields(final ConfigurationData data, final Context context) {
        this.context = context;
        converterCache = new ConverterCache();
        executionInterceptors = new HashSet<>(data.getExecutionInterceptors());
        bapiInterceptors = new HashSet<>(data.getBapiInterceptors());
    }

    /*
     * {@inheritDoc}
     */
    public void close() {
        if (!closed) {
            closed = true;
            context.close();
            bapiMappings.clear();
            converterCache.clear();
            executionInterceptors.clear();
            bapiInterceptors.clear();
        }
    }

    /*
     * {@inheritDoc}
     */
    public boolean isClosed() {
        return closed;
    }

    /*
     * {@inheritDoc}
     */
    public Map<String, BapiMapping> getBapiMappings() {
        assertNotClosed();
        return Collections.unmodifiableMap(bapiMappings);
    }

    /*
     * {@inheritDoc}
     */
    public ConverterCache getConverterCache() {
        assertNotClosed();
        return this.converterCache;
    }

    /*
     * {@inheritDoc}
     */
    public SessionManagerConfig getConfig() {
        assertNotClosed();
        return config;
    }

    /*
     * {@inheritDoc}
     */
    public Context getContext() {
        assertNotClosed();
        return context;
    }

    /*
     * {@inheritDoc}
     */
    public Session openSession() {
        assertNotClosed();
        return new SessionImpl(this);
    }

    /*
     * {@inheritDoc}
     */
    public Session openSession(final Credentials credentials) {
        assertNotClosed();
        return new SessionImpl(this, credentials);
    }

    /*
     * {@inheritDoc}
     */
    public Set<ExecutionInterceptor> getExecutionInterceptors() {
        assertNotClosed();
        return executionInterceptors;
    }

    /*
     * {@inheritDoc}
     */
    public Set<BapiInterceptor> getBapiInterceptors() {
        assertNotClosed();
        return bapiInterceptors;
    }

    private void assertNotClosed() {
        if (closed) {
            throw new HibersapException("The SessionManager has been closed, it must not be used anymore");
        }
    }

    @Override
    public String toString() {
        String format = "SessionManagerImpl"
                + "[Config=[%s], ContextClass=[%s], Converters=[%s], Interceptors=[%s], BapiMappings=[%s]]";
        return String.format(format, config.toString(), context.toString(), converterCache.toString(),
                executionInterceptors, bapiMappings);
    }

    private void readObject(final ObjectInputStream stream) throws ClassNotFoundException, IOException {
        stream.defaultReadObject();

        context = ConfigurationHelper.createContext(config);
        converterCache = new ConverterCache();
        bapiInterceptors = ConfigurationHelper.createBapiInterceptors(config);
        executionInterceptors = ConfigurationHelper.createExecutionInterceptors(config);
    }
}
