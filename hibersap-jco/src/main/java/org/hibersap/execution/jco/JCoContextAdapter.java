/*
 * Copyright (c) 2008-2014 akquinet tech@spree GmbH
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
import com.sap.conn.jco.JCoException;

/**
 * This interface acts as an adapter to the JCoContext class. Since JCoContext
 * is not not an interface, but provides static methods instead, it is not testable.
 *
 * @author Carsten Erker
 */
public interface JCoContextAdapter {

    void begin( JCoDestination destination );

    void end( JCoDestination destination )
            throws JCoException;

    boolean isStateful( JCoDestination destination );
}
