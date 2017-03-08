/*
 * Copyright (c) 2008-2017 akquinet tech@spree GmbH
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

import java.math.BigDecimal;
import java.util.Date;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.annotations.ThrowExceptionOnError;
import org.hibersap.it.AbstractBapiTest;
import org.junit.Test;
import static org.fest.assertions.Assertions.assertThat;

public class MapCurrencyParametersToBigDecimalTest extends AbstractBapiTest {

    @Test
    public void mapsCurrencyParametersToBigDecimal() throws Exception {
        BapiExchangeRateGetDetail bapi = new BapiExchangeRateGetDetail();

        session.execute( bapi );

        assertThat( bapi.exchangeRate.exchangeRate.toPlainString() ).startsWith( "0.511" );
        assertThat( bapi.exchangeRate.ratioFrom ).isEqualTo( new BigDecimal( 1 ) );
        assertThat( bapi.exchangeRate.ratioTo ).isEqualTo( new BigDecimal( 1 ) );
    }

    @Bapi("BAPI_EXCHANGERATE_GETDETAIL")
    @ThrowExceptionOnError
    @SuppressWarnings("UnusedDeclaration") // fields called per reflection by Hibersap
    private static class BapiExchangeRateGetDetail {

        @Import
        @Parameter("RATE_TYPE")
        String rateType = "EURO";

        @Import
        @Parameter("FROM_CURR")
        String fromCurrency = "DEM";

        @Import
        @Parameter("TO_CURRNCY")
        String toCurrency = "EUR";

        @Import
        @Parameter("DATE")
        Date date = new Date();

        @Export
        @Parameter(value = "EXCH_RATE", type = ParameterType.STRUCTURE)
        ExchangeRate exchangeRate;
    }

    @BapiStructure
    private static class ExchangeRate {

        @Parameter("EXCH_RATE")
        BigDecimal exchangeRate;

        @Parameter("FROM_FACTOR")
        BigDecimal ratioFrom;

        @Parameter("TO_FACTOR")
        BigDecimal ratioTo;
    }
}
