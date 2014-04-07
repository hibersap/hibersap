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

package org.hibersap.ejb.util;

import org.hibersap.InternalHiberSapException;
import org.hibersap.ejb.interceptor.HibersapSession;
import org.hibersap.session.Session;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;

public class ReflectionUtil
{
    private ReflectionUtil()
    {
        // utility class with static methods
    }

    public static Set<Field> getHibersapSessionFields( final Object target )
    {
        HashSet<Field> fields = new HashSet<Field>();

        Field[] declaredFields = target.getClass().getDeclaredFields();
        for ( Field declaredField : declaredFields )
        {
            if ( declaredField.getAnnotation( HibersapSession.class ) != null )
            {
                fields.add( declaredField );
            }
        }

        return fields;
    }

    public static String getSessionManagerJndiName( final Field sessionField )
    {
        HibersapSession annotation = sessionField.getAnnotation( HibersapSession.class );

        if ( annotation == null )
        {
            throw new InternalHiberSapException( format( "The field %s.%s is not annotated with @%s",
                    sessionField.getDeclaringClass().getName(),
                    sessionField.getName(),
                    HibersapSession.class.getSimpleName() ) );
        }

        return annotation.value();
    }

    public static void injectSessionIntoTarget( final Object target, final Field field, final Session session )
    {
        if ( !Session.class.isAssignableFrom( field.getType() ) )
        {
            throw new InternalHiberSapException(
                    format( "Annotation @%s can only be used on a field of type or subtype of %s. "
                            + "Can not set %s field %s.%s to %s",
                            HibersapSession.class.getSimpleName(),
                            Session.class,
                            field.getType(),
                            target.getClass().getName(),
                            field.getName(),
                            session ) );
        }

        boolean accessible = field.isAccessible();

        synchronized ( field )
        {
            field.setAccessible( true );
            try
            {
                field.set( target, session );
            }
            catch ( IllegalAccessException e )
            {
                throw new InternalHiberSapException(
                        format( "Error injecting Hibersap Session %s into %s.%s",
                                session,
                                target.getClass().getName(),
                                field.getName() ), e );
            }
            finally
            {
                field.setAccessible( accessible );
            }
        }
    }
}
