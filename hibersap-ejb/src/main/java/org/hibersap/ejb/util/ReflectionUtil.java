/*
 * Copyright (c) 2008-2014 akquinet tech@spree GmbH
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

package org.hibersap.ejb.util;

import org.hibersap.InternalHiberSapException;
import org.hibersap.ejb.interceptor.HibersapSession;
import org.hibersap.session.Session;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;

public final class ReflectionUtil {

    private ReflectionUtil() {
        // utility class with static methods
    }

    public static Set<Field> getHibersapSessionFields( final Object target ) {
        HashSet<Field> fields = new HashSet<Field>();

        Field[] declaredFields = target.getClass().getDeclaredFields();
        for ( Field declaredField : declaredFields ) {
            if ( declaredField.getAnnotation( HibersapSession.class ) != null ) {
                fields.add( declaredField );
            }
        }

        return fields;
    }

    public static String getSessionManagerJndiName( final Field sessionField ) {
        HibersapSession annotation = sessionField.getAnnotation( HibersapSession.class );

        if ( annotation == null ) {
            throw new InternalHiberSapException( format( "The field %s.%s is not annotated with @%s",
                                                         sessionField.getDeclaringClass().getName(),
                                                         sessionField.getName(),
                                                         HibersapSession.class.getSimpleName() ) );
        }

        return annotation.value();
    }

    public static void injectSessionIntoTarget( final Object target, final Field field, final Session session ) {
        if ( !Session.class.isAssignableFrom( field.getType() ) ) {
            throw new InternalHiberSapException(
                    format( "Annotation @%s can only be used on a field of type or subtype of %s. "
                                    + "Can not set %s field %s.%s to %s",
                            HibersapSession.class.getSimpleName(),
                            Session.class,
                            field.getType(),
                            target.getClass().getName(),
                            field.getName(),
                            session
                    )
            );
        }

        boolean accessible = field.isAccessible();

        synchronized ( field ) {
            field.setAccessible( true );
            try {
                field.set( target, session );
            } catch ( IllegalAccessException e ) {
                throw new InternalHiberSapException(
                        format( "Error injecting Hibersap Session %s into %s.%s",
                                session,
                                target.getClass().getName(),
                                field.getName() ), e
                );
            } finally {
                field.setAccessible( accessible );
            }
        }
    }
}
