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

package org.hibersap.ejb.util;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

public class MockInitialContextFactory implements InitialContextFactory
{
    public static String NAME = "org.hibersap.ejb.util.MockInitialContextFactory";

    private static Context mockContext;

    public Context getInitialContext( java.util.Hashtable<?, ?> environment ) throws NamingException
    {
        if ( mockContext == null )
        {
            throw new IllegalStateException( "No InitialContext set" );
        }
        return mockContext;
    }

    public static void setMockContext( Context mockContext )
    {
        MockInitialContextFactory.mockContext = mockContext;
    }
}