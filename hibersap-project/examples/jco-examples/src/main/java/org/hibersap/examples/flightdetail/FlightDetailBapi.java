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
 */

import java.io.Serializable;
import java.util.Date;

import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.annotations.ThrowExceptionOnError;
import org.hibersap.bapi.BapiConstants;
import org.hibersap.bapi.BapiRet2;

/**
 * @author Carsten Erker
 */
@Bapi(FlightDetailConstants.BAPI_NAME)
@ThrowExceptionOnError
public class FlightDetailBapi
    implements Serializable
{
    private static final long serialVersionUID = 1L;

    // import parameters

    @Import
    @Parameter(FlightDetailConstants.AIRLINECARRIER)
    private final String airlineId;

    @Import
    @Parameter(FlightDetailConstants.CONNECTIONNUMBER)
    private final String connectionId;

    @Import
    @Parameter(FlightDetailConstants.DATEOFFLIGHT)
    private final Date flightDate;

    // export parameters

    @Export
    @Parameter(value = FlightDetailConstants.FLIGHTDATA, type = ParameterType.STRUCTURE)
    private FlightData flightData;

    @Export
    @Parameter(value = BapiConstants.RETURN, type = ParameterType.STRUCTURE)
    private BapiRet2 bapiReturn;

    public FlightDetailBapi( final String airlineId, final String connectionId, final Date flightDate )
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

    @Override
    public String toString()
    {
        return "FlightDetailBapi[return:type=" + bapiReturn.getType() + ",message=" + bapiReturn.getMessage() + "]";
    }
}
