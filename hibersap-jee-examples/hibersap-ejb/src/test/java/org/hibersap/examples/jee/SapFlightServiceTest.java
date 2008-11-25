package org.hibersap.examples.jee;

import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.hibersap.bapi.BapiRet2;
import org.hibersap.examples.flightdetail.FlightData;
import org.hibersap.examples.flightdetail.FlightDetailBapi;
import org.junit.BeforeClass;
import org.junit.Test;

public class SapFlightServiceTest
{
    private static SapFlightService _service;

    @BeforeClass
    public static void setUp()
        throws Exception
    {
        final Context naming = new InitialContext();

        _service = (SapFlightService) naming.lookup( SapFlightService.JNDI_NAME );
        assertNotNull( _service );
        naming.close();
    }

    @Test
    public void testFlightDetails()
        throws Exception
    {
        final Date date26Apr2002 = new GregorianCalendar( 2002, Calendar.APRIL, 26 ).getTime();
        final FlightDetailBapi flightDetail = _service.showFlightDetail( date26Apr2002 );
        showResult( flightDetail );
    }

    private static void showResult( final FlightDetailBapi flightDetail )
    {
        System.out.println( "AirlineId: " + flightDetail.getAirlineId() );
        System.out.println( "ConnectionId: " + flightDetail.getConnectionId() );
        System.out.println( "FlightDate: " + flightDetail.getFlightDate() );

        System.out.println( "FlightData" );
        final FlightData flightData = flightDetail.getFlightData();
        System.out.println( "\tAirlineId: " + flightData.getAirlineId() );
        System.out.println( "\tAirportfr: " + flightData.getAirportfr() );
        System.out.println( "\tAirportt: " + flightData.getAirportto() );
        System.out.println( "\tCityfrom: " + flightData.getCityfrom() );
        System.out.println( "\tCityto: " + flightData.getCityto() );
        System.out.println( "\tConnectid: " + flightData.getConnectid() );
        System.out.println( "\tCurr: " + flightData.getCurr() );
        System.out.println( "\tPrice: " + flightData.getPrice() );
        System.out.println( "\tArrtime: " + flightData.getArrtime() );
        System.out.println( "\tDeptime: " + flightData.getDeptime() );
        System.out.println( "\tFlightdate: " + flightData.getFlightdate() );
        System.out.println( "BapiRet2" );
        final BapiRet2 returnStruct = flightDetail.getReturn();
        System.out.println( "\tMessage: " + returnStruct.getMessage() );
        System.out.println( "\tNumber: " + returnStruct.getNumber() );
        System.out.println( "\tType: " + returnStruct.getType() );
        System.out.println( "\tId: " + returnStruct.getId() );
    }
}
