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

import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;

public class JCoContextAdapterImpl implements JCoContextAdapter {

    public void begin( final JCoDestination destination ) {
        JCoContext.begin( destination );
    }

    public void end( final JCoDestination destination )
            throws JCoException {
        JCoContext.end( destination );
    }

    public boolean isStateful( final JCoDestination destination ) {
        return JCoContext.isStateful( destination );
    }
}
