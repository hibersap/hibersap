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

package org.hibersap.bapi;

/**
 * Contains some of the "magic" constants commonly used in SAP systems.
 *
 * @author Markus Dahm
 */
@SuppressWarnings("WeakerAccess")
public final class BapiConstants {

    public static final String TYPE = "TYPE";
    public static final String ID = "ID";
    public static final String NUMBER = "NUMBER";
    public static final String MESSAGE = "MESSAGE";
    public static final String LOG_NO = "LOG_NO";
    public static final String LOG_MSG_NO = "LOG_MSG_NO";
    public static final String MESSAGE_V1 = "MESSAGE_V1";
    public static final String MESSAGE_V2 = "MESSAGE_V2";
    public static final String MESSAGE_V3 = "MESSAGE_V3";
    public static final String MESSAGE_V4 = "MESSAGE_V4";
    public static final String PARAMETER = "PARAMETER";
    public static final String ROW = "ROW";
    public static final String FIELD = "FIELD";
    public static final String SYSTEM = "SYSTEM";
    public static final String BAPI_TRANSACTION_COMMIT = "BAPI_TRANSACTION_COMMIT";
    public static final String BAPI_TRANSACTION_ROLLBACK = "BAPI_TRANSACTION_ROLLBACK";
    public static final String EXPORT_RETURN = "EXPORT/RETURN";
    public static final String WAIT = "WAIT";
    public static final String IMPORT = "IMPORT";
    public static final String EXPORT = "EXPORT";
    public static final String TABLE = "TABLE";

    private BapiConstants() {
        // Utility class that should not be instantiated.
    }
}
