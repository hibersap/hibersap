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


import org.hibersap.interceptor.BapiInterceptor;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashSet;
import java.util.Set;

public class BeanValidationInterceptor implements BapiInterceptor
{
    private final ValidatorFactory validatorFactory;

    public BeanValidationInterceptor( ValidatorFactory validatorFactory )
    {
        this.validatorFactory = validatorFactory;
    }

    public void beforeExecution( Object bapiObject ) throws ConstraintViolationException
    {
        final Validator validator = validatorFactory.getValidator();

        final Set<ConstraintViolation<Object>> constraintViolations = validator.validate( bapiObject );

        if ( constraintViolations.size() > 0 )
        {
            checkConstraints( constraintViolations );
        }
    }

    private void checkConstraints( Set<ConstraintViolation<Object>> constraintViolations )
    {
        Set<ConstraintViolation<?>> propagatedViolations = new HashSet<ConstraintViolation<?>>(
                constraintViolations.size() );
        Set<String> classNames = new HashSet<String>();

        for ( ConstraintViolation<?> violation : constraintViolations )
        {
            propagatedViolations.add( violation );
            classNames.add( violation.getLeafBean().getClass().getName() );
        }

        String msg = "Validation failed for classes " + classNames;
        throw new ConstraintViolationException( msg, propagatedViolations );
    }

    public void afterExecution( Object bapiObject )
    {
        // check only one way
    }
}
