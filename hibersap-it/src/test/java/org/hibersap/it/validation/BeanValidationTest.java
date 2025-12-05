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

package org.hibersap.it.validation;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Changing;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.it.AbstractBapiTest;
import org.hibersap.it.bapi.MapsRawTypeParameterToByteArrayTest.StfcDeepStructure;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BeanValidationTest extends AbstractBapiTest {

    @Test
    public void validationFailsWhenStartValueIsNegative() {
        StfcChangingWithBeanValidation bapi = new StfcChangingWithBeanValidation(-1, 2);

        assertThatThrownBy(() -> session.execute(bapi))
                .isInstanceOf(ConstraintViolationException.class)
                .extracting("constraintViolations").asString().contains("startValue", "must be greater than or equal to 0");
    }

    @Test
    public void validationFailsWhenCounterIsGreaterThan100() {
        StfcChangingWithBeanValidation bapi = new StfcChangingWithBeanValidation(1, 101);

        assertThatThrownBy(() -> session.execute(bapi))
                .isInstanceOf(ConstraintViolationException.class)
                .extracting("constraintViolations").asString().contains("counter", "must be less than or equal to 100");
    }

    @Test
    public void validationIsNotFailsWhenResultIsNegative() {
        StfcChangingWithBeanValidation bapi = new StfcChangingWithBeanValidation(1, -3);

        session.execute(bapi);

        assertThatThrownBy(() -> session.execute(bapi))
                .isInstanceOf(ConstraintViolationException.class)
                .extracting("constraintViolations").asString().contains("result", "must be greater than or equal to 0");
    }

    @Test
    public void validatesComplexTypes() {
        StfcDeepStructure bapi = new StfcDeepStructure(new byte[0]);

        assertThatThrownBy(() -> session.execute(bapi))
                .isInstanceOf(ConstraintViolationException.class)
                .extracting("constraintViolations").asString().contains("in.rawStringParam", "must not be empty");
    }

    @Bapi("STFC_CHANGING")
    public static class StfcChangingWithBeanValidation {

        @Import
        @Parameter("START_VALUE")
        @Min(0)
        private int startValue;

        @Export
        @Parameter("RESULT")
        @Min(0)
        private int result;

        @Changing
        @Parameter("COUNTER")
        @Max(100)
        private int counter;

        StfcChangingWithBeanValidation(int startValue, int counter) {
            this.startValue = startValue;
            this.counter = counter;
        }
    }
}
