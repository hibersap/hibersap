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

import org.hibersap.validation.BeanValidationInterceptor;
import org.junit.Test;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.fail;

public class BeanValidationInterceptorTest
{
    @Test
    public void throwsConstraintViolationExceptionWhoseMessageContainsNameOfNonValidatingClass()
    {
        final BeanValidationInterceptor<TestObject> interceptor = new BeanValidationInterceptor<TestObject>( null );

        ConstraintViolationException validationException = null;
        try
        {
            interceptor.beforeExecution( new TestObject() );
            fail();
        }
        catch ( ConstraintViolationException e )
        {
            validationException = e;
        }

        assertThat( validationException, notNullValue() );
        assertThat( validationException.getMessage(),
                containsString( "org.hibersap.interceptor.impl.BeanValidationInterceptorTest$InnerObject" ) );
    }

    @SuppressWarnings( "unused" )
    private static class TestObject
    {
        @Valid
        InnerObject innerObject = new InnerObject();
    }

    @SuppressWarnings( "unused" )
    private static class InnerObject
    {
        @NotNull
        String str = null;
    }
}
