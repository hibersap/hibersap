package org.hibersap.execution.jca.cci;

/*
 * Copyright (C) 2008-2009 akquinet tech@spree GmbH
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

import static org.junit.Assert.assertEquals;

import javax.resource.cci.ConnectionSpec;

import org.hibersap.InternalHiberSapException;
import org.hibersap.session.Credentials;
import org.junit.Test;

public class AbstractConnectionSpecFactoryTest
{
    private static class TestConnectionSpecImpl
        implements ConnectionSpec
    {
        int property1 = -1;

        String property2 = "someText";

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
    public void getConnectionSpecClass()
        throws Exception
    {
        Class<? extends ConnectionSpec> specClass = factory.getConnectionSpecClass( TestConnectionSpecImpl.class
            .getName() );
        assertEquals( TestConnectionSpecImpl.class, specClass );
    }

    @Test(expected = IllegalArgumentException.class)
    public void getConnectionSpecClassIllegalArgument()
        throws Exception
    {
        factory.getConnectionSpecClass( String.class.getName() );
    }

    @Test(expected = ClassNotFoundException.class)
    public void getConnectionSpecClassNotFound()
        throws Exception
    {
        factory.getConnectionSpecClass( "NonExistingClass" );
    }

    @Test
    public void newConnectionSpecInstance()
        throws Exception
    {
        TestConnectionSpecImpl spec = (TestConnectionSpecImpl) factory
            .newConnectionSpecInstance( TestConnectionSpecImpl.class, new Class<?>[] { int.class },
                                        new Object[] { 4711 } );
        assertEquals( spec.property1, 4711 );

        spec = (TestConnectionSpecImpl) factory.newConnectionSpecInstance( TestConnectionSpecImpl.class, null, null );
        assertEquals( spec.property1, -1 );

        spec = (TestConnectionSpecImpl) factory.newConnectionSpecInstance( TestConnectionSpecImpl.class,
                                                                           new Class<?>[] { int.class, String.class },
                                                                           new Object[] { 4711, null } );
        assertEquals( spec.property1, 4711 );
        assertEquals( spec.property2, null );
    }
}
