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

package org.hibersap.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibersap.SapException;
import org.hibersap.SapException.SapError;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.Table;
import org.hibersap.annotations.ThrowExceptionOnError;
import org.hibersap.bapi.BapiRet2;
import org.hibersap.interceptor.impl.SapErrorInterceptor;
import org.hibersap.mapping.AnnotationBapiMapper;
import org.hibersap.mapping.model.BapiMapping;
import org.junit.Test;

public class SapErrorInterceptorTest
{
    private SapErrorInterceptor interceptor = new SapErrorInterceptor();

    private AnnotationBapiMapper mapper = new AnnotationBapiMapper();

    @Test
    public void testAfterExecuteWithExportParameterUsingDefaults()
    {
        BapiMapping bapiMapping = mapper.mapBapi( TestBapiExportParam.class );

        Map<String, Object> functionMap = new HashMap<String, Object>();
        Map<String, Object> exportParamsMap = new HashMap<String, Object>();
        exportParamsMap.put( "RETURN", buildReturnParamMap( "A", "message", "id", "number" ) );
        functionMap.put( "EXPORT", exportParamsMap );

        try
        {
            interceptor.afterExecution( bapiMapping, functionMap );
            fail();
        }
        catch ( SapException e )
        {
            List<SapError> errors = e.getErrors();
            assertEquals( 1, errors.size() );
            SapError error = errors.get( 0 );
            assertEquals( "A", error.getType() );
            assertEquals( "message", error.getMessage() );
            assertEquals( "id", error.getId() );
            assertEquals( "number", error.getNumber() );
        }
    }

    @Test
    public void testAfterExecuteWithTableParameterUsingDefaults()
    {
        BapiMapping bapiMapping = mapper.mapBapi( TestBapiTableParam.class );

        Map<String, Object> functionMap = new HashMap<String, Object>();
        Map<String, Object> tableParamsMap = new HashMap<String, Object>();
        Collection<Map<String, Object>> returnTable = new ArrayList<Map<String, Object>>();
        returnTable.add( buildReturnParamMap( "E", "messageE", "idE", "numberE" ) );
        returnTable.add( buildReturnParamMap( "W", "messageW", "idW", "numberW" ) );
        returnTable.add( buildReturnParamMap( "A", "messageA", "idA", "numberA" ) );
        returnTable.add( buildReturnParamMap( "S", "messageA", "idA", "numberA" ) );
        returnTable.add( buildReturnParamMap( "", "messageA", "idA", "numberA" ) );
        returnTable.add( buildReturnParamMap( " ", "messageA", "idA", "numberA" ) );
        tableParamsMap.put( "RETURN", returnTable );
        functionMap.put( "TABLE", tableParamsMap );

        try
        {
            interceptor.afterExecution( bapiMapping, functionMap );
            fail();
        }
        catch ( SapException e )
        {
            List<SapError> errors = e.getErrors();
            assertEquals( 2, errors.size() );

            SapError error = errors.get( 0 );
            assertEquals( "E", error.getType() );
            assertEquals( "messageE", error.getMessage() );
            assertEquals( "idE", error.getId() );
            assertEquals( "numberE", error.getNumber() );

            error = errors.get( 1 );
            assertEquals( "A", error.getType() );
            assertEquals( "messageA", error.getMessage() );
            assertEquals( "idA", error.getId() );
            assertEquals( "numberA", error.getNumber() );
        }
    }

    @Test
    public void testAfterExecuteNotUsingDefaults()
    {
        BapiMapping bapiMapping = mapper.mapBapi( TestBapiWithoutDefaults.class );

        Map<String, Object> functionMap = new HashMap<String, Object>();
        Map<String, Object> tableParamsMap = new HashMap<String, Object>();
        Collection<Map<String, Object>> returnTable = new ArrayList<Map<String, Object>>();
        returnTable.add( buildReturnParamMap( "T1", "messageT1", "idT1", "numberT1" ) );
        returnTable.add( buildReturnParamMap( "E", "messageE", "idE", "numberE" ) );
        returnTable.add( buildReturnParamMap( "T2", "messageT2", "idT2", "numberT2" ) );
        tableParamsMap.put( "MY_RETURN", returnTable );
        functionMap.put( "TABLE", tableParamsMap );

        try
        {
            interceptor.afterExecution( bapiMapping, functionMap );
            fail();
        }
        catch ( SapException e )
        {
            List<SapError> errors = e.getErrors();
            assertEquals( 2, errors.size() );

            SapError error = errors.get( 0 );
            assertEquals( "T1", error.getType() );
            assertEquals( "messageT1", error.getMessage() );
            assertEquals( "idT1", error.getId() );
            assertEquals( "numberT1", error.getNumber() );

            error = errors.get( 1 );
            assertEquals( "T2", error.getType() );
            assertEquals( "messageT2", error.getMessage() );
            assertEquals( "idT2", error.getId() );
            assertEquals( "numberT2", error.getNumber() );
        }
    }

    @Test
    public void testAfterExecuteWithoutErrorCheck()
    {
        BapiMapping bapiMapping = mapper.mapBapi( TestBapiWithoutErrorCheck.class );

        Map<String, Object> functionMap = new HashMap<String, Object>();
        HashMap<String, Object> exportParamsMap = new HashMap<String, Object>();
        exportParamsMap.put( "RETURN", buildReturnParamMap( "A", "message", "id", "number" ) );
        functionMap.put( "EXPORT", exportParamsMap );

        // no exception must be thrown
        interceptor.afterExecution( bapiMapping, functionMap );
    }

    private HashMap<String, Object> buildReturnParamMap( String type, String msg, String id, String number )
    {
        HashMap<String, Object> returnParamMap = new HashMap<String, Object>();
        returnParamMap.put( "TYPE", type );
        returnParamMap.put( "MESSAGE", msg );
        returnParamMap.put( "ID", id );
        returnParamMap.put( "NUMBER", number );
        return returnParamMap;
    }

    @Bapi(value = "TEST")
    @ThrowExceptionOnError
    private class TestBapiExportParam
    {
        @SuppressWarnings("unused")
        @Export
        @Parameter("RETURN")
        private BapiRet2 returnStruct;
    }

    @Bapi(value = "TEST")
    @ThrowExceptionOnError(returnStructure = "TABLE/RETURN")
    private class TestBapiTableParam
    {
        @SuppressWarnings("unused")
        @Table
        @Parameter("RETURN")
        private Set<BapiRet2> returnTable;
    }

    @SuppressWarnings("synthetic-access")
    @Bapi(value = "TEST")
    private class TestBapiWithoutErrorCheck
        extends TestBapiExportParam
    {
        // just need to "overwrite" annotations
    }

    @SuppressWarnings("synthetic-access")
    @Bapi(value = "TEST")
    @ThrowExceptionOnError(errorMessageTypes = { "T1", "T2" }, returnStructure = "TABLE/MY_RETURN")
    private class TestBapiWithoutDefaults
        extends TestBapiTableParam
    {
        // just need to "overwrite" annotations
    }
}
