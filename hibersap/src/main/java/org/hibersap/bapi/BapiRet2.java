package org.hibersap.bapi;

import org.hibersap.annotations.Convert;
import org.hibersap.annotations.Parameter;
import org.hibersap.conversion.CharConverter;

/**
 * Standard BAPI structure for return values
 * 
 * @author cerker
 */
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

  public char getType()
  {
    return this.type;
  }

  public String getId()
  {
    return this.id;
  }

  public String getNumber()
  {
    return this.number;
  }

  public String getMessage()
  {
    return this.message;
  }

  public String getLogNumber()
  {
    return this.logNumber;
  }

  public String getLogMsgNumber()
  {
    return this.logMsgNumber;
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

  public String getParameter()
  {
    return this.parameter;
  }

  public int getRow()
  {
    return this.row;
  }

  public String getField()
  {
    return this.field;
  }

  public String getSystem()
  {
    return this.system;
  }
}
