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


import org.hibersap.interceptor.BapiInterceptor;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashSet;
import java.util.Set;

public class BeanValidationInterceptor implements BapiInterceptor {

    private final ValidatorFactory validatorFactory;

    public BeanValidationInterceptor( final ValidatorFactory validatorFactory ) {
        this.validatorFactory = validatorFactory;
    }

    public void beforeExecution( final Object bapiObject ) throws ConstraintViolationException {
        final Validator validator = validatorFactory.getValidator();

        final Set<ConstraintViolation<Object>> constraintViolations = validator.validate( bapiObject );

        if ( constraintViolations.size() > 0 ) {
            checkConstraints( constraintViolations );
        }
    }

    private void checkConstraints( final Set<ConstraintViolation<Object>> constraintViolations ) {
        Set<ConstraintViolation<?>> propagatedViolations = new HashSet<ConstraintViolation<?>>(
                constraintViolations.size() );
        Set<String> classNames = new HashSet<String>();

        for ( ConstraintViolation<?> violation : constraintViolations ) {
            propagatedViolations.add( violation );
            classNames.add( violation.getLeafBean().getClass().getName() );
        }

        String msg = "Validation failed for classes " + classNames;
        throw new ConstraintViolationException( msg, propagatedViolations );
    }

    public void afterExecution( final Object bapiObject ) {
        // check only one way
    }
}
