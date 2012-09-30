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

package org.hibersap.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.session.Context;
import org.junit.Test;

public class ContextFactoryTest
{
    @Test
    public void testInitializesContextClass()
        throws Exception
    {
        SessionManagerConfig config = new SessionManagerConfig( "Test" ).setContext( DummyContext.class.getName() );
        Context context = ContextFactory.create( config );

        assertNotNull( context );
        assertEquals( DummyContext.class, context.getClass() );
    }
}
