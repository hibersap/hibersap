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

package org.hibersap.conversion;

import static org.junit.Assert.assertEquals;

import org.hibersap.HibersapException;
import org.junit.Test;

public class ConverterCacheTest
{
    private ConverterCache cache = new ConverterCache();

    @Test
    public void testCreatesOneInstanceOfEachClass()
    {
        cache.getConverter( CharConverter.class );
        assertEquals( 1, cache.getSize() );

        cache.getConverter( CharConverter.class );
        assertEquals( 1, cache.getSize() );

        cache.getConverter( BooleanConverter.class );
        assertEquals( 2, cache.getSize() );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThrowsExceptionOnNullArgument()
    {
        cache.getConverter( null );
    }

    @Test(expected = HibersapException.class)
    public void testThrowsHibersapExceptionIfNotInstantiable()
    {
        cache.getConverter( Converter.class );
    }
}
