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

package org.hibersap.mapping;

import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.Table;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.mapping.model.FieldMapping;
import org.hibersap.mapping.model.TableMapping;
import org.junit.Test;

import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

public class AnnotationBapiMapperTest
{
    private final AnnotationBapiMapper mapper = new AnnotationBapiMapper();

    @Test
    public void mapsBapiStructureWithInheritedField()
    {
        final BapiMapping mapping = mapper.mapBapi( TestBapiClass.class );

        final TableMapping tableMapping = mapping.getTableParameters().iterator().next();
        final Set<FieldMapping> parameters = tableMapping.getComponentParameter().getParameters();

        assertThat( parameters ).hasSize( 2 )
                .onProperty( "javaName" ).contains( "structureParamSubClass", "structureParamSuperClass" );
    }

    @Test
    public void mapsTable()
    {
        final BapiMapping mapping = mapper.mapBapi( TestBapiClass.class );

        final Set<TableMapping> tableParams = mapping.getTableParameters();
        assertThat( tableParams ).hasSize( 1 );

        final TableMapping tableMapping = tableParams.iterator().next();
        assertThat( tableMapping.getAssociatedType().getName() ).isEqualTo( TestBapiStructureSubClass.class.getName() );
    }

    @Bapi( "test" )
    private class TestBapiClass
    {
        @Import
        @Parameter( "ABAP_FIELD" )
        @SuppressWarnings( "unused" )
        private int intParam;

        @Table
        @Parameter( "ABAP_TABLE" )
        @SuppressWarnings( "unused" )
        private Set<TestBapiStructureSubClass> tableParam;
    }

    private class TestBapiStructureSubClass extends TestBapiStructureSuperClass
    {
        @Parameter( "ABAP_FIELD" )
        @SuppressWarnings( "unused" )
        private long structureParamSubClass;
    }

    private class TestBapiStructureSuperClass
    {
        @Parameter( "ABAP_FIELD" )
        @SuppressWarnings( "unused" )
        private String structureParamSuperClass;
    }
}
