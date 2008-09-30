package org.hibersap.execution;

/*
 * Copyright (C) 2008 akquinet tech@spree GmbH
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

import java.util.List;

import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.annotations.Table;
import org.hibersap.annotations.ThrowExceptionOnError;

/**
 * Name must not be Test* or *Test, because Surefire then tries to run it as a test case.
 *  
 * @author Carsten Erker
 */
@Bapi(name = "bapiName")
@ThrowExceptionOnError
class MyTestBapi
{
    public MyTestBapi()
    {
        // TODO Auto-generated constructor stub
    }

    MyTestBapi( Integer intParam, TestStructure structureParam, List<TestStructure> tableParam )
    {
        this.intParam = intParam;
        this.structureParam = structureParam;
        this.tableParam = tableParam;
    }

    @SuppressWarnings("unused")
    @Import
    @Parameter(name = "intParam")
    Integer intParam;

    @SuppressWarnings("unused")
    @Export
    @Parameter(name = "structureParam", type = ParameterType.STRUCTURE)
    TestStructure structureParam;

    @SuppressWarnings("unused")
    @Table
    @Parameter(name = "tableParam")
    List<TestStructure> tableParam;

    @BapiStructure
    static class TestStructure
    {
        @SuppressWarnings("unused")
        private TestStructure()
        {
            // for Hibersap
        }

        public TestStructure( char charParam )
        {
            this.charParam = charParam;
        }

        @SuppressWarnings("unused")
        @Parameter(name = "charParam")
        char charParam;
    }
}
