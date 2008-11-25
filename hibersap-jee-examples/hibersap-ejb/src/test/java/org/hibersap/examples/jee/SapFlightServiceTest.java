package org.hibersap.examples.jee;

import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.naming.Context;
import javax.naming.InitialContext;

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
        _service.showFlightDetail( date26Apr2002 );
    }
}
