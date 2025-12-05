/*
 * Copyright (c) 2008-2025 tech@spree GmbH
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

package org.hibersap.execution.jco;

import org.hibersap.HibersapException;
import org.hibersap.bapi.BapiTransactionCommit;
import org.hibersap.bapi.BapiTransactionRollback;
import org.hibersap.mapping.AnnotationBapiMapper;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.session.SessionImplementor;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class JCoTransactionTest {

    private static final AnnotationBapiMapper mapper = new AnnotationBapiMapper();
    private static final BapiMapping bapiCommitMapping = mapper.mapBapi(BapiTransactionCommit.class);
    private static final BapiMapping bapiRollbackMapping = mapper.mapBapi(BapiTransactionRollback.class);

    private final SessionImplementor sessionMock = mock(SessionImplementor.class);
    private final JCoTransaction transaction = new JCoTransaction(sessionMock);

    @Test(expected = HibersapException.class)
    public void testMustNotBeginAlreadyStartedTransaction() {
        transaction.begin();
        transaction.begin();
    }

    @Test
    public void testCommit() {
        transaction.begin();
        transaction.commit();

        verify(sessionMock, times(1)).execute(isA(BapiTransactionCommit.class), eq(bapiCommitMapping));
    }

    @Test
    public void testRollback() {
        transaction.begin();
        transaction.rollback();

        verify(sessionMock, times(1)).execute(isA(BapiTransactionRollback.class), eq(bapiRollbackMapping));
    }
}
