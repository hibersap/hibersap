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

import org.hibersap.bapi.BapiRet2;
import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.examples.AbstractHibersapTest;
import org.hibersap.session.Session;
import org.hibersap.session.SessionFactory;
import org.junit.Test;


/**
 * @author Carsten Erker
 */
public class FlightListTest
    extends AbstractHibersapTest
{
    @Test
    public void showFlightDetail()
    {
        AnnotationConfiguration configuration = new AnnotationConfiguration();
        configuration.addAnnotatedClass( FlightListBapi.class );

        SessionFactory sessionFactory = configuration.buildSessionFactory();

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        FlightListBapi flightList = new FlightListBapi( "DE", "Frankfurt", "EN", "Berlin", null, false, 10 );
        session.execute( flightList );
        // TODO session.getTransaction().commit();
        session.close();

        showResult( flightList );
    }

    private void showResult( FlightListBapi flightList )
    {
        System.out.println( "AirlineId: " + flightList.getFromCountryKey() );
        System.out.println( "FromCity: " + flightList.getFromCity() );
        System.out.println( "ToCountryKey: " + flightList.getToCountryKey() );
        System.out.println( "ToCity: " + flightList.getToCity() );
        System.out.println( "AirlineCarrier: " + flightList.getAirlineCarrier() );
        System.out.println( "Afternoon: " + flightList.getAfternoon() );
        System.out.println( "MaxRead: " + flightList.getMaxRead() );

        System.out.println( "\nFlightData" );
        List<Flight> flights = flightList.getFlightList();
        for ( Flight flight : flights )
        {
            System.out.print( "\t" + flight.getAirportFrom() );
            System.out.print( "\t" + flight.getAirportTo() );
            System.out.print( "\t" + flight.getCarrierId() );
            System.out.print( "\t" + flight.getConnectionId() );
            System.out.print( "\t" + flight.getSeatsMax() );
            System.out.print( "\t" + flight.getSeatsOccupied() );
            System.out.println( "\t" + flight.getDepartureTime() );
        }

        System.out.println( "\nReturn" );
        BapiRet2 returnStruct = flightList.getReturnData();
        System.out.println( "\tMessage: " + returnStruct.getMessage() );
        System.out.println( "\tNumber: " + returnStruct.getNumber() );
        System.out.println( "\tType: " + returnStruct.getType() );
        System.out.println( "\tId: " + returnStruct.getId() );
    }
}
