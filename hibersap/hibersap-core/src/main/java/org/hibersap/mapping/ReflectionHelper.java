package org.hibersap.mapping;

/**
 * Copyright (C) 2008-2009 akquinet tech@spree GmbH
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

import org.hibersap.HibersapException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Carsten Erker
 */
public final class ReflectionHelper
{
    private static final String MSG_CAN_NOT_CREATE_INSTANCE = "Can not create an instance of type ";

    public static Class<?> getClassForName( String className )
            throws ClassNotFoundException
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try
        {
            return Class.forName( className, true, classLoader );
        }
        catch ( ClassNotFoundException e )
        {
            return Class.forName( className );
        }
    }

    /**
     * Get the array type of type, or null if type is not an array.
     *
     * @param type The array class type.
     * @return The type of the array components.
     */
    public static Class<?> getArrayType( Class<?> type )
    {
        if ( type.isArray() )
        {
            return getClass( type.getComponentType() );
        }
        return null;
    }

    /**
     * Get the underlying class for a type, or null if the type is a variable type. Stolen from:
     * http://www.artima.com/weblogs/viewpost.jsp?thread=208860
     *
     * @param type The type
     * @return the underlying class
     */
    private static Class<?> getClass( Type type )
    {
        if ( type instanceof Class )
        {
            return ( Class<?> ) type;
        }
        else if ( type instanceof ParameterizedType )
        {
            return getClass( ( ( ParameterizedType ) type ).getRawType() );
        }
        else if ( type instanceof GenericArrayType )
        {
            Type componentType = ( ( GenericArrayType ) type ).getGenericComponentType();
            Class<?> componentClass = getClass( componentType );
            if ( componentClass != null )
            {
                return Array.newInstance( componentClass, 0 ).getClass();
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    public static Field getDeclaredField( Object bean, String fieldName )
    {
        Class<?> clazz = bean.getClass();
        try
        {
            return clazz.getDeclaredField( fieldName );
        }
        catch ( SecurityException e )
        {
            throw new HibersapException( "Field " + bean.getClass() + "." + fieldName + " is not accessible", e );
        }
        catch ( NoSuchFieldException e )
        {
            throw new HibersapException( "Field " + bean.getClass() + "." + fieldName + " does not exist in class "
                    + clazz, e );
        }
    }

    public static Object getFieldValue( Object bean, String fieldName )
    {
        try
        {
            java.lang.reflect.Field javaField = getDeclaredFieldWithInheritance( bean.getClass(), fieldName );
            javaField.setAccessible( true );
            return javaField.get( bean );
        }
        catch ( IllegalArgumentException e )
        {
            throw new HibersapException( e );
        }
        catch ( IllegalAccessException e )
        {
            throw new HibersapException( "Field " + bean.getClass() + "." + fieldName + " is not accessible", e );
        }
    }

    /**
     * Get the actual type of a one-dimensional generic field.
     *
     * @param field The field
     * @return The generic type or null if the field is not generic
     */
    public static Class<?> getGenericType( Field field )
    {
        Type genericType = field.getGenericType();
        return getGenericType( genericType );
    }

    private static Class<?> getGenericType( Type genericType )
    {
        if ( genericType instanceof ParameterizedType )
        {
            ParameterizedType paramType = ( ParameterizedType ) genericType;
            Type[] actualTypeArguments = paramType.getActualTypeArguments();
            if ( actualTypeArguments.length == 1 )
            {
                Type actualType = actualTypeArguments[0];
                if ( actualType != null )
                {
                    return getClass( actualType );
                }
            }
        }
        return null;
    }

    @SuppressWarnings( "unchecked" )
    public static Collection<Object> newCollectionInstance( Class<? extends Collection> clazz )
    {
        try
        {
            return clazz.newInstance();
        }
        catch ( InstantiationException e )
        {
            throw new HibersapException( MSG_CAN_NOT_CREATE_INSTANCE + clazz.getName(), e );
        }
        catch ( IllegalAccessException e )
        {
            throw new HibersapException( MSG_CAN_NOT_CREATE_INSTANCE + clazz.getName(), e );
        }
    }

    public static <T> T newInstance( Class<T> clazz )
    {
        try
        {
            Constructor<T> defaultConstructor = clazz.getDeclaredConstructor();
            defaultConstructor.setAccessible( true );
            return defaultConstructor.newInstance();
        }
        // TODO add more meaningful message to exceptions
        catch ( InstantiationException e )
        {
            throw new HibersapException( MSG_CAN_NOT_CREATE_INSTANCE + clazz.getName(), e );
        }
        catch ( IllegalAccessException e )
        {
            throw new HibersapException( MSG_CAN_NOT_CREATE_INSTANCE + clazz.getName(), e );
        }
        catch ( NoSuchMethodException e )
        {
            throw new HibersapException( "Class does not have a default constructor: " + clazz.getName(), e );
        }
        catch ( InvocationTargetException e )
        {
            throw new HibersapException( "Default constructor threw an exception: " + clazz.getName(), e );
        }
    }

    public static <T> T newInstance( String fullyQualifiedClassName, Class<T> superType )
    {
        try
        {
            final Class<? extends T> theClass = getClassForName( fullyQualifiedClassName ).asSubclass( superType );
            return newInstance( theClass );
        }
        catch ( ClassNotFoundException e )
        {
            throw new HibersapException( "Class not found in classpath: " + fullyQualifiedClassName, e );
        }
    }

    public static <T> Set<T> createInstances( Set<String> fullyQualifiedClassNames, Class<T> superType )
    {
        final HashSet<T> instances = new HashSet<T>();
        for ( String className : fullyQualifiedClassNames )
        {
            final T instance = newInstance( className, superType );
            instances.add( instance );
        }
        return instances;
    }

    public static void setFieldValue( Object bean, String fieldName, Object value )
    {
        if ( bean == null )
        {
            throw new HibersapException( "Cannot set a value on a null object" );
        }

        try
        {
            Field declaredField = getDeclaredFieldWithInheritance( bean.getClass(), fieldName );
            declaredField.setAccessible( true );
            declaredField.set( bean, value );
        }
        catch ( SecurityException e )
        {
            throw new HibersapException( "Can not assign an object of type " + getClassNameNullSafe( value )
                    + " to the field " + bean.getClass().getName() + "." + fieldName, e );
        }
        catch ( IllegalArgumentException e )
        {
            throw new HibersapException( "Can not assign an object of type " + getClassNameNullSafe( value )
                    + " to the field " + bean.getClass().getName() + "." + fieldName, e );
        }
        catch ( IllegalAccessException e )
        {
            throw new HibersapException( "Can not assign an object of type " + getClassNameNullSafe( value )
                    + " to the field " + bean.getClass().getName() + "." + fieldName, e );
        }
    }

    private static Field getDeclaredFieldWithInheritance( Class beanClass, String fieldName )
    {
        try
        {
            return beanClass.getDeclaredField( fieldName );
        }
        catch ( NoSuchFieldException e )
        {
            final Class<?> superclass = beanClass.getSuperclass();

            if ( superclass != null )
            {
                return getDeclaredFieldWithInheritance( superclass, fieldName );
            }
            else
            {
                throw new HibersapException( "Class " + beanClass.getName() + " does not have a field named "
                        + fieldName, e );
            }
        }
    }

    private static String getClassNameNullSafe( Object object )
    {
        return object == null ? "null" : object.getClass().getName();
    }


    public static Set<Field> getDeclaredFieldsWithAnnotationRecursively( Class<?> clazz,
                                                                         Class<? extends Annotation> annotationClass )
    {
        final HashSet<Field> fields = new HashSet<Field>();
        addDeclaredFieldsWithAnnotationRecursively( fields, clazz, annotationClass );
        return fields;
    }

    private static void addDeclaredFieldsWithAnnotationRecursively( HashSet<Field> fields, Class<?> clazz,
                                                                    Class<? extends Annotation> annotationClass )
    {
        final Field[] declaredFields = clazz.getDeclaredFields();
        for ( Field declaredField : declaredFields )
        {
            if ( declaredField.isAnnotationPresent( annotationClass ) )
            {
                fields.add( declaredField );
            }
        }

        final Class<?> superclass = clazz.getSuperclass();
        if ( superclass != null )
        {
            addDeclaredFieldsWithAnnotationRecursively( fields, superclass, annotationClass );
        }
    }

    private ReflectionHelper()
    {
        // should not be instantiated
    }
}
