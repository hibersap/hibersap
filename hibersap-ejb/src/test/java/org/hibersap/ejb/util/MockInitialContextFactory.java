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

package org.hibersap.ejb.util;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.spi.InitialContextFactory;

public class MockInitialContextFactory implements InitialContextFactory {

    public static String NAME = "org.hibersap.ejb.util.MockInitialContextFactory";

    private static Context mockContext;

    public static void setMockContext(final Context mockContext) {
        MockInitialContextFactory.mockContext = mockContext;
    }

    public Context getInitialContext(final Hashtable<?, ?> environment) {
        if (mockContext == null) {
            throw new IllegalStateException("No InitialContext set");
        }
        return mockContext;
    }
}