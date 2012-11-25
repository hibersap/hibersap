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

package org.hibersap.ejb.util;

import org.hibersap.InternalHiberSapException;
import org.hibersap.ejb.interceptor.HibersapSession;
import org.hibersap.session.Session;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Set;

import static org.easymock.EasyMock.createMock;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

public class ReflectionUtilTest
{
    @Test
    public void getSessionFieldReturnsEmptySetWhenObjectDoesNotContainAnyFieldWithHibersapSessionAnnotation() throws
                                                                                                              Exception
    {
        Set<Field> fields = ReflectionUtil.getHibersapSessionFields( new Object() );

        assertThat( fields ).isNotNull();
        assertThat( fields ).hasSize( 0 );
    }

    @Test
    public void getSessionFieldReturnsFieldWithHibersapSessionAnnotation() throws Exception
    {
        Set<Field> fields = ReflectionUtil.getHibersapSessionFields( new TestBean() );

        assertThat( fields ).hasSize( 2 );
    }

    @Test
    public void getSessionManagerJndiNameReturnsTheCorrectValue() throws Exception
    {
        Field session1Field = TestBean.class.getDeclaredField( "session1" );

        String jndiName = ReflectionUtil.getSessionManagerJndiName( session1Field );

        assertThat( jndiName ).isEqualTo( "jndiName1" );
    }

    @Test
    public void getSessionManagerJndiNameThrowsExceptionWhenAnnotationNotPresent() throws Exception
    {
        try
        {
            ReflectionUtil.getSessionManagerJndiName( TestBean.class.getDeclaredField( "notASession" ) );
            fail();
        }
        catch ( InternalHiberSapException e )
        {
            assertThat( e.getMessage() ).isEqualTo( "The field "
                    + "org.hibersap.ejb.util.ReflectionUtilTest$TestBean.notASession "
                    + "is not annotated with @HibersapSession" );
        }
    }

    @Test
    public void injectsSessionIntoTarget() throws Exception
    {
        TestBean bean = new TestBean();
        Session session = createMock( Session.class );

        ReflectionUtil.injectSessionIntoTarget( bean, TestBean.class.getDeclaredField( "session1" ), session );

        assertThat( bean.session1 ).isSameAs( session );
    }

    @Test( expected = InternalHiberSapException.class )
    public void injectSessionIntoTargetThrowsExceptionWhenFieldIsNotOfTypeHibersapSession() throws Exception
    {
        TestBean bean = new TestBean();
        Session session = createMock( Session.class );

        ReflectionUtil.injectSessionIntoTarget( bean, TestBean.class.getDeclaredField( "notASession" ), session );
    }

    private static class TestBean
    {
        @HibersapSession( value = "jndiName1" )
        private Session session1;

        @HibersapSession( value = "jndiName2" )
        private Session session2;

        private Integer notASession;
    }
}
