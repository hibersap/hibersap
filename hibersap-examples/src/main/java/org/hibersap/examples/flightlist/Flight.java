package org.hibersap.examples.flightlist;

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

import java.io.Serializable;
import java.util.Date;

import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;
import org.hibersap.util.DateUtil;

/**
 * @author Carsten Erker
 */
@BapiStructure
public class Flight
    implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Parameter(FlightListConstants.CARRID)
    private String carrierId;

    @Parameter(FlightListConstants.CONNID)
    private String connectionId;

    @Parameter(FlightListConstants.AIRPFROM)
    private String airportFrom;

    @Parameter(FlightListConstants.AIRPTO)
    private String airportTo;

    @Parameter(FlightListConstants.FLDATE)
    private Date flightDate;

    @Parameter(FlightListConstants.DEPTIME)
    private Date departureTime;

    @Parameter(FlightListConstants.SEATSMAX)
    private int seatsMax;

    @Parameter(FlightListConstants.SEATSOCC)
    private int seatsOccupied;

    public String getAirportFrom()
    {
        return this.airportFrom;
    }

    public String getAirportTo()
    {
        return this.airportTo;
    }

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
        return DateUtil.joinDateAndTime( flightDate, departureTime );
    }

    public Date getFlightDate()
    {
        return flightDate;
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
