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

import java.util.Arrays;
import javax.resource.cci.ConnectionSpec;
import org.hibersap.InternalHiberSapException;
import org.hibersap.session.Credentials;

public class CuckooJcaAdapterConnectionSpecFactory extends AbstractConnectionSpecFactory {

    private static final String CONNECTION_SPEC_IMPL_CLASS_NAME = "org.cuckoo.ra.cci.ApplicationPropertiesImpl";

    public ConnectionSpec createConnectionSpec( Credentials credentials ) throws InternalHiberSapException {
        try {
            Object[] arguments = {
                    credentials.getUser(),
                    credentials.getPassword()
//                    ,
//                    credentials.getLanguage(),
//                    credentials.getClient(),
//                    credentials.getAliasUser(),
//                    credentials.getSsoTicket(),
//                    credentials.getX509Certificate()
            };

            Class<?>[] parameterTypes = new Class<?>[arguments.length];
            Arrays.fill( parameterTypes, String.class );

            Class<?> connSpecClass = getConnectionSpecClass( CONNECTION_SPEC_IMPL_CLASS_NAME );
            return newConnectionSpecInstance( connSpecClass, parameterTypes, arguments );
        } catch ( IllegalArgumentException e ) {
            throw new InternalHiberSapException( e.getMessage(), e );
        } catch ( ClassNotFoundException e ) {
            throw new InternalHiberSapException( e.getMessage(), e );
        }
    }
}
