package org.hibersap.examples.jee;

import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.hibersap.bapi.BapiRet2;
import org.hibersap.examples.flightdetail.FlightData;
import org.hibersap.examples.flightdetail.FlightDetailBapi;
import org.hibersap.examples.flightlist.Flight;
import org.hibersap.examples.flightlist.FlightListBapi;
import org.junit.Test;

public class SapFlightServiceTest {
	private static SapFlightService _service;

	@Test
	public void testEmpty() {
		// to avoid errors when the other tests are disabled
	}

	// @BeforeClass
	public static void setUp() throws Exception {
		final Context naming = new InitialContext();

		_service = (SapFlightService) naming.lookup(SapFlightService.JNDI_NAME);
		assertNotNull(_service);
		naming.close();
	}

	// @Test
	public void testFlightDetails() throws Exception {
		final Date date26Apr2002 = new GregorianCalendar(2002, Calendar.APRIL,
				26).getTime();
		final FlightDetailBapi flightDetail = _service
				.showFlightDetail(date26Apr2002);
		showResult(flightDetail);
	}

	// @Test
	public void testFlightList() throws Exception {
		final FlightListBapi flightList = _service.showFlightList();
		showResult(flightList);
	}

	private static void showResult(final FlightDetailBapi flightDetail) {
		System.out.println("AirlineId: " + flightDetail.getAirlineId());
		System.out.println("ConnectionId: " + flightDetail.getConnectionId());
		System.out.println("FlightDate: " + flightDetail.getFlightDate());

		System.out.println("FlightData");
		final FlightData flightData = flightDetail.getFlightData();
		System.out.println("\tAirlineId: " + flightData.getAirlineId());
		System.out.println("\tAirportfr: " + flightData.getAirportfr());
		System.out.println("\tAirportt: " + flightData.getAirportto());
		System.out.println("\tCityfrom: " + flightData.getCityfrom());
		System.out.println("\tCityto: " + flightData.getCityto());
		System.out.println("\tConnectid: " + flightData.getConnectid());
		System.out.println("\tCurr: " + flightData.getCurr());
		System.out.println("\tPrice: " + flightData.getPrice());
		System.out.println("\tArrtime: " + flightData.getArrtime());
		System.out.println("\tDeptime: " + flightData.getDeptime());
		System.out.println("\tFlightdate: " + flightData.getFlightdate());
		System.out.println("BapiRet2");
		final BapiRet2 returnStruct = flightDetail.getReturn();
		System.out.println("\tMessage: " + returnStruct.getMessage());
		System.out.println("\tNumber: " + returnStruct.getNumber());
		System.out.println("\tType: " + returnStruct.getType());
		System.out.println("\tId: " + returnStruct.getId());
	}

	private void showResult(final FlightListBapi flightList) {
		System.out.println("AirlineId: " + flightList.getFromCountryKey());
		System.out.println("FromCity: " + flightList.getFromCity());
		System.out.println("ToCountryKey: " + flightList.getToCountryKey());
		System.out.println("ToCity: " + flightList.getToCity());
		System.out.println("AirlineCarrier: " + flightList.getAirlineCarrier());
		System.out.println("Afternoon: " + flightList.getAfternoon());
		System.out.println("MaxRead: " + flightList.getMaxRead());

		System.out.println("\nFlightData");
		final List<Flight> flights = flightList.getFlightList();
		for (final Flight flight : flights) {
			System.out.print("\t" + flight.getAirportFrom());
			System.out.print("\t" + flight.getAirportTo());
			System.out.print("\t" + flight.getCarrierId());
			System.out.print("\t" + flight.getConnectionId());
			System.out.print("\t" + flight.getSeatsMax());
			System.out.print("\t" + flight.getSeatsOccupied());
			System.out.println("\t" + flight.getDepartureTime());
		}

		System.out.println("\nReturn");
		final BapiRet2 returnStruct = flightList.getReturnData();
		System.out.println("\tMessage: " + returnStruct.getMessage());
		System.out.println("\tNumber: " + returnStruct.getNumber());
		System.out.println("\tType: " + returnStruct.getType());
		System.out.println("\tId: " + returnStruct.getId());
	}
}
