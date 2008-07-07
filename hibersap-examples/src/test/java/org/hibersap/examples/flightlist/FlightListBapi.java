package org.hibersap.examples.flightlist;

import java.util.List;

import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Convert;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.annotations.Table;
import org.hibersap.annotations.ThrowExceptionOnError;
import org.hibersap.bapi.BapiRet2;
import org.hibersap.conversion.BooleanConverter;

@Bapi(name = "BAPI_SFLIGHT_GETLIST")
@ThrowExceptionOnError
public class FlightListBapi
{
  // Import parameters

  @Import
  @Parameter(name = "FROMCOUNTRYKEY")
  private final String fromCountryKey;

  @Import
  @Parameter(name = "FROMCITY")
  private final String fromCity;

  @Import
  @Parameter(name = "TOCOUNTRYKEY")
  private final String toCountryKey;

  @Import
  @Parameter(name = "TOCITY")
  private final String toCity;

  @Import
  @Parameter(name = "AIRLINECARRIER")
  private final String airlineCarrier;

  @Import
  @Parameter(name = "AFTERNOON")
  @Convert(converter = BooleanConverter.class)
  private final boolean afternoon;

  @Import
  @Parameter(name = "MAXREAD")
  private final int maxRead;

  // Export parameter

  @Export
  @Parameter(name = "RETURN", type = ParameterType.STRUCTURE)
  private BapiRet2 returnData;

  // Table parameter

  @Table
  @Parameter(name = "FLIGHTLIST")
  private List<Flight> flightList;

  public FlightListBapi(String fromCountryKey, String fromCity, String toCountryKey, String toCity,
      String airlineCarrier, boolean afternoon, int maxRead)
  {
    this.fromCountryKey = fromCountryKey;
    this.fromCity = fromCity;
    this.toCountryKey = toCountryKey;
    this.toCity = toCity;
    this.airlineCarrier = airlineCarrier;
    this.afternoon = afternoon;
    this.maxRead = maxRead;
  }

  public String getFromCountryKey()
  {
    return this.fromCountryKey;
  }

  public String getFromCity()
  {
    return this.fromCity;
  }

  public String getToCountryKey()
  {
    return this.toCountryKey;
  }

  public String getToCity()
  {
    return this.toCity;
  }

  public String getAirlineCarrier()
  {
    return this.airlineCarrier;
  }

  public boolean getAfternoon()
  {
    return this.afternoon;
  }

  public int getMaxRead()
  {
    return this.maxRead;
  }

  public BapiRet2 getReturnData()
  {
    return this.returnData;
  }

  public List<Flight> getFlightList()
  {
    return this.flightList;
  }
}
