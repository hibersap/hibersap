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

package org.hibersap.execution.jco;

import org.easymock.EasyMock;
import org.hibersap.HibersapException;
import org.hibersap.bapi.BapiTransactionCommit;
import org.hibersap.bapi.BapiTransactionRollback;
import org.hibersap.mapping.AnnotationBapiMapper;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.session.SessionImplementor;
import org.junit.Test;

import java.util.TimeZone;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.isA;

public class JCoTransactionTest
{
    private static final AnnotationBapiMapper mapper = new AnnotationBapiMapper();
    private static final BapiMapping bapiCommitMapping = mapper.mapBapi( BapiTransactionCommit.class );
    private static final BapiMapping bapiRollbackMapping = mapper.mapBapi( BapiTransactionRollback.class );

    private final SessionImplementor sessionMock = EasyMock.createMock( SessionImplementor.class );
    private final JCoTransaction transaction = new JCoTransaction( sessionMock );
    
    @Test(expected = HibersapException.class)
    public void testMustNotBeginAlreadyStartedTransaction()
        throws Exception
    {
        transaction.begin();
        transaction.begin();
    }

    @Test
    public void testCommit()
        throws Exception
    {
        sessionMock.execute(isA(BapiTransactionCommit.class), eq(bapiCommitMapping));
        EasyMock.replay(sessionMock);
        transaction.begin();
        transaction.commit();
    }

    @Test
    public void testRollback()
        throws Exception
    {
        sessionMock.execute(isA(BapiTransactionRollback.class), eq(bapiRollbackMapping));
        EasyMock.replay(sessionMock);
        transaction.begin();
        transaction.rollback();
    }
}
