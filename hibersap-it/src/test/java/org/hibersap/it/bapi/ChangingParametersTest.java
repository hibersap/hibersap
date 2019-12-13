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

package org.hibersap.it.bapi;

import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Changing;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.it.AbstractBapiTest;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ChangingParametersTest extends AbstractBapiTest {

    @Test
    public void mapsChangingParameter() {
        StfcChanging bapi = new StfcChanging(4711, 2);

        session.execute(bapi);

        // returns result = startValue + counter and increments counter
        assertThat(bapi.result).isEqualTo(4713);
        assertThat(bapi.counter).isEqualTo(3);
    }

    @Bapi("STFC_CHANGING")
    public static class StfcChanging {

        @Import
        @Parameter("START_VALUE")
        private int startValue;

        @Export
        @Parameter("RESULT")
        private int result;

        @Changing
        @Parameter("COUNTER")
        private int counter;

        StfcChanging(int startValue, int counter) {
            this.startValue = startValue;
            this.counter = counter;
        }
    }
}
