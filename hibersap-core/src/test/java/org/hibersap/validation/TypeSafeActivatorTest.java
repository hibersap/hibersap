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

package org.hibersap.validation;

import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.interceptor.BapiInterceptor;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

public class TypeSafeActivatorTest
{
    @Before
    public void setDefaultValdiationFactoryFactory() throws Exception
    {
        useValidationFactoryFactory( new DefaultValidatorFactoryFactory() );
    }

    @Test
    public void hibernateValidationIsInClasspath() throws ClassNotFoundException
    {
        Class.forName( "org.hibernate.validator.HibernateValidator" );
    }

    @Test
    public void addsBeanValidationInterceptorWithDefaultValidationMode()
    {
        Set<BapiInterceptor> interceptors = new HashSet<BapiInterceptor>();
        final SessionManagerConfig config = new SessionManagerConfig();

        TypeSafeActivator.activateBeanValidation( interceptors, config );

        assertThat( interceptors ).hasSize( 1 );
        assertThat( interceptors.toArray() ).hasAtLeastOneElementOfType( BeanValidationInterceptor.class );
    }

    private void useValidationFactoryFactory( ValidatorFactoryFactory factory ) throws Exception
    {
        Field declaredField = TypeSafeActivator.class.getDeclaredField( "validatorFactoryFactory" );
        declaredField.setAccessible( true );
        declaredField.set( null, factory );
    }
}
