/*
 * Copyright (c) 2008-2019 akquinet tech@spree GmbH
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

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.interceptor.BapiInterceptor;
import org.junit.Before;
import org.junit.Test;
import static org.fest.assertions.Assertions.assertThat;

public class TypeSafeActivatorTest {

    @Before
    public void setDefaultValdiationFactoryFactory() throws Exception {
        useValidationFactoryFactory(new DefaultValidatorFactoryFactory());
    }

    @Test
    public void hibernateValidationIsInClasspath() throws ClassNotFoundException {
        Class.forName("org.hibernate.validator.HibernateValidator");
    }

    @Test
    public void addsBeanValidationInterceptorWithDefaultValidationMode() {
        Set<BapiInterceptor> interceptors = new HashSet<BapiInterceptor>();
        final SessionManagerConfig config = new SessionManagerConfig();

        TypeSafeActivator.activateBeanValidation(interceptors, config);

        assertThat(interceptors).hasSize(1);
        assertThat(interceptors.toArray()).hasAtLeastOneElementOfType(BeanValidationInterceptor.class);
    }

    private void useValidationFactoryFactory(ValidatorFactoryFactory factory) throws Exception {
        Field declaredField = TypeSafeActivator.class.getDeclaredField("validatorFactoryFactory");
        declaredField.setAccessible(true);
        declaredField.set(null, factory);
    }
}
