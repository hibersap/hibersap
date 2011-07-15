/*
 * Copyright (c) 2009, 2011 akquinet tech@spree GmbH.
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

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class EnvironmentTest
{
    @Test
    public void loadsHibersapVersionFromFile()
    {
        final String regex = "^(\\d+\\.\\d+\\.\\d+)((-SNAPSHOT)|([A-Za-z][0-9A-Za-z-]*))?$";

/*
        assertTrue( "12.10.9".matches( regex ) );
        assertFalse( "1.1".matches( regex ) );
        assertFalse( "1.1.0.2".matches( regex ) );

        assertTrue( "12.10.9-SNAPSHOT".matches( regex ) );
        assertFalse( "12.10.9-SNAPSHOT2".matches( regex ) );

        assertTrue( "1.1.0Beta2".matches( regex ) );
        assertFalse( "1.1.0.Beta2".matches( regex ) );
        assertFalse( "1.1.0-Beta2".matches( regex ) );
*/

        assertTrue( Environment.VERSION.matches( regex ) );
    }
}
