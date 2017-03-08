/*
 * Copyright (c) 2008-2017 akquinet tech@spree GmbH
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

import java.util.Map;
import javax.resource.ResourceException;
import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.MappedRecord;
import javax.resource.cci.Record;
import javax.resource.cci.RecordFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.HibersapException;
import org.hibersap.execution.Connection;
import org.hibersap.execution.UnsafeCastHelper;
import org.hibersap.session.Credentials;
import org.hibersap.session.SessionImplementor;
import org.hibersap.session.Transaction;

/**
 * Implementation for JCA, i.e. it uses a deployed resource adapter to connect to SAP.
 *
 * @author dahm
 */
public class JCAConnection implements Connection {

    private static final Log LOG = LogFactory.getLog( JCAConnection.class );
    private final JCAMapper mapper = new JCAMapper();
    private final ConnectionProvider connectionProvider;
    private RecordFactory recordFactory;
    private Transaction transaction;

    public JCAConnection( final ConnectionFactory connectionFactory, final String connectionSpecFactoryName ) {
        connectionProvider = new ConnectionProvider( connectionFactory, connectionSpecFactoryName );
        try {
            recordFactory = connectionFactory.getRecordFactory();
        } catch ( ResourceException e ) {
            throw new HibersapException( "Problem creating RecordFactory.", e );
        }
    }

    public Transaction beginTransaction( final SessionImplementor session ) {
        if ( transaction == null ) {
            LOG.debug( "Begin JCA transaction: " + session );

            try {
                transaction = new JCATransaction( connectionProvider.getConnection().getLocalTransaction() );
            } catch ( final ResourceException e ) {
                throw new HibersapException( "new JCATransaction", e );
            }

            transaction.begin();
        }

        return transaction;
    }

    public void close() {
        try {
            connectionProvider.getConnection().close();
        } catch ( final ResourceException e ) {
            throw new HibersapException( "While closing JCA connection", e );
        }
    }

    public void execute( final String bapiName, final Map<String, Object> functionMap ) {
        Record result;

        try {
            MappedRecord mappedInputRecord = mapper.mapFunctionMapValuesToMappedRecord( bapiName, recordFactory,
                                                                                        functionMap );

            LOG.debug( "JCA Execute: " + bapiName + ", arguments= " + functionMap + "\ninputRecord = "
                               + mappedInputRecord );

            result = connectionProvider.getConnection().createInteraction().execute( null, mappedInputRecord );

            LOG.debug( "JCA Execute: " + bapiName + ", result = " + result );

            final Map<String, Object> resultMap = UnsafeCastHelper.castToMap( result );
            mapper.mapRecordToFunctionMap( functionMap, resultMap );
        } catch ( final ResourceException e ) {
            throw new HibersapException( "Error executing function module " + bapiName, e );
        }
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setCredentials( final Credentials credentials ) {
        connectionProvider.setCredentials( credentials );
    }
}
