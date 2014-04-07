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
import org.hibersap.configuration.xml.ValidationMode;
import org.hibersap.interceptor.BapiInterceptor;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

public class BeanValidationActivatorTest
{
    @Test
    public void doesNotAddInterceptorWhenValidationModeNoneIsConfigured()
    {
        Set<BapiInterceptor> interceptors = new HashSet<BapiInterceptor>();
        final SessionManagerConfig config = new SessionManagerConfig().setValidationMode( ValidationMode.NONE );

        BeanValidationActivator.activateBeanValidation( interceptors, config );

        assertThat( interceptors ).hasSize( 0 );
    }

    @Test
    public void addsBeanValidationInterceptorWhenValidationTypeAutoIsConfigured()
    {
        Set<BapiInterceptor> interceptors = new HashSet<BapiInterceptor>();
        final SessionManagerConfig config = new SessionManagerConfig().setValidationMode( ValidationMode.AUTO );

        BeanValidationActivator.activateBeanValidation( interceptors, config );

        assertThat( interceptors ).hasSize( 1 );
        assertThat( interceptors.iterator().next() ).isInstanceOf( BeanValidationInterceptor.class );
    }
}
