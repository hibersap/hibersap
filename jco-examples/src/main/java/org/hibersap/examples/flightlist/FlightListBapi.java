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
import java.util.List;

import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Convert;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.annotations.Table;
import org.hibersap.annotations.ThrowExceptionOnError;
import org.hibersap.bapi.BapiConstants;
import org.hibersap.bapi.BapiRet2;
import org.hibersap.conversion.BooleanConverter;

/**
 * @author Carsten Erker
 */
@Bapi(FlightListConstants.BAPI_NAME)
@ThrowExceptionOnError
public class FlightListBapi
    implements Serializable
{
    private static final long serialVersionUID = 1L;

    // Import parameters

    @Import
    @Parameter(FlightListConstants.FROMCOUNTRYKEY)
    private final String fromCountryKey;

    @Import
    @Parameter(FlightListConstants.FROMCITY)
    private final String fromCity;

    @Import
    @Parameter(FlightListConstants.TOCOUNTRYKEY)
    private final String toCountryKey;

    @Import
    @Parameter(FlightListConstants.TOCITY)
    private final String toCity;

    @Import
    @Parameter(FlightListConstants.AIRLINECARRIER)
    private final String airlineCarrier;

    @Import
    @Parameter(FlightListConstants.AFTERNOON)
    @Convert(converter = BooleanConverter.class)
    private final boolean afternoon;

    @Import
    @Parameter(FlightListConstants.MAXREAD)
    private final int maxRead;

    // Export parameter

    @Export
    @Parameter(value = BapiConstants.RETURN, type = ParameterType.STRUCTURE)
    private BapiRet2 returnData;

    // Table parameter

    @Table
    @Parameter(FlightListConstants.FLIGHTLIST)
    private List<Flight> flightList;

    public FlightListBapi( final String fromCountryKey, final String fromCity, final String toCountryKey,
                           final String toCity, final String airlineCarrier, final boolean afternoon, final int maxRead )
    {
        this.fromCountryKey = fromCountryKey;
        this.fromCity = fromCity;
        this.toCountryKey = toCountryKey;
        this.toCity = toCity;
        this.airlineCarrier = airlineCarrier;
        this.afternoon = afternoon;
        this.maxRead = maxRead;
    }

    public boolean getAfternoon()
    {
        return this.afternoon;
    }

    public String getAirlineCarrier()
    {
        return this.airlineCarrier;
    }

    public List<Flight> getFlightList()
    {
        return this.flightList;
    }

    public String getFromCity()
    {
        return this.fromCity;
    }

    public String getFromCountryKey()
    {
        return this.fromCountryKey;
    }

    public int getMaxRead()
    {
        return this.maxRead;
    }

    public BapiRet2 getReturnData()
    {
        return this.returnData;
    }

    public String getToCity()
    {
        return this.toCity;
    }

    public String getToCountryKey()
    {
        return this.toCountryKey;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append( "AirlineId: " ).append( getFromCountryKey() ).append( '\n' );
        sb.append( "FromCity: " ).append( getFromCity() ).append( '\n' );
        sb.append( "ToCountryKey: " ).append( getToCountryKey() ).append( '\n' );
        sb.append( "ToCity: " ).append( getToCity() ).append( '\n' );
        sb.append( "AirlineCarrier: " ).append( getAirlineCarrier() ).append( '\n' );
        sb.append( "Afternoon: " ).append( getAfternoon() ).append( '\n' );
        sb.append( "MaxRead: " ).append( getMaxRead() ).append( '\n' );

        sb.append( "FlightData" );
        final List<Flight> flights = getFlightList();
        for ( final Flight flight : flights )
        {
            sb.append( "\t" ).append( flight.getAirportFrom() );
            sb.append( "\t" ).append( flight.getAirportTo() );
            sb.append( "\t" ).append( flight.getCarrierId() );
            sb.append( "\t" ).append( flight.getConnectionId() );
            sb.append( "\t" ).append( flight.getSeatsMax() );
            sb.append( "\t" ).append( flight.getSeatsOccupied() );
            sb.append( "\t" ).append( flight.getDepartureTime() );
        }

        sb.append( "\nReturn" );
        final BapiRet2 returnStruct = getReturnData();
        sb.append( "\tMessage: " ).append( returnStruct.getMessage() ).append( '\n' );
        sb.append( "\tNumber: " ).append( returnStruct.getNumber() ).append( '\n' );
        sb.append( "\tType: " ).append( returnStruct.getType() ).append( '\n' );
        sb.append( "\tId: " ).append( returnStruct.getId() ).append( '\n' );

        return sb.toString();
    }
}
