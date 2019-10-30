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

package org.hibersap.execution.jca;

import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.ConnectionSpec;
import org.hibersap.ConfigurationException;
import org.hibersap.HibersapException;
import org.hibersap.execution.jca.cci.ConnectionSpecFactory;
import org.hibersap.mapping.ReflectionHelper;
import org.hibersap.session.Credentials;

public class ConnectionProvider {

    private final ConnectionFactory connectionFactory;

    private final String connectionSpecFactoryName;

    private Connection connection;

    private Credentials credentials;

    ConnectionProvider(final ConnectionFactory connectionFactory, final String connectionSpecFactoryName) {
        this.connectionFactory = connectionFactory;
        this.connectionSpecFactoryName = connectionSpecFactoryName;
    }

    Connection getConnection() {
        if (connection == null) {
            connection = newConnection();
        }
        return connection;
    }

    void setCredentials(final Credentials credentials) {
        this.credentials = credentials;
    }

    private Connection newConnection() {
        try {
            if (credentials == null) {
                return connectionFactory.getConnection();
            } else {
                ConnectionSpec connectionSpec = newConnectionSpecFactory(connectionSpecFactoryName)
                        .createConnectionSpec(credentials);
                return connectionFactory.getConnection(connectionSpec);
            }
        } catch (ResourceException e) {
            throw new HibersapException("Problem creating Connection", e);
        }
    }

    private ConnectionSpecFactory newConnectionSpecFactory(final String className) {
        Class<?> clazz;
        try {
            clazz = ReflectionHelper.getClassForName(className);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationException("ConnectionSpecFactory implementation class not found: " + className, e);
        }

        if (!ConnectionSpecFactory.class.isAssignableFrom(clazz)) {
            throw new ConfigurationException("Class " + clazz.getName() + " does not implement "
                    + ConnectionSpecFactory.class.getName());
        }

        Object newInstance = ReflectionHelper.newInstance(clazz);

        return (ConnectionSpecFactory) newInstance;
    }
}
