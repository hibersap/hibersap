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

package org.hibersap.configuration.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.ConfigurationException;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"sessionManagers"})
@XmlRootElement(name = "hibersap")
public final class HibersapConfig implements Serializable {

    @XmlTransient
    public static final String NAMESPACE = "http://hibersap.org/xml/ns/hibersap-config";

    @XmlTransient
    private static final Log LOG = LogFactory.getLog(HibersapConfig.class);

    @XmlTransient
    private static final long serialVersionUID = 1;

    @XmlElement(name = "session-manager", required = true)
    protected List<SessionManagerConfig> sessionManagers = new ArrayList<>();

    public HibersapConfig() {
        LOG.trace("created");
    }

    HibersapConfig(final SessionManagerConfig sessionManager) {
        this.sessionManagers.add(sessionManager);
    }

    public List<SessionManagerConfig> getSessionManagers() {
        return sessionManagers;
    }

    public void setSessionManagers(final List<SessionManagerConfig> sessionManagers) {
        this.sessionManagers.clear();
        this.sessionManagers.addAll(sessionManagers);
    }

    public SessionManagerConfig getSessionManager(final String name) {
        for (SessionManagerConfig config : sessionManagers) {
            if (config.getName().equals(name)) {
                return config;
            }
        }
        throw new ConfigurationException("No configuration found for Session Manager name '" + name + "'");
    }

    public SessionManagerConfig addSessionManager(final String name) {
        SessionManagerConfig config = new SessionManagerConfig(name);
        sessionManagers.add(config);
        return config;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HibersapConfig that = (HibersapConfig) o;

        if (sessionManagers != null ? !sessionManagers.equals(that.sessionManagers) : that.sessionManagers != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return sessionManagers != null ? sessionManagers.hashCode() : 0;
    }
}
