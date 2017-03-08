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

package org.hibersap.mapping;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.hibersap.HibersapException;
import org.hibersap.annotations.Export;
import org.junit.Test;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;
import static org.hibersap.mapping.ReflectionHelper.getDeclaredFieldsWithAnnotationRecursively;

public class ReflectionHelperTest {

    @Test
    public void getClassForNameReturnsObject() throws ClassNotFoundException {
        Class<?> clazz = ReflectionHelper.getClassForName( Object.class.getName() );

        assertThat( clazz ).isSameAs( Object.class );
    }

    @Test( expected = ClassNotFoundException.class )
    public void getClassForNameThrowsExceptionWhenClassNotFound() throws ClassNotFoundException {
        ReflectionHelper.getClassForName( "NotExistent" );
    }

    @Test
    public void getArrayTypeReturnsCorrectTypeForObjectArray() {
        Class<?> clazz = ReflectionHelper.getArrayType( Object[].class );
        assertThat( clazz ).isSameAs( Object.class );
    }

    @Test
    public void getArrayTypeReturnsNullWhenParameterTypeIsNotAnArray() {
        Class<?> clazz = ReflectionHelper.getArrayType( Object.class );
        assertThat( clazz ).isNull();
    }

    @Test
    public void getDeclaredFieldReturnsCorrectField() throws Exception {
        TestBean bean = new TestBean();
        Field field = ReflectionHelper.getDeclaredField( bean, "intValue" );
        assertThat( field.getName() ).isEqualTo( "intValue" );
    }

    @Test( expected = HibersapException.class )
    public void getDeclaredFieldThrowsExceptionWhenFieldDoesNotExist() throws Exception {
        ReflectionHelper.getDeclaredField( new TestBean(), "notExistent" );
    }

    @Test
    public void getFieldValueReturnsCorrectValue() {
        int value = (Integer) ReflectionHelper.getFieldValue( new TestBean(), "intValue" );
        assertThat( value ).isEqualTo( 1 );
    }

    @Test
    public void getFieldValueReturnsCorrectValueForInheritedField() {
        int value = (Integer) ReflectionHelper.getFieldValue( new TestSubClass(), "intValue" );
        assertThat( value ).isEqualTo( 1 );
    }

    @Test( expected = HibersapException.class )
    public void testGetFieldValueThrowsExceptionWhenFieldDoesNotExist() {
        ReflectionHelper.getFieldValue( new TestBean(), "notExistent" );
    }

    @Test
    public void getGenericTypeReturnsCorrectTypeOfSet() throws Exception {
        Field field = TestBean.class.getDeclaredField( "set" );
        Class<?> clazz = ReflectionHelper.getGenericType( field );
        assertThat( clazz ).isEqualTo( Object.class );
    }

    @Test
    public void getGenericTypeReturnsNullIfFieldHasNoGenericType() throws Exception {
        Field field = TestBean.class.getDeclaredField( "intValue" );
        Class<?> clazz = ReflectionHelper.getGenericType( field );
        assertThat( clazz ).isNull();
    }

    @Test
    public void newCollectionInstanceCanCreateArrayList() {
        Collection<Object> collection = ReflectionHelper.newCollectionInstance( ArrayList.class );
        assertThat( collection.getClass() ).isSameAs( ArrayList.class );
    }

    @Test( expected = HibersapException.class )
    public void newCollectionInstanceThrowsExceptionForInterface() {
        ReflectionHelper.newCollectionInstance( List.class );
    }

    @Test
    public void newInstanceCanCreateStringInstance() {
        Object instance = ReflectionHelper.newInstance( String.class );
        assertThat( instance.getClass() ).isSameAs( String.class );
    }

    @Test
    @SuppressWarnings( "synthetic-access" )
    public void setFieldValueCanChangeIntValue() {
        TestBean bean = new TestBean();
        ReflectionHelper.setFieldValue( bean, "intValue", 2 );
        assertThat( bean.intValue ).isEqualTo( 2 );
    }

