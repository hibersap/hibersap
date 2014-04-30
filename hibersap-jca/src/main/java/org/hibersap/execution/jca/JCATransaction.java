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

package org.hibersap.execution.jca;

import org.hibersap.HibersapException;
import org.hibersap.session.AbstractTransaction;

import javax.resource.ResourceException;
import javax.resource.cci.LocalTransaction;

/**
 * Implementation for JCA, i.e. it uses a deployed resource adapter to connect to SAP.
 *
 * @author dahm
 */
public class JCATransaction extends AbstractTransaction {

    private final LocalTransaction transaction;

    public JCATransaction( final LocalTransaction transaction ) {
        this.transaction = transaction;
    }

    public void begin() {
        try {
            transaction.begin();
        } catch ( final ResourceException e ) {
            throw new HibersapException( "Error beginning a local transaction", e );
        }
    }

    public void commit() {
        notifySynchronizationsBeforeCompletion();
        try {
            transaction.commit();
            notifySynchronizationsAfterCompletion( true );
        } catch ( final ResourceException e ) {
            notifySynchronizationsAfterCompletion( false );
            throw new HibersapException( "Error committing a local transaction", e );
        }
    }

    public void rollback() {
        try {
            transaction.rollback();
        } catch ( final ResourceException e ) {
            throw new HibersapException( "Error rolling back a local transaction", e );
        } finally {
            notifySynchronizationsAfterCompletion( false );
        }
    }
}
