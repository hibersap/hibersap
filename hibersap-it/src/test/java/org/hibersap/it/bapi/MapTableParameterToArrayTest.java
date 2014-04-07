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
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.Table;
import org.hibersap.annotations.ThrowExceptionOnError;
import org.hibersap.it.AbstractBapiTest;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class MapTableParameterToArrayTest extends AbstractBapiTest
{
    @Test
    public void mapsTableParameterToArray() throws Exception
    {
        BapiCustomerGetList bapi = new BapiCustomerGetList();
        session.execute( bapi );

        assertThat( bapi.customerList ).hasSize( 2 );
    }

    @Bapi( "BAPI_FLCUST_GETLIST" )
    @ThrowExceptionOnError( returnStructure = "TABLE/RETURN" )
    private static class BapiCustomerGetList
    {
        @Import
        @Parameter( "MAX_ROWS" )
        int maxRows = 2;

        @Table
        @Parameter( "CUSTOMER_LIST" )
        CustomerList[] customerList;
    }

    @BapiStructure
    private static class CustomerList
    {
    }
}
