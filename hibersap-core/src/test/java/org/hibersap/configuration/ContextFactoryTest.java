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

package org.hibersap.configuration;

import org.hibersap.ConfigurationException;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.session.Context;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ContextFactoryTest {

    @Test
    public void initializesContextClass()
            throws Exception {
        SessionManagerConfig config = new SessionManagerConfig("Test").setContext(DummyContext.class.getName());
        Context context = ContextFactory.create(config);

        assertNotNull(context);
        assertEquals(DummyContext.class, context.getClass());
    }

    @Test(expected = ConfigurationException.class)
    public void throwsExceptionWhenGivenContextClassDoesNotExtendContext() throws Exception {
        SessionManagerConfig config = new SessionManagerConfig("Test").setContext(String.class.getName());
        ContextFactory.create(config);
    }
}
