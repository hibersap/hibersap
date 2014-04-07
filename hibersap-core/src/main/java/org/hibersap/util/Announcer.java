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

package org.hibersap.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Handles Listener logic.
 * <p/>
 * Stolen and modified from the examples of the book "Growing Object-Oriented Software, Guided by Tests" by Freeman/Pryce.
 * See https://github.com/sf105/goos-code.
 *
 * @param <T> The Listener's type
 */
public class Announcer<T>
{
    private final T proxy;
    private final List<T> listeners = new ArrayList<T>();


    private Announcer( Class<? extends T> listenerType )
    {
        proxy = listenerType.cast( Proxy.newProxyInstance(
                listenerType.getClassLoader(),
                new Class<?>[]{listenerType},
                new InvocationHandler()
                {
                    public Object invoke( Object aProxy, Method method, Object[] args ) throws Throwable
                    {
                        announce( method, args );
                        return null;
                    }
                } ) );
    }

    public void addListener( T listener )
    {
        listeners.add( listener );
    }

    public void addAllListeners( Collection<T> listeners )
    {
        this.listeners.addAll( listeners );
    }

    public void removeListener( T listener )
    {
        listeners.remove( listener );
    }

    public T announce()
    {
        return proxy;
    }

    private void announce( Method m, Object[] args )
    {
        try
        {
            for ( T listener : listeners )
            {
                m.invoke( listener, args );
            }
        }
        catch ( IllegalAccessException e )
        {
            throw new IllegalArgumentException( "could not invoke listener", e );
        }
        catch ( InvocationTargetException e )
        {
            Throwable cause = e.getCause();

            if ( cause instanceof RuntimeException )
            {
                throw ( RuntimeException ) cause;
            }
            else if ( cause instanceof Error )
            {
                throw ( Error ) cause;
            }
            else
            {
                throw new UnsupportedOperationException( "listener threw exception", cause );
            }
        }
    }

    public static <T> Announcer<T> to( Class<? extends T> listenerType )
    {
        return new Announcer<T>( listenerType );
    }
}