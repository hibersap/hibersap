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
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.Table;
import org.hibersap.annotations.ThrowExceptionOnError;
import org.hibersap.it.AbstractBapiTest;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class MapTableParameterToArrayTest extends AbstractBapiTest {

    @Test
    public void mapsTableParameterToArray() {
        BapiCustomerGetList bapi = new BapiCustomerGetList();
        session.execute(bapi);

        assertThat(bapi.customerList).hasSize(2);
    }

    @Bapi("BAPI_FLCUST_GETLIST")
    @ThrowExceptionOnError(returnStructure = "TABLE/RETURN")
    private static class BapiCustomerGetList {

        @Import
        @Parameter("MAX_ROWS")
        int maxRows = 2;

        @Table
        @Parameter("CUSTOMER_LIST")
        CustomerList[] customerList;
    }

    @BapiStructure
    private static class CustomerList {

    }
}
