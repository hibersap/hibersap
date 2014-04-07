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

package org.hibersap.it.bapi;

import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.annotations.ThrowExceptionOnError;
import org.hibersap.it.AbstractBapiTest;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.fest.assertions.Assertions.assertThat;

public class MapCurrencyParametersToBigDecimalTest extends AbstractBapiTest
{
    @Test
    public void mapsCurrencyParametersToBigDecimal() throws Exception
    {
        BapiExchangeRateGetDetail bapi = new BapiExchangeRateGetDetail();

        session.execute( bapi );

        assertThat( bapi.exchangeRate.exchangeRate.toPlainString() ).startsWith( "0.511" );
        assertThat( bapi.exchangeRate.ratioFrom ).isEqualTo( new BigDecimal( 1 ) );
        assertThat( bapi.exchangeRate.ratioTo ).isEqualTo( new BigDecimal( 1 ) );
    }

    @Bapi( "BAPI_EXCHANGERATE_GETDETAIL" )
    @ThrowExceptionOnError
    @SuppressWarnings( "UnusedDeclaration" ) // fields called per reflection by Hibersap
    private static class BapiExchangeRateGetDetail
    {
        @Import
        @Parameter( "RATE_TYPE" )
        String rateType = "EURO";

        @Import
        @Parameter( "FROM_CURR" )
        String fromCurrency = "DEM";

        @Import
        @Parameter( "TO_CURRNCY" )
        String toCurrency = "EUR";

        @Import
        @Parameter( "DATE" )
        Date date = new Date();

        @Export
        @Parameter( value = "EXCH_RATE", type = ParameterType.STRUCTURE )
        ExchangeRate exchangeRate;
    }

    @BapiStructure
    private static class ExchangeRate
    {
        @Parameter( "EXCH_RATE" )
        BigDecimal exchangeRate;

        @Parameter( "FROM_FACTOR" )
        BigDecimal ratioFrom;

        @Parameter( "TO_FACTOR" )
        BigDecimal ratioTo;
    }
}
