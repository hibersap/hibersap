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

package org.hibersap.bapi;

import java.io.Serializable;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ThrowExceptionOnError;

/**
 * BAPI class to commit a transaction. Maps to the BAPI_TRANSACTION_COMMIT function module in SAP.
 *
 * @author Carsten Erker
 */
@Bapi(BapiConstants.BAPI_TRANSACTION_COMMIT)
@ThrowExceptionOnError
public final class BapiTransactionCommit
        implements Serializable {

    private static final long serialVersionUID = 1L;

    @Import
    @Parameter(BapiConstants.WAIT)
    private final String wait = "X";
}
