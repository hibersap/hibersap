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

/**
 * @author Carsten Erker
 */
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

    public FlightListBapi( String fromCountryKey, String fromCity, String toCountryKey, String toCity,
                           String airlineCarrier, boolean afternoon, int maxRead )
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
}
