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

/**
 * Contains some of the "magic" constants commonly used in SAP systems.
 * 
 * @author Markus Dahm
 */
public class BapiConstants
{
    private BapiConstants()
    {
        // Utility class that should not be instantiated.
    }

    public static final String RETURN = "RETURN";

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
}
