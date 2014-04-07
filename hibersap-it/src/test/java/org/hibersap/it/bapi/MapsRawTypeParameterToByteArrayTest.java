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
import org.hibersap.it.AbstractBapiTest;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class MapsRawTypeParameterToByteArrayTest extends AbstractBapiTest
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
