/*
 * Copyright (c) 2008-2017 akquinet tech@spree GmbH
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

package org.hibersap.execution.jca.cci;

import javax.resource.cci.ConnectionSpec;
import org.hibersap.InternalHiberSapException;
import org.hibersap.session.Credentials;

/**
 * Factory for creating instances of ConnectionSpec implementations. Decouples Hibersap from the
 * ConnectionSpec implementation and thus from the actually used JCA implementation.
 *
 * @author Carsten Erker
 */
public interface ConnectionSpecFactory {

    /**
     * Creates an instance of the ConnectionSpec implementation using the specified credentials.
     *
     * @param credentials The credentials to populate to the ConnectionSpec object.
     * @return The ConnectionSpec object, initialized with the credential properties.
     * @throws InternalHiberSapException
     */
    ConnectionSpec createConnectionSpec( Credentials credentials ) throws InternalHiberSapException;
}
