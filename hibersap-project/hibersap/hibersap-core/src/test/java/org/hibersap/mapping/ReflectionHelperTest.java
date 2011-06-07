package org.hibersap.mapping;

import org.hibersap.HibersapException;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class ReflectionHelperTest
{

    @Test
    public void getClassForNameReturnsObject() throws ClassNotFoundException
    {
        Class<?> clazz = ReflectionHelper.getClassForName( Object.class.getName() );

        assertEquals( Object.class, clazz );
    }

    @Test( expected = ClassNotFoundException.class )
    public void getClassForNameThrowsExceptionWhenClassNotFound() throws ClassNotFoundException
    {
        ReflectionHelper.getClassForName( "NotExistent" );
    }

    @Test
    public void getArrayTypeReturnsCorrectTypeForObjectArray()
    {
        Class<?> clazz = ReflectionHelper.getArrayType( Object[].class );
        assertEquals( Object.class, clazz );
    }

    @Test
    public void getArrayTypeReturnsNullWhenParameterTypeIsNotAnArray()
    {
        Class<?> clazz = ReflectionHelper.getArrayType( Object.class );
        assertNull( clazz );
    }

    @Test
    public void getDeclaredFieldReturnsCorrectField() throws Exception
    {
        TestBean bean = new TestBean();
        Field field = ReflectionHelper.getDeclaredField( bean, "intValue" );
        assertEquals( "intValue", field.getName() );
    }

    @Test( expected = HibersapException.class )
    public void getDeclaredFieldThrowsExceptionWhenFieldDoesNotExist() throws Exception
    {
        ReflectionHelper.getDeclaredField( new TestBean(), "notExistent" );
    }

    @Test
    public void getFieldValueReturnsCorrectValue()
    {
        int value = ( Integer ) ReflectionHelper.getFieldValue( new TestBean(), "intValue" );
        assertEquals( 1, value );
    }

    @Test( expected = HibersapException.class )
    public void testGetFieldValueThrowsExceptionWhenFieldDoesNotExist()
    {
        ReflectionHelper.getFieldValue( new TestBean(), "notExistent" );
    }

    @Test
    public void getGenericTypeReturnsCorrectTypeOfSet() throws Exception
    {
        Field field = TestBean.class.getDeclaredField( "set" );
        Class<?> clazz = ReflectionHelper.getGenericType( field );
        assertEquals( Object.class, clazz );
    }

    @Test
    public void getGenericTypeReturnsNullIfFieldHasNoGenericType() throws Exception
    {
        Field field = TestBean.class.getDeclaredField( "intValue" );
        Class<?> clazz = ReflectionHelper.getGenericType( field );
        assertNull( clazz );
    }

    @Test
    public void newCollectionInstanceCanCreateArrayList()
    {
        Collection<Object> collection = ReflectionHelper.newCollectionInstance( ArrayList.class );
        assertEquals( ArrayList.class, collection.getClass() );
    }

    @Test( expected = HibersapException.class )
    public void newCollectionInstanceThrowsExceptionForInterface()
    {
        ReflectionHelper.newCollectionInstance( List.class );
    }

    @Test
    public void newInstanceCanCreateStringInstance()
    {
        Object instance = ReflectionHelper.newInstance( String.class );
        assertEquals( String.class, instance.getClass() );
    }

    @Test
    @SuppressWarnings( "synthetic-access" )
    public void setFieldValueCanChangeIntValue()
    {
        TestBean bean = new TestBean();
        ReflectionHelper.setFieldValue( bean, "intValue", 2 );
        assertEquals( 2, bean.intValue );
    }

    @Test( expected = HibersapException.class )
    public void setFieldValueThrowsExceptionWhenTryingToSetValueOnNullObject()
    {
        ReflectionHelper.setFieldValue( null, "intValue", 0 );
    }

    @Test( expected = HibersapException.class )
    public void setFieldValueThrowsExceptionWhenSettingNullValueOnPrimitiveType()
    {
        TestBean bean = new TestBean();
        ReflectionHelper.setFieldValue( bean, "intValue", null );
    }

    @Test
    public void setFieldValueCanSetNullValue()
    {
        TestBean bean = new TestBean();
        bean.set = Collections.emptySet();

        ReflectionHelper.setFieldValue( bean, "set", null );
        assertThat(bean.set, nullValue());
    }

    @Test
    public void createsNewInstanceFromClassNameAndReturnsSupertype()
    {
        final CharSequence charSequence = ReflectionHelper.newInstance( "java.lang.String", CharSequence.class );

        assertThat( ( String ) charSequence, equalTo( "" ) );
    }

    @SuppressWarnings( "unused" )
    class TestBean
    {
        private int intValue = 1;

        private Set<Object> set;
    }
}
