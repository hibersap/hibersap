package org.hibersap.bapi;

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

import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Convert;
import org.hibersap.annotations.Parameter;
import org.hibersap.conversion.CharConverter;

/**
 * Standard BAPI structure for return values
 * 
 * @author Carsten Erker
 */
@BapiStructure
public class BapiRet2
{
    /**
     * Message type: S Success, E Error, W Warning, I Info, A Abort
     */
    @Parameter(name = "TYPE")
    @Convert(converter = CharConverter.class)
    private char type;

    /**
     * Messages, message class
     */
    @Parameter(name = "ID")
    private String id;

    /**
     * Messages, message number
     */
    @Parameter(name = "NUMBER")
    private String number;

    /**
     * Message text
     */
    @Parameter(name = "MESSAGE")
    private String message;

    /**
     * Application log: log number
     */
    @Parameter(name = "LOG_NO")
    private String logNumber;

    /**
     * Application log: Internal message serial number
     */
    @Parameter(name = "LOG_MSG_NO")
    private String logMsgNumber;

    /**
     * Messages, message variables
     */
    @Parameter(name = "MESSAGE_V1")
    private String messageV1;

    /**
     * Messages, message variables
     */
    @Parameter(name = "MESSAGE_V2")
    private String messageV2;

    /**
     * Messages, message variables
     */
    @Parameter(name = "MESSAGE_V3")
    private String messageV3;

    /**
     * Messages, message variables
     */
    @Parameter(name = "MESSAGE_V4")
    private String messageV4;

    /**
     * Parameter name
     */
    @Parameter(name = "PARAMETER")
    private String parameter;

    /**
     * Lines in parameter
     */
    @Parameter(name = "ROW")
    private int row;

    /**
     * Field in parameter
     */
    @Parameter(name = "FIELD")
    private String field;

    /**
     * Logical system from which message originates
     */
    @Parameter(name = "SYSTEM")
    private String system;

    public String getField()
    {
        return this.field;
    }

    public String getId()
    {
        return this.id;
    }

    public String getLogMsgNumber()
    {
        return this.logMsgNumber;
    }

    public String getLogNumber()
    {
        return this.logNumber;
    }

    public String getMessage()
    {
        return this.message;
    }

    public String getMessageV1()
    {
        return this.messageV1;
    }

    public String getMessageV2()
    {
        return this.messageV2;
    }

    public String getMessageV3()
    {
        return this.messageV3;
    }

    public String getMessageV4()
    {
        return this.messageV4;
    }

    public String getNumber()
    {
        return this.number;
    }

    public String getParameter()
    {
        return this.parameter;
    }

    public int getRow()
    {
        return this.row;
    }

    public String getSystem()
    {
        return this.system;
    }

    public char getType()
    {
        return this.type;
    }
}
