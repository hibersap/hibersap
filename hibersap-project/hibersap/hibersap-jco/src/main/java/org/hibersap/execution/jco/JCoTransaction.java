package org.hibersap.execution.jco;

/*
 * Copyright (C) 2008 akquinet tech@spree GmbH
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

import org.hibersap.HibersapException;
import org.hibersap.bapi.BapiTransactionCommit;
import org.hibersap.bapi.BapiTransactionRollback;
import org.hibersap.mapping.AnnotationBapiMapper;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.session.SessionImplementor;
import org.hibersap.session.Transaction;

/**
 * @author Carsten Erker
 */
public class JCoTransaction
    implements Transaction
{
    private final SessionImplementor session;

    private BapiMapping bapiCommit;

    private BapiMapping bapiRollback;

    private boolean inTransaction = false;

    public JCoTransaction( SessionImplementor session )
    {
        this.session = session;
        initTransactionBapis();
    }

    public void begin()
    {
        if ( inTransaction )
        {
            throw new HibersapException( "Transaction was already started" );
        }
        inTransaction = true;
    }

    public void commit()
    {
        errorIfNotInTransaction();
        executeBapi( new BapiTransactionCommit(), bapiCommit );
    }

    public void rollback()
    {
        errorIfNotInTransaction();
        executeBapi( new BapiTransactionRollback(), bapiRollback );
    }

    private void errorIfNotInTransaction()
    {
        if ( !inTransaction )
        {
            throw new HibersapException( "Not in transaction" );
        }
    }

    private void executeBapi( Object bapi, BapiMapping mapping )
    {
        session.execute( bapi, mapping );
    }

    // TODO cache mappings
    private void initTransactionBapis()
    {
        AnnotationBapiMapper mapper = new AnnotationBapiMapper();
        this.bapiCommit = mapper.mapBapi( BapiTransactionCommit.class );
        this.bapiRollback = mapper.mapBapi( BapiTransactionRollback.class );
    }
}
