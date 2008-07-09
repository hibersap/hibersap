package org.hibersap.execution.jco;

/*
 * Copyright (C) 2008 akquinet tech@spree GmbH
 * 
 * This file is part of Hibersap.
 *
 * Hibersap is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hibersap is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Hibersap.  If not, see <http://www.gnu.org/licenses/>.
 */

import org.hibersap.bapi.BapiTransactionCommit;
import org.hibersap.bapi.BapiTransactionRollback;
import org.hibersap.mapping.AnnotationBapiMapper;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.session.Session;
import org.hibersap.session.Transaction;


/**
 * @author Carsten Erker
 */
public class JCoTransaction
    implements Transaction
{
    private final Session session;

    private BapiMapping bapiCommit;

    private BapiMapping bapiRollback;

    public JCoTransaction( Session session )
    {
        this.session = session;
        initTransactionBapis();
    }

    public void commit()
    {
        executeBapi( new BapiTransactionCommit(), bapiCommit );
    }

    private void executeBapi( Object bapi, BapiMapping mapping )
    {
        session.execute( bapi, mapping );
    }

    private void initTransactionBapis()
    {
        AnnotationBapiMapper mapper = new AnnotationBapiMapper();
        this.bapiCommit = mapper.mapBapi( BapiTransactionCommit.class );
        this.bapiRollback = mapper.mapBapi( BapiTransactionRollback.class );
    }

    public void rollback()
    {
        executeBapi( new BapiTransactionRollback(), bapiRollback );
    }
}
