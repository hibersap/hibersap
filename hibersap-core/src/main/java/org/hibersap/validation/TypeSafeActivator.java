/*
 * Copyright (c) 2008-2025 tech@spree GmbH
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

import java.util.Set;
import javax.validation.ValidationException;
import javax.validation.ValidatorFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.HibersapException;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.configuration.xml.ValidationMode;
import org.hibersap.interceptor.BapiInterceptor;

class TypeSafeActivator {

    private static final Log LOGGER = LogFactory.getLog(TypeSafeActivator.class);

    private static ValidatorFactoryFactory validatorFactoryFactory = new DefaultValidatorFactoryFactory();

    private TypeSafeActivator() {
        // use static methods
    }

    @SuppressWarnings("unused") // called by reflection
    static void activateBeanValidation(final Set<BapiInterceptor> bapiInterceptors,
                                       final SessionManagerConfig sessionManagerConfig) {
        try {
            ValidatorFactory factory = validatorFactoryFactory.buildValidatorFactory();
            bapiInterceptors.add(new BeanValidationInterceptor(factory));
        } catch (ValidationException e) {
            ValidationMode validationMode = sessionManagerConfig.getValidationMode();
            if (validationMode == ValidationMode.AUTO) {
                LOGGER.warn("Bean Validation will not be used: Bean Validation API is in the classpath, " +
                        "but default ValidatorFactory can not be built. " +
                        "ValidationMode is AUTO, so startup will be continued.", e);
            } else {
                throw new HibersapException("Unable to build the default ValidatorFactory, ValidationMode is " +
                        validationMode, e);
            }
        }
    }
}
