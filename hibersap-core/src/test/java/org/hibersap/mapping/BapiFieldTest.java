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

package org.hibersap.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibersap.annotations.Table;
import org.junit.Test;

/**
 * @author Carsten Erker
 */
public class BapiFieldTest
{
    @Table
    @SuppressWarnings("unused")
    private List<String> listGeneric;

    @Table
    @SuppressWarnings("unused")
    private Integer[] integerArray;

    @SuppressWarnings("unused")
    private Collection<?> list;

    @SuppressWarnings("unused")
    private Map<Integer, String> mapGeneric;

    @SuppressWarnings("unused")
    private int[] intArray;

    @SuppressWarnings("unused")
    private int intPrimitive;

    @SuppressWarnings( { "unchecked", "unused" })
    private ArrayList listSimple;

    @Test
    public void testGetActualType()
        throws NoSuchFieldException
    {
        BapiField field = new BapiField( getClass().getDeclaredField( "listGeneric" ) );
        assertEquals( String.class, field.getAssociatedType() );

        field = new BapiField( getClass().getDeclaredField( "integerArray" ) );
        assertEquals( Integer.class, field.getAssociatedType() );

        field = new BapiField( getClass().getDeclaredField( "intPrimitive" ) );
        assertEquals( int.class, field.getAssociatedType() );
    }

    @Test
    public void testGetArrayType()
        throws NoSuchFieldException
    {
        BapiField fieldIntegerArray = new BapiField( getClass().getDeclaredField( "integerArray" ) );
        assertEquals( Integer.class, fieldIntegerArray.getArrayType() );

        BapiField fieldIntArray = new BapiField( getClass().getDeclaredField( "intArray" ) );
        assertEquals( int.class, fieldIntArray.getArrayType() );
    }

    @Test
    public void testGetGenericType()
        throws NoSuchFieldException
    {
        BapiField field = new BapiField( getClass().getDeclaredField( "listGeneric" ) );
        assertEquals( String.class, field.getGenericType() );

        field = new BapiField( getClass().getDeclaredField( "list" ) );
        assertNull( field.getGenericType() );

        field = new BapiField( getClass().getDeclaredField( "listSimple" ) );
        assertNull( field.getGenericType() );

        field = new BapiField( getClass().getDeclaredField( "mapGeneric" ) );
        assertNull( field.getGenericType() );

        field = new BapiField( getClass().getDeclaredField( "integerArray" ) );
        assertEquals( null, field.getGenericType() );
    }
}
