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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.HibersapException;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.configuration.xml.ValidationMode;
import org.hibersap.interceptor.BapiInterceptor;

import javax.validation.ValidationException;
import javax.validation.ValidatorFactory;
import java.util.Set;

class TypeSafeActivator
{
    private static final Log LOGGER = LogFactory.getLog( TypeSafeActivator.class );

    private static ValidatorFactoryFactory validatorFactoryFactory = new DefaultValidatorFactoryFactory();

    @SuppressWarnings( "unused" ) // called by reflection
    static void activateBeanValidation( Set<BapiInterceptor> bapiInterceptors,
                                        SessionManagerConfig sessionManagerConfig )
    {
        try
        {
            ValidatorFactory factory = validatorFactoryFactory.buildValidatorFactory();
            bapiInterceptors.add( new BeanValidationInterceptor( factory ) );
        }
        catch ( ValidationException e )
        {
            ValidationMode validationMode = sessionManagerConfig.getValidationMode();
            if ( validationMode == ValidationMode.AUTO )
            {
                LOGGER.warn( "Bean Validation will not be used: Bean Validation API is in the classpath, " +
                        "but default ValidatorFactory can not be built. " +
                        "ValidationMode is AUTO, so startup will be continued.", e );
            }
            else
            {
                throw new HibersapException( "Unable to build the default ValidatorFactory, ValidationMode is " +
                        validationMode, e );
            }
        }
    }
}
