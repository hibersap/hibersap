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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

import org.hibersap.bapi.BapiRet2;
import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.configuration.Environment;
import org.hibersap.examples.AbstractHibersapTest;
import org.hibersap.execution.jco.JCoExecutor;
import org.hibersap.session.Session;
import org.hibersap.session.SessionFactory;
import org.junit.Test;


/**
 * @author Carsten Erker
 */
public class FlightDetailTest
    extends AbstractHibersapTest
{
    @Test
    public void showFlightDetail()
    {
        AnnotationConfiguration configuration = new AnnotationConfiguration();
        configuration.addAnnotatedClass( FlightDetailBapi.class );

        Properties properties = new Properties();
        properties.setProperty( Environment.EXECUTOR, JCoExecutor.class.getName() );
        properties.setProperty( Environment.CONNECTION_APPLICATION_SERVER, "10.20.80.76" );
        properties.setProperty( Environment.CONNECTION_LANGUAGE, "DE" );
        properties.setProperty( Environment.CONNECTION_PASSWORD, "finnland" );
        properties.setProperty( Environment.CONNECTION_POOL_NAME, "F46" );
        properties.setProperty( Environment.CONNECTION_POOL_SIZE, "10" );
        properties.setProperty( Environment.CONNECTION_SAPCLIENT, "800" );
        properties.setProperty( Environment.CONNECTION_SYSTEMNUMBER, "00" );
        properties.setProperty( Environment.CONNECTION_USERNAME, "XXXXX" );
        properties.setProperty( Environment.REPOSITORY_NAME, "F46" );
        configuration.setProperties( properties );

        SessionFactory sessionFactory = configuration.buildSessionFactory();

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        FlightDetailBapi flightDetail = new FlightDetailBapi( "AZ", "0788", new GregorianCalendar( 2002,
                                                                                                   Calendar.APRIL, 26 )
            .getTime() );
        session.execute( flightDetail );
        session.getTransaction().commit();
        session.close();

        showResult( flightDetail );
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
