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

package org.hibersap.execution.jco;

import org.hibersap.HibersapException;
import org.hibersap.bapi.BapiTransactionCommit;
import org.hibersap.bapi.BapiTransactionRollback;
import org.hibersap.mapping.AnnotationBapiMapper;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.session.AbstractTransaction;
import org.hibersap.session.SessionImplementor;

/**
 * @author Carsten Erker
 */
public class JCoTransaction extends AbstractTransaction {

    private final SessionImplementor session;

    private BapiMapping bapiCommit;

    private BapiMapping bapiRollback;

    private boolean inTransaction = false;

    public JCoTransaction( final SessionImplementor session ) {
        this.session = session;
        initTransactionBapis();
    }

    public void begin() {
        if ( inTransaction ) {
            throw new HibersapException( "Transaction was already started" );
        }
        inTransaction = true;
    }

    public void commit() {
        errorIfNotInTransaction();
        notifySynchronizationsBeforeCompletion();
        try {
            executeBapi( new BapiTransactionCommit(), bapiCommit );
            notifySynchronizationsAfterCompletion( true );
        } catch ( RuntimeException e ) {
            notifySynchronizationsAfterCompletion( false );
            throw e;
        }
    }

    public void rollback() {
        errorIfNotInTransaction();
        try {
            executeBapi( new BapiTransactionRollback(), bapiRollback );
        } finally {
            notifySynchronizationsAfterCompletion( false );
        }
    }

    private void errorIfNotInTransaction() {
        if ( !inTransaction ) {
            throw new HibersapException( "Not in transaction" );
        }
    }

    private void executeBapi( final Object bapi, final BapiMapping mapping ) {
        session.execute( bapi, mapping );
    }

    // TODO cache mappings
    private void initTransactionBapis() {
        AnnotationBapiMapper mapper = new AnnotationBapiMapper();
        this.bapiCommit = mapper.mapBapi( BapiTransactionCommit.class );
        this.bapiRollback = mapper.mapBapi( BapiTransactionRollback.class );
    }
}
