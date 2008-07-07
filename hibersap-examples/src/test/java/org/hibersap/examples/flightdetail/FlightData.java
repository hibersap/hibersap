package org.hibersap.examples.flightdetail;

import java.util.Date;

import org.hibersap.annotations.Parameter;

public class FlightData
{
  @Parameter(name = "CARRID")
  private String _airlineId;

  @Parameter(name = "CONNID")
  private String _connectid;

  @Parameter(name = "FLDATE")
  private Date _flightdate;

  @Parameter(name = "AIRPFROM")
  private String _airportfr;

  @Parameter(name = "CITYFROM")
  private String _cityfrom;

  @Parameter(name = "AIRPTO")
  private String _airportto;

  @Parameter(name = "CITYTO")
  private String _cityto;

  @Parameter(name = "DEPTIME")
  private Date _deptime;

  @Parameter(name = "ARRTIME")
  private Date _arrtime;

  @Parameter(name = "PRICE")
  private String _price;

  @Parameter(name = "CURRENCY")
  private String _curr;

  public FlightData()
  {
  }

  public String getAirlineId()
  {
    return _airlineId;
  }

  public String getAirportfr()
  {
    return _airportfr;
  }

  public String getAirportto()
  {
    return _airportto;
  }

  public Date getArrtime()
  {
    return _arrtime;
  }

  public String getCityfrom()
  {
    return _cityfrom;
  }

  public String getCityto()
  {
    return _cityto;
  }

  public String getConnectid()
  {
    return _connectid;
  }

  public String getCurr()
  {
    return _curr;
  }

  public Date getDeptime()
  {
    return _deptime;
  }

  public Date getFlightdate()
  {
    return _flightdate;
  }

  public String getPrice()
  {
    return _price;
  }
}
