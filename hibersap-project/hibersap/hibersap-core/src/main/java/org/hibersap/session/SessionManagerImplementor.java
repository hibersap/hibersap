package org.hibersap.session;

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

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibersap.configuration.Settings;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.conversion.ConverterCache;
import org.hibersap.mapping.model.BapiMapping;

/**
 * The client's interface to the SessionManager. A SessionManager is used to create Hibersap
 * sessions.
 * 
 * @author Carsten Erker
 */
public interface SessionManagerImplementor
    extends Serializable
{

    /**
     * Gets this SessionManager's BapiMappings.
     * 
     * @return A BapiMappings for Classes map.
     */
    Map<Class<?>, BapiMapping> getBapiMappings();

    ConverterCache getConverterCache();

    SessionManagerConfig getConfig();

    Settings getSettings();

    void reset();

    /**
     * Open a Session using a newly created connection to SAP.
     * 
     * @return Session
     */
    Session openSession();

    List<ExecutionInterceptor> getInterceptors();
}
