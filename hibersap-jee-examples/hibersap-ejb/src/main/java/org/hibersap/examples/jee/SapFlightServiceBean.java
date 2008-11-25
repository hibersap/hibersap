package org.hibersap.examples.jee;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibersap.bapi.BapiRet2;
import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.examples.flightdetail.FlightData;
import org.hibersap.examples.flightdetail.FlightDetailBapi;
import org.hibersap.session.Session;
import org.hibersap.session.SessionFactory;
import org.jboss.annotation.ejb.RemoteBinding;

@Stateless
@RemoteBinding(jndiBinding = SapFlightService.JNDI_NAME)
@Remote(SapFlightService.class)
public class SapFlightServiceBean
    implements SapFlightService
{
    private static final Logger LOG = Logger.getLogger( SapFlightServiceBean.class );

    private SessionFactory sessionFactory;

    @PostConstruct
    public void init()
    {
        LOG.info( "Initializing flight service" );
        final AnnotationConfiguration configuration = new AnnotationConfiguration();
        sessionFactory = configuration.buildSessionFactory();
        LOG.info( "DONE Initializing flight service" );
    }

    public void showFlightDetail( final Date date )
    {
        final Session session = sessionFactory.openSession();

        try
        {
            session.beginTransaction();
            final FlightDetailBapi flightDetail = new FlightDetailBapi( "AZ", "0788", date );
            session.execute( flightDetail );
            session.getTransaction().commit();
            showResult( flightDetail );
        }
        finally
        {
            session.close();
        }
    }

    private void showResult( final FlightDetailBapi flightDetail )
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
