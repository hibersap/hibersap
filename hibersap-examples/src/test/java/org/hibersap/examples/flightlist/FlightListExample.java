package org.hibersap.examples.flightlist;

import java.util.List;

import org.hibersap.bapi.BapiRet2;
import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.examples.HibersapExample;
import org.hibersap.session.Session;
import org.hibersap.session.SessionFactory;
import org.junit.Test;

public class FlightListExample extends HibersapExample
{
  @Test
  public void showFlightDetail()
  {
    AnnotationConfiguration configuration = new AnnotationConfiguration();
    configuration.addAnnotatedClass(FlightListBapi.class);

    SessionFactory sessionFactory = configuration.buildSessionFactory();

    Session session = sessionFactory.openSession();
    session.beginTransaction();
    FlightListBapi flightList = new FlightListBapi("DE", "Frankfurt", "EN", "Berlin", null, false,
        10);
    session.execute(flightList);
    // TODO session.getTransaction().commit();
    session.close();

    showResult(flightList);
  }

  private void showResult(FlightListBapi flightList)
  {
    System.out.println("AirlineId: " + flightList.getFromCountryKey());
    System.out.println("FromCity: " + flightList.getFromCity());
    System.out.println("ToCountryKey: " + flightList.getToCountryKey());
    System.out.println("ToCity: " + flightList.getToCity());
    System.out.println("AirlineCarrier: " + flightList.getAirlineCarrier());
    System.out.println("Afternoon: " + flightList.getAfternoon());
    System.out.println("MaxRead: " + flightList.getMaxRead());

    System.out.println("\nFlightData");
    List<Flight> flights = flightList.getFlightList();
    for (Flight flight : flights)
    {
      System.out.print("\t" + flight.getAirportFrom());
      System.out.print("\t" + flight.getAirportTo());
      System.out.print("\t" + flight.getCarrierId());
      System.out.print("\t" + flight.getConnectionId());
      System.out.print("\t" + flight.getSeatsMax());
      System.out.print("\t" + flight.getSeatsOccupied());
      System.out.println("\t" + flight.getDepartureTime());
    }

    System.out.println("\nReturn");
    BapiRet2 returnStruct = flightList.getReturnData();
    System.out.println("\tMessage: " + returnStruct.getMessage());
    System.out.println("\tNumber: " + returnStruct.getNumber());
    System.out.println("\tType: " + returnStruct.getType());
    System.out.println("\tId: " + returnStruct.getId());
  }
}
