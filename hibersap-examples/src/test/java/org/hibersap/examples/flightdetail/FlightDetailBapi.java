package org.hibersap.examples.flightdetail;

import java.util.Date;

import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.bapi.BapiRet2;

@Bapi(name = "BAPI_SFLIGHT_GETDETAIL")
public class FlightDetailBapi
{
  // import parameters

  @Import
  @Parameter(name = "AIRLINECARRIER")
  private final String airlineId;

  @Import
  @Parameter(name = "CONNECTIONNUMBER")
  private final String connectionId;

  @Import
  @Parameter(name = "DATEOFFLIGHT")
  private final Date flightDate;

  // export parameters

  @Export
  @Parameter(name = "FLIGHTDATA", type = ParameterType.STRUCTURE)
  private FlightData flightData;

  @Export
  @Parameter(name = "RETURN", type = ParameterType.STRUCTURE)
  private BapiRet2 bapiReturn;

  public FlightDetailBapi(String airlineId, String connectionId, Date flightDate)
  {
    this.airlineId = airlineId;
    this.connectionId = connectionId;
    this.flightDate = flightDate;
  }

  public String getAirlineId()
  {
    return airlineId;
  }

  public String getConnectionId()
  {
    return connectionId;
  }

  public FlightData getFlightData()
  {
    return flightData;
  }

  public Date getFlightDate()
  {
    return flightDate;
  }

  public BapiRet2 getReturn()
  {
    return bapiReturn;
  }
}
