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

import java.math.BigDecimal;
import java.util.Date;

import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 * @author Carsten Erker
 */
@BapiStructure
public class FlightData {
	@Parameter("CARRID")
	private String _airlineId;

	@Parameter("CONNID")
	private String _connectid;

	@Parameter("FLDATE")
	private Date _flightdate;

	@Parameter("AIRPFROM")
	private String _airportfr;

	@Parameter("CITYFROM")
	private String _cityfrom;

	@Parameter("AIRPTO")
	private String _airportto;

	@Parameter("CITYTO")
	private String _cityto;

	@Parameter("DEPTIME")
	private Date _deptime;

	@Parameter("ARRTIME")
	private Date _arrtime;

	@Parameter("PRICE")
	private BigDecimal _price;

	@Parameter("CURRENCY")
	private String _curr;

	public String getAirlineId() {
		return _airlineId;
	}

	public String getAirportfr() {
		return _airportfr;
	}

	public String getAirportto() {
		return _airportto;
	}

	public Date getArrtime() {
		return _arrtime;
	}

	public String getCityfrom() {
		return _cityfrom;
	}

	public String getCityto() {
		return _cityto;
	}

	public String getConnectid() {
		return _connectid;
	}

	public String getCurr() {
		return _curr;
	}

	public Date getDeptime() {
		return _deptime;
	}

	public Date getFlightdate() {
		return _flightdate;
	}

	public BigDecimal getPrice() {
		return _price;
	}
}
