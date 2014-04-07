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

package org.hibersap.execution.jca.cci;

import org.hibersap.InternalHiberSapException;
import org.hibersap.mapping.ReflectionHelper;

import javax.resource.cci.ConnectionSpec;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Offers methods to help accessing and instantiating the ConnectionSpec implementation. May be used
 * to implement a new ConnectionSpecFactory.
 *
 * @author Carsten Erker
 */
public abstract class AbstractConnectionSpecFactory
        implements ConnectionSpecFactory
{

    /**
     * Looks up and returns the specified class. First, it will try to get the class from the
     * context class loader, if not found, it will try to get it from the current class loader.
     *
     * @param connectionSpecClass The fully qualified class name of the ConnectionSpec
     *                            implementation.
     * @return The Class object of the ConnectionSpec implementation
     * @throws ClassNotFoundException   - if the class could not be found.
     * @throws IllegalArgumentException - if the class does not implement
     *                                  javax.resource.cci.ConnectionSpec.
     */
    protected Class<? extends ConnectionSpec> getConnectionSpecClass( String connectionSpecClass )
            throws ClassNotFoundException, IllegalArgumentException
    {
        Class<?> connSpecClass = ReflectionHelper.getClassForName( connectionSpecClass );

        if ( !ConnectionSpec.class.isAssignableFrom( connSpecClass ) )
        {
            throw new IllegalArgumentException(
                    connSpecClass.getName() + " does not implement " + ConnectionSpec.class );
        }

        //noinspection unchecked
        return ( Class<? extends ConnectionSpec> ) connSpecClass;
    }

    /**
     * Creates a new instance of the specified ConnectionSpec implementation class via reflection.
     *
     * @param connectionSpecClass       The class to instantiate. Must implement
     *                                  javax.resource.cci.ConnectionSpec.
     * @param constructorParameterTypes The types of the constructor's parameters. Used to determine
     *                                  which constructor should be used.
     * @param constructorArguments      The constructor's arguments used when creating a new instance of
     *                                  the class. Must match the constructorParameterTypes.
     * @return The instance of the ConnectionSpec implementation
     * @throws InternalHiberSapException - if the class could not be accessed or instantiated,
     *                                   see cause for the actual reason.
     */
    protected ConnectionSpec newConnectionSpecInstance( Class<?> connectionSpecClass,
                                                        Class<?>[] constructorParameterTypes,
                                                        Object[] constructorArguments )
            throws InternalHiberSapException
    {
        Object connSpecImpl = null;
        try
        {
            Constructor<?> constructor = connectionSpecClass.getConstructor( constructorParameterTypes );
            connSpecImpl = constructor.newInstance( constructorArguments );
        }
        catch ( SecurityException e )
        {
            final String msg = "Can not access declared constructor: ";
            throwInternalHibersapException( connectionSpecClass, constructorParameterTypes, e, msg );
        }
        catch ( NoSuchMethodException e )
        {
            final String msg = "No such constructor: ";
            return throwInternalHibersapException( connectionSpecClass, constructorParameterTypes, e, msg );
        }
        catch ( IllegalArgumentException e )
        {
            throw new InternalHiberSapException( "No such constructor: "
                    + getSignature( connectionSpecClass, constructorArguments ), e );
        }
        catch ( InstantiationException e )
        {
            final String msg = "Cannot create new instance of: ";
            throwInternalHibersapException( connectionSpecClass, constructorParameterTypes, e, msg );
        }
        catch ( IllegalAccessException e )
        {
            final String msg = "Cannot create new instance of: ";
            throwInternalHibersapException( connectionSpecClass, constructorParameterTypes, e, msg );
        }
        catch ( InvocationTargetException e )
        {
            final String msg = "Constructor threw an Exception: ";
            throwInternalHibersapException( connectionSpecClass, constructorParameterTypes, e, msg );
        }

        if ( !ConnectionSpec.class.isAssignableFrom( connSpecImpl.getClass() ) )
        {
            throw new InternalHiberSapException( connectionSpecClass + " does not implement " + ConnectionSpec.class );
        }
        return ( ConnectionSpec ) connSpecImpl;
    }

    private ConnectionSpec throwInternalHibersapException( Class<?> connectionSpecClass,
                                                           Class<?>[] constructorParameterTypes, Exception e,
                                                           String msg )
    {
        throw new InternalHiberSapException( msg
                + getSignature( connectionSpecClass, constructorParameterTypes ), e );
    }

    private String getSignature( Class<?> clazz, Object[] arguments )
    {
        Class<?>[] classes = new Class<?>[arguments.length];

        for ( int i = 0; i < arguments.length; i++ )
        {
            Class<?> argClass = arguments[i] == null ? null : arguments[i].getClass();
            classes[i] = argClass;
        }

        return getSignature( clazz, classes );
    }

    private String getSignature( Class<?> clazz, Class<?>[] argClasses )
    {
        StringBuilder sb = new StringBuilder( clazz.getName() );
        sb.append( '(' );

        for ( int i = 0; i < argClasses.length; i++ )
        {
            if ( i > 0 )
            {
                sb.append( "," );
            }
            Class<?> argClass = argClasses[i];
            sb.append( argClass == null ? "null" : argClass.getName() );
        }

        sb.append( ')' );
        return sb.toString();
    }
}