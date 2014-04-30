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

import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Convert;
import org.hibersap.annotations.Parameter;
import org.hibersap.conversion.CharConverter;

import java.io.Serializable;

/**
 * Standard BAPI structure for return values
 *
 * @author Carsten Erker
 */
@BapiStructure
public class BapiRet2
        implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Message type: S Success, E Error, W Warning, I Info, A Abort
     */
    @Parameter( BapiConstants.TYPE )
    @Convert( converter = CharConverter.class )
    private char type;

    /**
     * Messages, message class
     */
    @Parameter( BapiConstants.ID )
    private String id;

    /**
     * Messages, message number
     */
    @Parameter( BapiConstants.NUMBER )
    private String number;

    /**
     * Message text
     */
    @Parameter( BapiConstants.MESSAGE )
    private String message;

    /**
     * Application log: log number
     */
    @Parameter( BapiConstants.LOG_NO )
    private String logNumber;

    /**
     * Application log: Internal message serial number
     */
    @Parameter( BapiConstants.LOG_MSG_NO )
    private String logMsgNumber;

    /**
     * Messages, message variables
     */
    @Parameter( BapiConstants.MESSAGE_V1 )
    private String messageV1;

    /**
     * Messages, message variables
     */
    @Parameter( BapiConstants.MESSAGE_V2 )
    private String messageV2;

    /**
     * Messages, message variables
     */
    @Parameter( BapiConstants.MESSAGE_V3 )
    private String messageV3;

    /**
     * Messages, message variables
     */
    @Parameter( BapiConstants.MESSAGE_V4 )
    private String messageV4;

    /**
     * Parameter name
     */
    @Parameter( BapiConstants.PARAMETER )
    private String parameter;

    /**
     * Lines in parameter
     */
    @Parameter( BapiConstants.ROW )
    private int row;

    /**
     * Field in parameter
     */
    @Parameter( BapiConstants.FIELD )
    private String field;

    /**
     * Logical system from which message originates
     */
    @Parameter( BapiConstants.SYSTEM )
    private String system;

    public String getField() {
        return this.field;
    }

    public String getId() {
        return this.id;
    }

    public String getLogMsgNumber() {
        return this.logMsgNumber;
    }

    public String getLogNumber() {
        return this.logNumber;
    }

    public String getMessage() {
        return this.message;
    }

    public String getMessageV1() {
        return this.messageV1;
    }

    public String getMessageV2() {
        return this.messageV2;
    }

    public String getMessageV3() {
        return this.messageV3;
    }

    public String getMessageV4() {
        return this.messageV4;
    }

    public String getNumber() {
        return this.number;
    }

    public String getParameter() {
        return this.parameter;
    }

    public int getRow() {
        return this.row;
    }

    public String getSystem() {
        return this.system;
    }

    public char getType() {
        return this.type;
    }
}
