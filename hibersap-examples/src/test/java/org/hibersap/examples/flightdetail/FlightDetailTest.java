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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibersap.SapException;
import org.hibersap.SapException.SapError;
import org.hibersap.bapi.BapiRet2;
import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.configuration.Environment;
import org.hibersap.examples.AbstractHibersapTest;
import org.hibersap.session.Session;
import org.hibersap.session.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Carsten Erker
 */
public class FlightDetailTest
    extends AbstractHibersapTest
{
    private SessionFactory sessionFactory;

    @Before
    public void setup()
    {
        AnnotationConfiguration configuration = new AnnotationConfiguration();
        configuration.addAnnotatedClass( FlightDetailBapi.class );
        configuration.setProperty( Environment.SESSION_FACTORY_NAME, "F46" );
        sessionFactory = configuration.buildSessionFactory();
    }

    @After
    public void reset()
    {
        sessionFactory.reset();
    }

    @Test
    public void showFlightDetail()
    {
        Session session = sessionFactory.openSession();
        try
        {
            session.beginTransaction();
            Date date26Apr2002 = new GregorianCalendar( 2002, Calendar.APRIL, 26 ).getTime();
            FlightDetailBapi flightDetail = new FlightDetailBapi( "AZ", "0788", date26Apr2002 );
            session.execute( flightDetail );
            session.getTransaction().commit();
            showResult( flightDetail );
        }
        finally
        {
            session.close();
        }
    }

    @Test
    public void showFlightDetailWithSapErrorMessage()
    {
        Session session = sessionFactory.openSession();

        try
        {
            session.beginTransaction();
            FlightDetailBapi flightDetail = new FlightDetailBapi( "XY", "1234", new Date() );
            session.execute( flightDetail );
            fail();
        }
        catch ( SapException e )
        {
            List<SapError> errors = e.getErrors();
            assertEquals( 1, errors.size() );
            SapError error = errors.get( 0 );
            assertEquals( "600", error.getNumber() );
            assertEquals( "BC_BOR", error.getId() );
            assertEquals( "E", error.getType() );
            assertTrue( error.getMessage().indexOf( "XY1234" ) > -1 );
        }
        finally
        {
            session.close();
        }
    }

    private void showResult( FlightDetailBapi flightDetail )
    {
        System.out.println( "AirlineId: " + flightDetail.getAirlineId() );
        System.out.println( "ConnectionId: " + flightDetail.getConnectionId() );
        System.out.println( "FlightDate: " + flightDetail.getFlightDate() );

        System.out.println( "FlightData" );
        FlightData flightData = flightDetail.getFlightData();
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
        BapiRet2 returnStruct = flightDetail.getReturn();
        System.out.println( "\tMessage: " + returnStruct.getMessage() );
        System.out.println( "\tNumber: " + returnStruct.getNumber() );
        System.out.println( "\tType: " + returnStruct.getType() );
        System.out.println( "\tId: " + returnStruct.getId() );
    }
}
