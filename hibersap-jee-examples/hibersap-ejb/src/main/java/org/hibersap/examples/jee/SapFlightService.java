package org.hibersap.examples.jee;

import java.util.Date;

public interface SapFlightService
{
    String LOCAL_JNDI_NAME = "hibersap/jee/FlightService/local";

    String JNDI_NAME = "hibersap/jee/FlightService/remote";

    void init();

    void showFlightDetail( final Date date );
}