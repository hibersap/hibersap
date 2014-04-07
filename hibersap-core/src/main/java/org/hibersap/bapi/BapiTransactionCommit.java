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

package org.hibersap.bapi;

import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ThrowExceptionOnError;

import java.io.Serializable;

/**
 * BAPI class to commit a transaction. Maps to the BAPI_TRANSACTION_COMMIT function module in SAP.
 *
 * @author Carsten Erker
 */
@Bapi( BapiConstants.BAPI_TRANSACTION_COMMIT )
@ThrowExceptionOnError( returnStructure = BapiConstants.EXPORT_RETURN )
public final class BapiTransactionCommit
        implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Import
    @Parameter( BapiConstants.WAIT )
    @SuppressWarnings( "unused" )
    private final String wait = "X";
}
