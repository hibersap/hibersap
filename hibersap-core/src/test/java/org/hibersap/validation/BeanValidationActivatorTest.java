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

package org.hibersap.validation;

import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.configuration.xml.ValidationMode;
import org.hibersap.interceptor.BapiInterceptor;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

public class BeanValidationActivatorTest {

    @Test
    public void doesNotAddInterceptorWhenValidationModeNoneIsConfigured() {
        Set<BapiInterceptor> interceptors = new HashSet<BapiInterceptor>();
        final SessionManagerConfig config = new SessionManagerConfig().setValidationMode( ValidationMode.NONE );

        BeanValidationActivator.activateBeanValidation( interceptors, config );

        assertThat( interceptors ).hasSize( 0 );
    }

    @Test
    public void addsBeanValidationInterceptorWhenValidationTypeAutoIsConfigured() {
        Set<BapiInterceptor> interceptors = new HashSet<BapiInterceptor>();
        final SessionManagerConfig config = new SessionManagerConfig().setValidationMode( ValidationMode.AUTO );

        BeanValidationActivator.activateBeanValidation( interceptors, config );

        assertThat( interceptors ).hasSize( 1 );
        assertThat( interceptors.iterator().next() ).isInstanceOf( BeanValidationInterceptor.class );
    }
}
