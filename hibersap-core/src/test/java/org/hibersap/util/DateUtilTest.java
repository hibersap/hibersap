/*
 * Copyright (c) 2008-2012 akquinet tech@spree GmbH
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

package org.hibersap.util;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;


/**
 * @author Carsten Erker
 */
public class DateUtilTest
{
    @Test
    public void testJoinDateAndTime()
    {
        Date date = DateUtil.newDate( 2008, Calendar.JULY, 3 );
        Date time = DateUtil.newDate( 1970, Calendar.JANUARY, 1, 22, 56, 32 );
        Date expected = DateUtil.newDate( 2008, Calendar.JULY, 3, 22, 56, 32 );

        Date joined = DateUtil.joinDateAndTime( date, time );

        assertEquals( expected, joined );
    }

    @Test
    public void testStripDate()
    {
        Date datetime = DateUtil.newDate( 2008, Calendar.JULY, 3, 22, 56, 32 );
        Date expected = DateUtil.newDate( 1970, Calendar.JANUARY, 1, 22, 56, 32 );

        Date stripped = DateUtil.stripDate( datetime );

        assertEquals( expected, stripped );
    }

    @Test
    public void testStripTime()
    {
        Date datetime = DateUtil.newDate( 2008, Calendar.JULY, 3, 22, 56, 32 );
        Date expected = DateUtil.newDate( 2008, Calendar.JULY, 3 );

        Date stripped = DateUtil.stripTime( datetime );

        assertEquals( expected, stripped );
    }

    @Test
    public void testNewDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.newDate(1970, Calendar.SEPTEMBER, 27));
        assertEquals(27, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.SEPTEMBER, calendar.get(Calendar.MONTH));
        assertEquals(1970, calendar.get(Calendar.YEAR));
        assertEquals(0, calendar.get(Calendar.HOUR));
        assertEquals(0, calendar.get(Calendar.MINUTE));
        assertEquals(0, calendar.get(Calendar.SECOND));
        assertEquals(0, calendar.get(Calendar.MILLISECOND));
    }
}
