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

package org.hibersap.ejb.util;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import java.util.Hashtable;

public class MockInitialContextFactory implements InitialContextFactory {

    public static String NAME = "org.hibersap.ejb.util.MockInitialContextFactory";

    private static Context mockContext;

    public static void setMockContext( final Context mockContext ) {
        MockInitialContextFactory.mockContext = mockContext;
    }

    public Context getInitialContext( final Hashtable<?, ?> environment ) throws NamingException {
        if ( mockContext == null ) {
            throw new IllegalStateException( "No InitialContext set" );
        }
        return mockContext;
    }
}