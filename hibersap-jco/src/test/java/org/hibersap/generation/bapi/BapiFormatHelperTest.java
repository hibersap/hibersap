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

package org.hibersap.generation.bapi;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BapiFormatHelperTest
{
    @Test
    public void getCamelCaseSmall()
        throws Exception
    {
        assertEquals( "_", BapiFormatHelper.getCamelCaseSmall( null ) );
        assertEquals( "_", BapiFormatHelper.getCamelCaseSmall( "" ) );
        assertEquals( "_x", BapiFormatHelper.getCamelCaseSmall( "X" ) );
        assertEquals( "_xY", BapiFormatHelper.getCamelCaseSmall( "X_Y" ) );
        assertEquals( "_xYZ", BapiFormatHelper.getCamelCaseSmall( "X_Y_Z" ) );
        assertEquals( "_myLittleField", BapiFormatHelper.getCamelCaseSmall( "MY_LITTLE_FIELD" ) );
    }

    @Test
    public void getCamelCaseBig()
        throws Exception
    {
        assertEquals( "", BapiFormatHelper.getCamelCaseBig( null ) );
        assertEquals( "", BapiFormatHelper.getCamelCaseBig( "" ) );
        assertEquals( "X", BapiFormatHelper.getCamelCaseBig( "X" ) );
        assertEquals( "XY", BapiFormatHelper.getCamelCaseBig( "X_Y" ) );
        assertEquals( "XaYbZc", BapiFormatHelper.getCamelCaseBig( "xa_yb_zc" ) );
        assertEquals( "MyLittleClass", BapiFormatHelper.getCamelCaseBig( "MY_LITTLE_CLASS" ) );
        assertEquals( "MyLittleClass", BapiFormatHelper.getCamelCaseBig( "_MY_LITTLE_CLASS_" ) );
    }

}
