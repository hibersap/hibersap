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

package org.hibersap.execution.jca.cci;

import org.hibersap.InternalHiberSapException;
import org.hibersap.session.Credentials;

import javax.resource.cci.ConnectionSpec;

public class SapBapiJcaAdapterConnectionSpecFactory extends AbstractConnectionSpecFactory {

    private static final String CONNECTION_SPEC_IMPL_CLASS_NAME = "net.sf.sapbapijca.adapter.cci.ConnectionSpecImpl";

    public ConnectionSpec createConnectionSpec( final Credentials credentials )
            throws InternalHiberSapException {
        Class<?> connSpecClass;
        try {
            connSpecClass = getConnectionSpecClass( CONNECTION_SPEC_IMPL_CLASS_NAME );

            Object[] arguments = new Object[]{
                    credentials.getUser(),
                    credentials.getPassword(),
                    credentials.getLanguage()};
            Class<?>[] parameterTypes = new Class<?>[]{String.class, String.class, String.class};
            Object connSpecImpl = newConnectionSpecInstance( connSpecClass, parameterTypes, arguments );

            return (ConnectionSpec) connSpecImpl;
        } catch ( IllegalArgumentException e ) {
            throw new InternalHiberSapException( e.getMessage(), e );
        } catch ( ClassNotFoundException e ) {
            throw new InternalHiberSapException( e.getMessage(), e );
        }
    }
}
