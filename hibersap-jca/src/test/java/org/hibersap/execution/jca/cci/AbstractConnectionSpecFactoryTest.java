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

package org.hibersap.execution.jca.cci;

import org.hibersap.InternalHiberSapException;
import org.hibersap.session.Credentials;
import org.junit.Test;

import javax.resource.cci.ConnectionSpec;

import static org.fest.assertions.Assertions.assertThat;

public class AbstractConnectionSpecFactoryTest
{
    @SuppressWarnings( "unused" ) // constructors called by reflection
    private static class TestConnectionSpecImpl implements ConnectionSpec
    {
        int property1;
        String property2;

        public TestConnectionSpecImpl()
        {
            // do nothing
        }

        public TestConnectionSpecImpl( int property )
        {
            this.property1 = property;
        }

        public TestConnectionSpecImpl( int property1, String property2 )
        {
            this.property1 = property1;
            this.property2 = property2;
        }
    }

    private AbstractConnectionSpecFactory factory = new AbstractConnectionSpecFactory()
    {
        public ConnectionSpec createConnectionSpec( Credentials credentials )
                throws InternalHiberSapException
        {
            // implementation not tested here
            return null;
        }
    };

    @Test
    public void getConnectionSpecClassReturnsCorrectClass() throws Exception
    {
        Class<?> specClass = factory
                .getConnectionSpecClass( TestConnectionSpecImpl.class.getName() );

        assertThat( specClass ).isEqualTo( TestConnectionSpecImpl.class );
    }

    @Test( expected = IllegalArgumentException.class )
    public void getConnectionSpecClassThrowsIllegalArgumentWhenGivenClassIsNotAConnectionSpec() throws Exception
    {
        factory.getConnectionSpecClass( String.class.getName() );
    }

    @Test( expected = ClassNotFoundException.class )
    public void getConnectionSpecThrowsClassNotFoundWhenCalledWithNonExistingClassName() throws Exception
    {
        factory.getConnectionSpecClass( "NonExistingClass" );
    }

    @Test
    public void newConnectionSpecInstanceCalledWithOneArgumentConstructor() throws Exception
    {
        TestConnectionSpecImpl spec = ( TestConnectionSpecImpl ) factory
                .newConnectionSpecInstance( TestConnectionSpecImpl.class, new Class<?>[]{int.class},
                        new Object[]{4711} );

        assertThat( spec.property1 ).isEqualTo( 4711 );
        assertThat( spec.property2 ).isEqualTo( null );
    }

    @Test
    public void newConnectionSpecInstanceCalledWithDefaultConstructor() throws Exception
    {
        TestConnectionSpecImpl spec = ( TestConnectionSpecImpl ) factory.newConnectionSpecInstance(
                TestConnectionSpecImpl.class, null, null );

        assertThat( spec.property1 ).isEqualTo( 0 );
        assertThat( spec.property2 ).isEqualTo( null );
    }

    @Test
    public void newConnectionSpecInstanceCalledWithTwoArgumentConstructor() throws Exception
    {
        TestConnectionSpecImpl spec = ( TestConnectionSpecImpl )
                factory.newConnectionSpecInstance(
                        TestConnectionSpecImpl.class,
                        new Class<?>[]{int.class, String.class},
                        new Object[]{4711, "property2"} );

        assertThat( spec.property1 ).isEqualTo( 4711 );
        assertThat( spec.property2 ).isEqualTo( "property2" );
    }
}
