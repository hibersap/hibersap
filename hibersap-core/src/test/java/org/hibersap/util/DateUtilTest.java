/*
 * Copyright (c) 2008-2014 akquinet tech@spree GmbH
 *
 * This file is part of Hibersap.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hibersap.util;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;


/**
 * @author Carsten Erker
 */
public class DateUtilTest {

    @Test
    public void testJoinDateAndTime() {
        Date date = DateUtil.newDate( 2008, Calendar.JULY, 3 );
        Date time = DateUtil.newDate( 1970, Calendar.JANUARY, 1, 22, 56, 32 );
        Date expected = DateUtil.newDate( 2008, Calendar.JULY, 3, 22, 56, 32 );

        Date joined = DateUtil.joinDateAndTime( date, time );

        assertEquals( expected, joined );
    }

    @Test
    public void testStripDate() {
        Date datetime = DateUtil.newDate( 2008, Calendar.JULY, 3, 22, 56, 32 );
        Date expected = DateUtil.newDate( 1970, Calendar.JANUARY, 1, 22, 56, 32 );

        Date stripped = DateUtil.stripDate( datetime );

        assertEquals( expected, stripped );
    }

    @Test
    public void testStripTime() {
        Date datetime = DateUtil.newDate( 2008, Calendar.JULY, 3, 22, 56, 32 );
        Date expected = DateUtil.newDate( 2008, Calendar.JULY, 3 );

        Date stripped = DateUtil.stripTime( datetime );

        assertEquals( expected, stripped );
    }

    @Test
    public void testNewDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( DateUtil.newDate( 1970, Calendar.SEPTEMBER, 27 ) );
        assertEquals( 27, calendar.get( Calendar.DAY_OF_MONTH ) );
        assertEquals( Calendar.SEPTEMBER, calendar.get( Calendar.MONTH ) );
        assertEquals( 1970, calendar.get( Calendar.YEAR ) );
        assertEquals( 0, calendar.get( Calendar.HOUR ) );
        assertEquals( 0, calendar.get( Calendar.MINUTE ) );
        assertEquals( 0, calendar.get( Calendar.SECOND ) );
        assertEquals( 0, calendar.get( Calendar.MILLISECOND ) );
    }
}
