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

public class BapiWithRawTypeParameterTest extends AbstractBapiTest
{
    @Test
    public void handlesParameterOfRawType() throws Exception
    {
        StfcDeepStructure bapi = new StfcDeepStructure( "Ein anderer Text".getBytes() );
        session.execute( bapi );

        String rawParamAsString = new String( bapi.out.rawStringParam );
        assertThat( rawParamAsString ).isEqualTo( "Ein anderer Text" );
    }

    @Bapi( "STFC_DEEP_STRUCTURE" )
    private static class StfcDeepStructure
    {
        @Import
        @Parameter( value = "IMPORTSTRUCT", type = ParameterType.STRUCTURE )
        ComplexVar in;

        @Export
        @Parameter( value = "ECHOSTRUCT", type = ParameterType.STRUCTURE )
        ComplexVar out;

        @SuppressWarnings( {"UnusedDeclaration"} ) // for Hibersap
        private StfcDeepStructure()
        {
        }

        public StfcDeepStructure( byte[] rawStringParam )
        {
            in = new ComplexVar( rawStringParam );
        }
    }

    @BapiStructure
    private static class ComplexVar
    {
        @Parameter( "XSTR" )
        byte[] rawStringParam;

        @SuppressWarnings( {"UnusedDeclaration"} ) // for Hibersap
        private ComplexVar()
        {
        }

        private ComplexVar( byte[] rawStringParam )
        {
            this.rawStringParam = rawStringParam;
        }
    }
}
