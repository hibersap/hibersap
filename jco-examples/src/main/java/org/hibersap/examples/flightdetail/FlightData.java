package org.hibersap.examples.flightdetail;

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
 * 
 * @author Carsten Erker
 */

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 * @author Carsten Erker
 */
@BapiStructure
public class FlightData
    implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Parameter(FlightDetailConstants.CARRID)
    private String airlineId;

    @Parameter(FlightDetailConstants.CONNID)
    private String connectid;

    @Parameter(FlightDetailConstants.FLDATE)
    private Date flightdate;

    @Parameter(FlightDetailConstants.AIRPFROM)
    private String airportfr;

    @Parameter(FlightDetailConstants.CITYFROM)
    private String cityfrom;

    @Parameter(FlightDetailConstants.AIRPTO)
    private String airportto;

    @Parameter(FlightDetailConstants.CITYTO)
    private String cityto;

    @Parameter(FlightDetailConstants.DEPTIME)
    private Date deptime;

    @Parameter(FlightDetailConstants.ARRTIME)
    private Date arrtime;

    @Parameter(FlightDetailConstants.PRICE)
    private BigDecimal price;

    @Parameter(FlightDetailConstants.CURRENCY)
    private String curr;

    public String getAirlineId()
    {
        return airlineId;
    }

    public String getAirportfr()
    {
        return airportfr;
    }

    public String getAirportto()
    {
        return airportto;
    }

    public Date getArrtime()
    {
        return arrtime;
    }

    public String getCityfrom()
    {
        return cityfrom;
    }

    public String getCityto()
    {
        return cityto;
    }

    public String getConnectid()
    {
        return connectid;
    }

    public String getCurr()
    {
        return curr;
    }

    public Date getDeptime()
    {
        return deptime;
    }

    public Date getFlightdate()
    {
        return flightdate;
    }

    public BigDecimal getPrice()
    {
        return price;
    }
}
