/*
 * Copyright (c) 2009, 2011 akquinet tech@spree GmbH.
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

import org.hibersap.HibersapException;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.configuration.xml.ValidationMode;
import org.hibersap.interceptor.BapiInterceptor;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ValidationException;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;

public class TypeSafeActivatorTest
{
    @Before
    public void setDefaultValdiationFactoryFactory() throws Exception
    {
        useValidationFactoryFactory( new DefaultValidatorFactoryFactory() );
    }

    @Test
    public void doesNotAddInterceptorWhenValidatorFactoryCannotBeBuildAndCalledWithValidationModeAuto() throws Exception
    {
        Set<BapiInterceptor> interceptors = new HashSet<BapiInterceptor>();
        final SessionManagerConfig config = new SessionManagerConfig();

        useValidationFactoryFactory( new ExceptionThrowingValidatorFactoryFactory() );

        TypeSafeActivator.activateBeanValidation( interceptors, config );

        assertThat( interceptors, hasSize( 0 ) );
    }

    @Test
    public void addsBeanValidationInterceptorWithDefaultValidationMode()
    {
        Set<BapiInterceptor> interceptors = new HashSet<BapiInterceptor>();
        final SessionManagerConfig config = new SessionManagerConfig();

        TypeSafeActivator.activateBeanValidation( interceptors, config );

        assertThat( interceptors, hasSize( 1 ) );
        assertThat( interceptors.iterator().next(), instanceOf( BeanValidationInterceptor.class ) );
    }

    @Test( expected = HibersapException.class )
    public void throwsExceptionWhenValidatorFactoryCannotBeBuiltAndIsCalledWithValidationModeCallback() throws Exception
    {
        Set<BapiInterceptor> interceptors = new HashSet<BapiInterceptor>();
        final SessionManagerConfig config = new SessionManagerConfig().setValidationMode( ValidationMode.CALLBACK );

        useValidationFactoryFactory( new ExceptionThrowingValidatorFactoryFactory() );

        TypeSafeActivator.activateBeanValidation( interceptors, config );
    }

    private void useValidationFactoryFactory( ValidatorFactoryFactory factory ) throws Exception
    {
        Field declaredField = TypeSafeActivator.class.getDeclaredField( "validatorFactoryFactory" );
        declaredField.setAccessible( true );
        declaredField.set( null, factory );
    }

    private static class ExceptionThrowingValidatorFactoryFactory implements ValidatorFactoryFactory
    {
        public ValidatorFactory buildValidatorFactory()
        {
            throw new ValidationException( "test" );
        }
    }
}
