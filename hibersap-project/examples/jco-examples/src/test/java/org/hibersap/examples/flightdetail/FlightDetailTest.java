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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.SapException;
import org.hibersap.SapException.SapError;
import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.configuration.HibersapProperties;
import org.hibersap.examples.AbstractHibersapTest;
import org.hibersap.session.Session;
import org.hibersap.session.SessionFactoryImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Carsten Erker
 */
public class FlightDetailTest
    extends AbstractHibersapTest
{
    private static final Log LOG = LogFactory.getLog( FlightDetailTest.class );

    private final AnnotationConfiguration configuration = new AnnotationConfiguration();

    private SessionFactoryImpl sessionFactory;

    @Before
    public void setup()
    {
        configuration.setProperty( HibersapProperties.SESSION_FACTORY_NAME, "F46" );
        sessionFactory = (SessionFactoryImpl) configuration.buildSessionFactory();
    }

    @After
    public void reset()
    {
        sessionFactory.reset();
    }

    @Test
    public void showFlightDetail()
    {
        final Session session = sessionFactory.openSession();
        try
        {
            session.beginTransaction();
            final Date date26Apr2002 = new GregorianCalendar( 2002, Calendar.APRIL, 26 ).getTime();
            final FlightDetailBapi flightDetail = new FlightDetailBapi( "AZ", "0788", date26Apr2002 );
            session.execute( flightDetail );
            session.getTransaction().commit();
            LOG.info( flightDetail );
        }
        finally
        {
            session.close();
        }
    }

    @Test
    public void showFlightDetailWithSapErrorMessage()
    {
        final Session session = sessionFactory.openSession();

        try
        {
            session.beginTransaction();
            final FlightDetailBapi flightDetail = new FlightDetailBapi( "XY", "1234", new Date() );
            session.execute( flightDetail );
            fail();
        }
        catch ( final SapException e )
        {
            final List<SapError> errors = e.getErrors();
            assertEquals( 1, errors.size() );
            final SapError error = errors.get( 0 );
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
}
