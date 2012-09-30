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
