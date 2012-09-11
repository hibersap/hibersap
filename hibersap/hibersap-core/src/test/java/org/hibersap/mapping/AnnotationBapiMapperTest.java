/*
 * Copyright (c) 2009, 2011 akquinet tech@spree GmbH.
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

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.Table;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.mapping.model.FieldMapping;
import org.hibersap.mapping.model.TableMapping;
import org.junit.Test;

import java.util.Collection;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hibersap.mapping.AnnotationBapiMapperTest.FieldMappingMatcher.hasFieldNamed;
import static org.junit.Assert.assertThat;

public class AnnotationBapiMapperTest
{
    private final AnnotationBapiMapper mapper = new AnnotationBapiMapper();

    @Test
    public void mapsBapiStructureWithInheritedField()
    {
        final BapiMapping mapping = mapper.mapBapi( TestBapiClass.class );

        final TableMapping tableMapping = mapping.getTableParameters().iterator().next();
        final Set<FieldMapping> parameters = tableMapping.getComponentParameter().getParameters();

        assertThat( parameters.size(), is( 2 ) );
        assertThat( parameters, hasFieldNamed( "structureParamSubClass" ) );
        assertThat( parameters, hasFieldNamed( "structureParamSuperClass" ) );
    }

    @Test
    public void mapsTable()
    {
        final BapiMapping mapping = mapper.mapBapi( TestBapiClass.class );

        final Set<TableMapping> tableParams = mapping.getTableParameters();
        assertThat( tableParams.size(), is( 1 ) );

        final TableMapping tableMapping = tableParams.iterator().next();
        assertThat( tableMapping.getAssociatedType().getName(), equalTo( TestBapiStructureSubClass.class.getName() ) );
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

    static class FieldMappingMatcher extends TypeSafeMatcher<Collection<FieldMapping>>
    {
        private final String fieldName;

        public FieldMappingMatcher( String fieldName )
        {
            this.fieldName = fieldName;
        }

        public static Matcher<Collection<FieldMapping>> hasFieldNamed( String fieldName )
        {
            return new FieldMappingMatcher( fieldName );
        }

        @Override
        protected boolean matchesSafely( Collection<FieldMapping> fields )
        {
            for ( FieldMapping field : fields )
            {
                if ( field.getJavaName().equals( fieldName ) )
                {
                    return true;
                }
            }
            return false;
        }

        public void describeTo( Description description )
        {
            description.appendText( "has field with java name '" ).appendText( fieldName ).appendText( "'" );
        }
    }
}
