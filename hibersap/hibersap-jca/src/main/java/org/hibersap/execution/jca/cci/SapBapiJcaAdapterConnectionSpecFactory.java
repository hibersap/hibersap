/*
 * Copyright (c) 2008-2012 akquinet tech@spree GmbH
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

package org.hibersap.execution.jca.cci;

import javax.resource.cci.ConnectionSpec;

import org.hibersap.InternalHiberSapException;
import org.hibersap.session.Credentials;

public class SapBapiJcaAdapterConnectionSpecFactory
    extends AbstractConnectionSpecFactory

{
    private static final String CONNECTION_SPEC_IMPL_CLASS_NAME = "net.sf.sapbapijca.adapter.cci.ConnectionSpecImpl";

    public ConnectionSpec createConnectionSpec( Credentials credentials )
        throws InternalHiberSapException
    {
        Class<?> connSpecClass;
        try
        {
            connSpecClass = getConnectionSpecClass( CONNECTION_SPEC_IMPL_CLASS_NAME );

            Object[] arguments = new Object[] {
                credentials.getUser(),
                credentials.getPassword(),
                credentials.getLanguage() };
            Class<?>[] parameterTypes = new Class<?>[] { String.class, String.class, String.class };
            Object connSpecImpl = newConnectionSpecInstance( connSpecClass, parameterTypes, arguments );

            return (ConnectionSpec) connSpecImpl;
        }
        catch ( IllegalArgumentException e )
        {
            throw new InternalHiberSapException( e.getMessage(), e );
        }
        catch ( ClassNotFoundException e )
        {
            throw new InternalHiberSapException( e.getMessage(), e );
        }
    }
}
