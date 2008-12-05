package org.hibersap;

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

/**
 * Contains some of the "magic" constants commonly used in SAP systems.
 * 
 * @author dahm
 */
public interface BapiConstants
{
    String RETURN = "RETURN";

    String TYPE = "TYPE";

    String ID = "ID";

    String NUMBER = "NUMBER";

    String MESSAGE = "MESSAGE";

    String LOG_NO = "LOG_NO";

    String LOG_MSG_NO = "LOG_MSG_NO";

    String MESSAGE_V1 = "MESSAGE_V1";

    String MESSAGE_V2 = "MESSAGE_V2";

    String MESSAGE_V3 = "MESSAGE_V3";

    String MESSAGE_V4 = "MESSAGE_V4";

    String PARAMETER = "PARAMETER";

    String ROW = "ROW";

    String FIELD = "FIELD";

    String SYSTEM = "SYSTEM";

    String BAPI_TRANSACTION_COMMIT = "BAPI_TRANSACTION_COMMIT";

    String BAPI_TRANSACTION_ROLLBACK = "BAPI_TRANSACTION_ROLLBACK";

    String EXPORT_RETURN = "EXPORT/RETURN";

    String WAIT = "WAIT";
}