    @Test( expected = HibersapException.class )
    @SuppressWarnings( "NullableProblems" )
    public void setFieldValueThrowsExceptionWhenTryingToSetValueOnNullObject() {
        ReflectionHelper.setFieldValue( null, "intValue", 0 );
    }

    @Test( expected = HibersapException.class )
    @SuppressWarnings( "NullableProblems" )
    public void setFieldValueThrowsExceptionWhenSettingNullValueOnPrimitiveType() {
        TestBean bean = new TestBean();
        ReflectionHelper.setFieldValue( bean, "intValue", null );
    }

    @Test
    @SuppressWarnings( "NullableProblems" )
    public void setFieldValueCanSetNullValue() {
        TestBean bean = new TestBean();
        bean.set = Collections.emptySet();

        ReflectionHelper.setFieldValue( bean, "set", null );
        assertThat( bean.set ).isNull();
    }

    @Test
    public void setFieldValueWhenFieldIsInheritedFromSuperclass() {
        TestSubClass bean = new TestSubClass();

        ReflectionHelper.setFieldValue( bean, "set", Collections.emptySet() );
        assertThat( bean.getSet() ).isNotNull();
    }

    @Test
    public void setFieldValueOnDirectMember() {
        TestSubClass bean = new TestSubClass();

        ReflectionHelper.setFieldValue( bean, "paramSubClass", 3L );
        assertThat( bean.paramSubClass ).isEqualTo( 3L );
    }

    @Test
    public void setFieldValueThrowsHibersapExceptionWithMessageContainingClassAndFieldNameWhenFieldDoesNotExist() {
        try {
            ReflectionHelper.setFieldValue( new Object(), "doesNotExist", "someValue" );
            fail();
        } catch ( HibersapException e ) {
            assertThat( e.getMessage() ).contains( "Object" );
            assertThat( e.getMessage() ).contains( "doesNotExist" );
        }
    }

    @Test
    public void createsNewInstanceFromClassNameAndReturnsSupertype() {
        final CharSequence charSequence = ReflectionHelper.newInstance( "java.lang.String", CharSequence.class );

        assertThat( charSequence ).isEqualTo( "" );
    }

    @Test
    public void getsDeclaredFieldsRecursivelyWithInheritance() {
        final Set<Field> fields = getDeclaredFieldsWithAnnotationRecursively( TestSubClass.class, Export.class );

        assertThat( fields ).hasSize( 2 );
        assertThat( fields ).onProperty( "name" ).contains( "set", "paramSubClass" );
    }

    @Test
    public void getsDeclaredFieldsRecursivelyWithoutInheritance() {
        final Set<Field> fields = getDeclaredFieldsWithAnnotationRecursively( TestBean.class, Export.class );

        assertThat( fields ).hasSize( 1 );
        assertThat( fields ).onProperty( "name" ).contains( "set" );
    }

    @Test
    public void newArrayFromCollectionReturnsNullWhenCollectionIsNull() throws Exception {
        Object[] objects = ReflectionHelper.newArrayFromCollection( null, Integer.class );

        assertThat( objects ).isNull();
    }

    @Test
    public void newArrayFromCollectionReturnsArrayOfSizeZeroWhenCollectionIsEmpty() throws Exception {
        Object[] objects = ReflectionHelper.newArrayFromCollection( emptySet(), Integer.class );

        assertThat( objects ).hasSize( 0 );
    }

    @Test
    public void newArrayFromCollectionReturnsArrayOfSizeTwoWhenCollectionHasTwoElements() throws Exception {
        List<Integer> list = asList( 1, 2 );
        Integer[] objects = ReflectionHelper.newArrayFromCollection( list, Integer.class );

        assertThat( objects ).containsOnly( 1, 2 );
    }

    @SuppressWarnings( "unused" )
    class TestBean {

        private int intValue = 1;

        @Export
        private Set<Object> set;

        public Set<Object> getSet() {
            return set;
        }
    }

    private class TestSubClass extends TestBean {

        @Export
        @SuppressWarnings( "unused" )
        private long paramSubClass;
    }
}
