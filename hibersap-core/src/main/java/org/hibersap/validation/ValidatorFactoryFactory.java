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

import javax.validation.ValidatorFactory;

/**
 * Wrapper for building the Bean Validation's ValidatorFactory. Bean Validation API uses a static method call
 * to Validator.buildDefaultValidatorFactory(), which makes it impossible to test.
 */
interface ValidatorFactoryFactory
{
    /**
     * Builds the Bean Validation's ValidatorFactory.
     *
     * @return The ValidatorFactory
     * @throws ValidationException If the ValidatorFactory cannot be built.
     */
    ValidatorFactory buildValidatorFactory();
}
