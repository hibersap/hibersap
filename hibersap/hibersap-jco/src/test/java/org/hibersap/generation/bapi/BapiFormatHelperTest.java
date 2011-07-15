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
