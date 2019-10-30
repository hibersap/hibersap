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
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.it.AbstractBapiTest;
import org.junit.Test;
import static org.fest.assertions.Assertions.assertThat;

public class MapsRawTypeParameterToByteArrayTest extends AbstractBapiTest {

    @Test
    public void handlesParameterOfRawType() throws Exception {
        StfcDeepStructure bapi = new StfcDeepStructure("Ein anderer Text".getBytes());
        session.execute(bapi);

        String rawParamAsString = new String(bapi.out.rawStringParam);
        assertThat(rawParamAsString).isEqualTo("Ein anderer Text");
    }

    @Bapi("STFC_DEEP_STRUCTURE")
    private static class StfcDeepStructure {

        @Import
        @Parameter(value = "IMPORTSTRUCT", type = ParameterType.STRUCTURE)
        ComplexVar in;

        @Export
        @Parameter(value = "ECHOSTRUCT", type = ParameterType.STRUCTURE)
        ComplexVar out;

        @SuppressWarnings({"UnusedDeclaration"}) // for Hibersap
        private StfcDeepStructure() {
        }

        public StfcDeepStructure(byte[] rawStringParam) {
            in = new ComplexVar(rawStringParam);
        }
    }

    @BapiStructure
    private static class ComplexVar {

        @Parameter("XSTR")
        byte[] rawStringParam;

        @SuppressWarnings({"UnusedDeclaration"}) // for Hibersap
        private ComplexVar() {
        }

        private ComplexVar(byte[] rawStringParam) {
            this.rawStringParam = rawStringParam;
        }
    }
}
