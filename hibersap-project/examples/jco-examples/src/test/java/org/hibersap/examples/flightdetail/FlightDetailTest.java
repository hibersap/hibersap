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
import org.hibersap.examples.AbstractHibersapTest;
import org.hibersap.session.Session;
import org.hibersap.session.SessionManagerImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Carsten Erker
 */
public class FlightDetailTest extends AbstractHibersapTest {
	private static final Log LOG = LogFactory.getLog(FlightDetailTest.class);

	private final AnnotationConfiguration configuration = new AnnotationConfiguration(
			"A12");

	private SessionManagerImpl sessionManager;

	@Before
	public void setup() {
		sessionManager = (SessionManagerImpl) configuration
				.buildSessionManager();
	}

	@After
	public void reset() {
		if (sessionManager != null) {
			sessionManager.reset();
		}
	}

	@Test
	public void testShowFlightDetail() {
		final Session session = sessionManager.openSession();
		try {
			session.beginTransaction();
			final Date date26Apr2002 = new GregorianCalendar(2011, Calendar.OCTOBER, 11).getTime();
			final FlightDetailBapi flightDetail = new FlightDetailBapi("AZ", "0788", date26Apr2002);
			session.execute(flightDetail);
			session.getTransaction().commit();
			LOG.info(flightDetail);
		} finally {
			session.close();
		}
	}

	@Test
	public void testShowFlightDetailWithSapErrorMessage() {
		final Session session = sessionManager.openSession();

		try {
			session.beginTransaction();
			final FlightDetailBapi flightDetail = new FlightDetailBapi("XY",
					"1234", new Date());
			session.execute(flightDetail);
			fail();
		} catch (final SapException e) {
			final List<SapError> errors = e.getErrors();
			assertEquals(1, errors.size());
			final SapError error = errors.get(0);
			assertEquals("600", error.getNumber());
			assertEquals("BC_BOR", error.getId());
			assertEquals("E", error.getType());
			assertTrue(error.getMessage().indexOf("XY1234") > -1);
		} finally {
			session.close();
		}
	}
}
