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
public class Announcer<T> {

    private final T proxy;
    private final List<T> listeners = new ArrayList<T>();

    private Announcer(final Class<? extends T> listenerType) {
        proxy = listenerType.cast(Proxy.newProxyInstance(
                listenerType.getClassLoader(),
                new Class<?>[]{listenerType},
                new InvocationHandler() {
                    public Object invoke(Object aProxy, Method method, Object[] args) throws Throwable {
                        announce(method, args);
                        return null;
                    }
                }
        ));
    }

    public static <T> Announcer<T> to(final Class<? extends T> listenerType) {
        return new Announcer<T>(listenerType);
    }

    public void addListener(final T listener) {
        listeners.add(listener);
    }

    public void addAllListeners(final Collection<T> listeners) {
        this.listeners.addAll(listeners);
    }

    public void removeListener(final T listener) {
        listeners.remove(listener);
    }

    public T announce() {
        return proxy;
    }

    private void announce(final Method m, final Object[] args) {
        try {
            for (T listener : listeners) {
                m.invoke(listener, args);
            }
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("could not invoke listener", e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();

            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else if (cause instanceof Error) {
                throw (Error) cause;
            } else {
                throw new UnsupportedOperationException("listener threw exception", cause);
            }
        }
    }
}