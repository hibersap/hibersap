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

package org.hibersap.mapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.hibersap.annotations.Changing;
import org.hibersap.annotations.Table;
import org.junit.Test;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Carsten Erker
 */
public class BapiFieldTest {

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

    @SuppressWarnings({"unchecked", "unused"})
    private ArrayList listSimple;

    @Changing
    private String changing;

    @Test
    public void getsActualType() throws NoSuchFieldException {
        BapiField field = new BapiField(getClass().getDeclaredField("listGeneric"));
        assertEquals(String.class, field.getAssociatedType());

        field = new BapiField(getClass().getDeclaredField("integerArray"));
        assertEquals(Integer.class, field.getAssociatedType());

        field = new BapiField(getClass().getDeclaredField("intPrimitive"));
        assertEquals(int.class, field.getAssociatedType());
    }

    @Test
    public void getsArrayType() throws NoSuchFieldException {
        BapiField fieldIntegerArray = new BapiField(getClass().getDeclaredField("integerArray"));
        assertEquals(Integer.class, fieldIntegerArray.getArrayType());

        BapiField fieldIntArray = new BapiField(getClass().getDeclaredField("intArray"));
        assertEquals(int.class, fieldIntArray.getArrayType());
    }

    @Test
    public void getsGenericType()
            throws NoSuchFieldException {
        BapiField field = new BapiField(getClass().getDeclaredField("listGeneric"));
        assertEquals(String.class, field.getGenericType());

        field = new BapiField(getClass().getDeclaredField("list"));
        assertNull(field.getGenericType());

        field = new BapiField(getClass().getDeclaredField("listSimple"));
        assertNull(field.getGenericType());

        field = new BapiField(getClass().getDeclaredField("mapGeneric"));
        assertNull(field.getGenericType());

        field = new BapiField(getClass().getDeclaredField("integerArray"));
        assertEquals(null, field.getGenericType());
    }

    @Test
    public void getsChangingParameter() throws Exception {
        BapiField field = new BapiField(getClass().getDeclaredField("changing"));
        assertThat(field.getAssociatedType()).isEqualTo(String.class);
        assertThat(field.isChanging()).isTrue();
    }
}
