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

package org.hibersap.interceptor.impl;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import org.hibersap.validation.BeanValidationInterceptor;
import org.junit.Test;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

public class BeanValidationInterceptorTest {

    @Test
    public void throwsConstraintViolationExceptionWhoseMessageContainsNameOfNonValidatingClass() {
        final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        final BeanValidationInterceptor interceptor = new BeanValidationInterceptor( validatorFactory );

        ConstraintViolationException validationException = null;
        try {
            interceptor.beforeExecution( new TestObject() );
            fail();
        } catch ( ConstraintViolationException e ) {
            validationException = e;
        }

        assertThat( validationException ).isNotNull();
        assertThat( validationException.getMessage() ).contains(
                "org.hibersap.interceptor.impl.BeanValidationInterceptorTest$InnerObject" );
    }

    @SuppressWarnings( "unused" )
    private static class TestObject {

        @Valid
        InnerObject innerObject = new InnerObject();
    }

    @SuppressWarnings( "unused" )
    private static class InnerObject {

        @NotNull
        String str = null;
    }
}
