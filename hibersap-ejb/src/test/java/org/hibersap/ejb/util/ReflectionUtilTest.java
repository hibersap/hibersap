/*
 * Copyright (c) 2008-2014 akquinet tech@spree GmbH
 *
 * This file is part of Hibersap.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

public class ReflectionUtilTest {

    @Test
    public void getSessionFieldReturnsEmptySetWhenObjectDoesNotContainAnyFieldWithHibersapSessionAnnotation() throws
                                                                                                              Exception {
        Set<Field> fields = ReflectionUtil.getHibersapSessionFields( new Object() );

        assertThat( fields ).isNotNull();
        assertThat( fields ).hasSize( 0 );
    }

    @Test
    public void getSessionFieldReturnsFieldWithHibersapSessionAnnotation() throws Exception {
        Set<Field> fields = ReflectionUtil.getHibersapSessionFields( new TestBean() );

        assertThat( fields ).hasSize( 2 );
    }

    @Test
    public void getSessionManagerJndiNameReturnsTheCorrectValue() throws Exception {
        Field session1Field = TestBean.class.getDeclaredField( "session1" );

        String jndiName = ReflectionUtil.getSessionManagerJndiName( session1Field );

        assertThat( jndiName ).isEqualTo( "jndiName1" );
    }

    @Test
    public void getSessionManagerJndiNameThrowsExceptionWhenAnnotationNotPresent() throws Exception {
        try {
            ReflectionUtil.getSessionManagerJndiName( TestBean.class.getDeclaredField( "notASession" ) );
            fail();
        } catch ( InternalHiberSapException e ) {
            assertThat( e.getMessage() ).isEqualTo( "The field "
                                                            + "org.hibersap.ejb.util.ReflectionUtilTest$TestBean.notASession "
                                                            + "is not annotated with @HibersapSession" );
        }
    }

    @Test
    public void injectsSessionIntoTarget() throws Exception {
        TestBean bean = new TestBean();
        Session session = createMock( Session.class );

        ReflectionUtil.injectSessionIntoTarget( bean, TestBean.class.getDeclaredField( "session1" ), session );

        assertThat( bean.session1 ).isSameAs( session );
    }

    @Test( expected = InternalHiberSapException.class )
    public void injectSessionIntoTargetThrowsExceptionWhenFieldIsNotOfTypeHibersapSession() throws Exception {
        TestBean bean = new TestBean();
        Session session = createMock( Session.class );

        ReflectionUtil.injectSessionIntoTarget( bean, TestBean.class.getDeclaredField( "notASession" ), session );
    }

    private static class TestBean {

        @HibersapSession( value = "jndiName1" )
        private Session session1;

        @HibersapSession( value = "jndiName2" )
        private Session session2;

        private Integer notASession;
    }
}
