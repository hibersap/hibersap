/*
 * Copyright (c) 2008-2019 akquinet tech@spree GmbH
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

package org.hibersap.execution.jca.cci;

import javax.resource.cci.ConnectionSpec;
import org.hibersap.InternalHiberSapException;
import org.hibersap.session.Credentials;
import org.jspecify.annotations.Nullable;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AbstractConnectionSpecFactoryTest {

    private final AbstractConnectionSpecFactory factory = new AbstractConnectionSpecFactory() {

        public @Nullable ConnectionSpec createConnectionSpec(Credentials credentials)
                throws InternalHiberSapException {
            // implementation not tested here
            return null;
        }
    };

    @Test
    public void getConnectionSpecClassReturnsCorrectClass() throws Exception {
        Class<?> specClass = factory
                .getConnectionSpecClass(TestConnectionSpecImpl.class.getName());

        assertThat(specClass).isEqualTo(TestConnectionSpecImpl.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getConnectionSpecClassThrowsIllegalArgumentWhenGivenClassIsNotAConnectionSpec() throws Exception {
        factory.getConnectionSpecClass(String.class.getName());
    }

    @Test(expected = ClassNotFoundException.class)
    public void getConnectionSpecThrowsClassNotFoundWhenCalledWithNonExistingClassName() throws Exception {
        factory.getConnectionSpecClass("NonExistingClass");
    }

    @Test
    public void newConnectionSpecInstanceCalledWithOneArgumentConstructor() {
        TestConnectionSpecImpl spec = (TestConnectionSpecImpl) factory
                .newConnectionSpecInstance(TestConnectionSpecImpl.class, new Class<?>[]{int.class},
                        new Object[]{4711});

        assertThat(spec.property1).isEqualTo(4711);
        assertThat(spec.property2).isEqualTo(null);
    }

    @Test
    public void newConnectionSpecInstanceCalledWithDefaultConstructor() {
        TestConnectionSpecImpl spec = (TestConnectionSpecImpl) factory.newConnectionSpecInstance(
                TestConnectionSpecImpl.class, null, null);

        assertThat(spec.property1).isEqualTo(0);
        assertThat(spec.property2).isEqualTo(null);
    }

    @Test
    public void newConnectionSpecInstanceCalledWithTwoArgumentConstructor() {
        TestConnectionSpecImpl spec = (TestConnectionSpecImpl)
                factory.newConnectionSpecInstance(
                        TestConnectionSpecImpl.class,
                        new Class<?>[]{int.class, String.class},
                        new Object[]{4711, "property2"});

        assertThat(spec.property1).isEqualTo(4711);
        assertThat(spec.property2).isEqualTo("property2");
    }

    @SuppressWarnings("unused") // constructors called by reflection
    private static class TestConnectionSpecImpl implements ConnectionSpec {

        int property1;
        String property2;

        public TestConnectionSpecImpl() {
            // do nothing
        }

        public TestConnectionSpecImpl(int property) {
            this.property1 = property;
        }

        public TestConnectionSpecImpl(int property1, String property2) {
            this.property1 = property1;
            this.property2 = property2;
        }
    }
}
