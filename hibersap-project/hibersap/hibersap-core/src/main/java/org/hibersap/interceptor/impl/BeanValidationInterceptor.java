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

package org.hibersap.interceptor.impl;


import org.hibersap.interceptor.BapiInterceptor;

import javax.validation.Validation;
import javax.validation.Validator;

public class BeanValidationInterceptor implements BapiInterceptor
{
    public void beforeExecution( Object bapiObject )
    {
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        validator.validate( bapiObject );
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void afterExecution( Object bapiObject )
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
