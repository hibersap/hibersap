package org.hibersap.examples.jee;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.examples.flightdetail.FlightDetailBapi;
import org.hibersap.examples.flightlist.FlightListBapi;
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

    public FlightDetailBapi showFlightDetail( final Date date )
    {
        LOG.info( "showFlightDetail" + date );
        final Session session = sessionFactory.openSession();

        try
        {
            session.beginTransaction();
            final FlightDetailBapi flightDetail = new FlightDetailBapi( "AZ", "0788", date );
            session.execute( flightDetail );
            session.getTransaction().commit();
            LOG.info( "DONE showFlightDetail" + flightDetail );
            return flightDetail;
        }
        finally
        {
            session.close();
        }
    }

    public FlightListBapi showFlightList()
    {
        LOG.info( "showFlightList" );
        final Session session = sessionFactory.openSession();

        try
        {
            session.beginTransaction();
            final FlightListBapi flightList = new FlightListBapi( "DE", "Frankfurt", "DE", "Berlin", null, false, 10 );
            session.execute( flightList );
            session.getTransaction().commit();
            LOG.info( "DONE showFlightList" + flightList );
            return flightList;
        }
        finally
        {
            session.close();
        }
    }
}
