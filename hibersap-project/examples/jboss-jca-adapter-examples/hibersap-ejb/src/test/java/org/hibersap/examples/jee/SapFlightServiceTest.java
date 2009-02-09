package org.hibersap.examples.jee;

import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.examples.flightdetail.FlightDetailBapi;
import org.hibersap.examples.flightlist.FlightListBapi;
import org.junit.BeforeClass;
import org.junit.Test;

public class SapFlightServiceTest
{
    private static final Log LOG = LogFactory.getLog( SapFlightServiceTest.class );

    private static SapFlightService _service;

    @Test
    public void testEmpty()
    {
        // to avoid errors when the other tests are disabled
    }

    @BeforeClass
    public static void setUp()
        throws Exception
    {
        final Context naming = new InitialContext();

        _service = (SapFlightService) naming.lookup( SapFlightService.JNDI_NAME );
        assertNotNull( _service );
        naming.close();
    }

    // @Test
    public void testFlightDetails()
        throws Exception
    {
        final Date date26Apr2002 = new GregorianCalendar( 2002, Calendar.APRIL, 26 ).getTime();
        FlightDetailBapi flightDetail = _service.showFlightDetail( date26Apr2002 );
        showResult( flightDetail );
    }

    // @Test
    public void testFlightList()
        throws Exception
    {
        final FlightListBapi flightList = _service.showFlightList();
        showResult( flightList );
    }

    private static void showResult( final FlightDetailBapi flightDetail )
    {
        LOG.info( flightDetail );
    }

    private void showResult( final FlightListBapi flightList )
    {
        LOG.info( flightList );
    }
}
