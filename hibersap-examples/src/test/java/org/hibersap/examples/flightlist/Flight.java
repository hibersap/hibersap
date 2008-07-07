package org.hibersap.examples.flightlist;

import java.util.Date;

import org.hibersap.annotations.Parameter;
import org.hibersap.util.DateUtil;

public class Flight
{
  @Parameter(name = "CARRID")
  private String carrierId;

  @Parameter(name = "CONNID")
  private String connectionId;

  @Parameter(name = "AIRPFROM")
  private String airportFrom;

  @Parameter(name = "AIRPTO")
  private String airportTo;

  @Parameter(name = "FLDATE")
  private Date flightDate;

  @Parameter(name = "DEPTIME")
  private Date departureTime;

  @Parameter(name = "SEATSMAX")
  private int seatsMax;

  @Parameter(name = "SEATSOCC")
  private int seatsOccupied;

  public String getCarrierId()
  {
    return this.carrierId;
  }

  public String getConnectionId()
  {
    return this.connectionId;
  }

  public Date getDepartureTime()
  {
    return DateUtil.joinDateAndTime(flightDate, departureTime);
  }

  public String getAirportFrom()
  {
    return this.airportFrom;
  }

  public String getAirportTo()
  {
    return this.airportTo;
  }

  public int getSeatsMax()
  {
    return this.seatsMax;
  }

  public int getSeatsOccupied()
  {
    return this.seatsOccupied;
  }
}
