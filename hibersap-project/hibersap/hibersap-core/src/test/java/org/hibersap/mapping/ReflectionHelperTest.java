package org.hibersap.mapping;

import org.hibersap.HibersapException;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class ReflectionHelperTest
{

    @Test
    public void testGetClassForName()
            throws ClassNotFoundException
    {
        Class<?> clazz = ReflectionHelper.getClassForName( Object.class.getName() );
        assertEquals( Object.class, clazz );
    }

    @Test( expected = ClassNotFoundException.class )
    public void testGetClassForNameNotFound()
            throws ClassNotFoundException
    {
        ReflectionHelper.getClassForName( "NotExistent" );
    }

    @Test
    public void testGetArrayType()
    {
        Class<?> clazz = ReflectionHelper.getArrayType( Object[].class );
        assertEquals( Object.class, clazz );

        clazz = ReflectionHelper.getArrayType( Object.class );
        assertNull( clazz );
    }

    @Test
    public void testGetDeclaredField()
            throws Exception
    {
        TestBean bean = new TestBean();
        Field field = ReflectionHelper.getDeclaredField( bean, "intValue" );
        assertEquals( "intValue", field.getName() );
    }

    @Test( expected = HibersapException.class )
    public void testGetDeclaredFieldNotExistent()
            throws Exception
    {
        ReflectionHelper.getDeclaredField( new TestBean(), "notExistent" );
    }

    @Test
    public void testGetFieldValue()
    {
        int value = ( Integer ) ReflectionHelper.getFieldValue( new TestBean(), "intValue" );
        assertEquals( 1, value );
    }

    @Test( expected = HibersapException.class )
    public void testGetFieldValueNotExistent()
    {
        ReflectionHelper.getFieldValue( new TestBean(), "notExistent" );
    }

    @Test
    public void testGetGenericType()
            throws Exception
    {
        Field field = TestBean.class.getDeclaredField( "set" );
        Class<?> clazz = ReflectionHelper.getGenericType( field );
        assertEquals( Object.class, clazz );
    }

    @Test
    public void testGetGenericTypeNotGeneric()
            throws Exception
    {
        Field field = TestBean.class.getDeclaredField( "intValue" );
        Class<?> clazz = ReflectionHelper.getGenericType( field );
        assertNull( clazz );
    }

    @Test
    public void testNewCollectionInstance()
    {
        Collection<Object> collection = ReflectionHelper.newCollectionInstance( ArrayList.class );
        assertEquals( ArrayList.class, collection.getClass() );
    }

    @Test( expected = HibersapException.class )
    public void testNewCollectionInstanceCannotInstanciate()
    {
        ReflectionHelper.newCollectionInstance( List.class );
    }

    @Test
    public void testNewInstance()
    {
        Object instance = ReflectionHelper.newInstance( String.class );
        assertEquals( String.class, instance.getClass() );
    }

    @Test
    @SuppressWarnings( "synthetic-access" )
    public void testSetFieldValue()
    {
        TestBean bean = new TestBean();
        assertEquals( 1, bean.intValue );
        ReflectionHelper.setFieldValue( bean, "intValue", 2 );
        assertEquals( 2, bean.intValue );
    }

    @Test( expected = HibersapException.class )
    public void testSetFieldValueNullObject()
    {
        ReflectionHelper.setFieldValue( null, "intValue", 0 );
    }

    @Test( expected = HibersapException.class )
    public void testSetFieldValueNullValue()
    {
        TestBean bean = new TestBean();
        ReflectionHelper.setFieldValue( bean, "intValue", null );
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
