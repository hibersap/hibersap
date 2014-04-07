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

package org.hibersap.mapping.model;

import org.hibersap.annotations.Parameter;
import org.hibersap.conversion.ConverterCache;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static org.hibersap.execution.UnsafeCastHelper.castToMap;

public class StructureMappingTest
{
    private final ConverterCache converterCache = new ConverterCache();

    private StructureMapping structureMapping;

    @Before
    public void createStructureMapping() throws Exception
    {
        structureMapping = new StructureMapping( TestStructureBean.class, "sapName", "javaName",
                null );
        structureMapping.addParameter( new FieldMapping( Integer.class, "sapField1", "javaField1", null ) );
        structureMapping.addParameter( new FieldMapping( float.class, "sapField2", "javaField2", null ) );
    }

    @Test
    public void getUnconvertedValueToJavaMapsFields() throws Exception
    {
        HashMap<String, Object> fieldMap = new HashMap<String, Object>();
        fieldMap.put( "sapField1", Integer.MAX_VALUE );
        fieldMap.put( "sapField2", Float.MAX_VALUE );

        Object value = structureMapping.getUnconvertedValueToJava( fieldMap, converterCache );

        assertThat( value ).isInstanceOf( TestStructureBean.class );
        TestStructureBean bean = ( TestStructureBean ) value;
        assertThat( bean.javaField1 ).isEqualTo( Integer.MAX_VALUE );
        assertThat( bean.javaField2 ).isEqualTo( Float.MAX_VALUE );
    }

    @Test
    public void getUnconvertedValueToSapMapsFields() throws Exception
    {
        TestStructureBean bean = new TestStructureBean();
        bean.javaField1 = Integer.MAX_VALUE;
        bean.javaField2 = Float.MAX_VALUE;

        Object value = structureMapping.getUnconvertedValueToSap( bean, converterCache );

        assertThat( value ).isInstanceOf( Map.class );
        Map<String, Object> map = castToMap( value );
        assertThat( map.get( "sapField1" ) ).isEqualTo( Integer.MAX_VALUE );
        assertThat( map.get( "sapField2" ) ).isEqualTo( Float.MAX_VALUE );
    }

    private static class TestStructureBean
    {
        @Parameter( "sapField1" )
        private Integer javaField1;

        @Parameter( "sapField2" )
        private float javaField2;

        private TestStructureBean()
        {
        }
    }
}
