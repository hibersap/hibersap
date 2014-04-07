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

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;


/**
 * @author Carsten Erker
 */
public final class DateUtil
{
    public static Date joinDateAndTime( Date date, Date time )
    {
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime( date );
        Calendar timeCal = Calendar.getInstance();
        timeCal.setTime( time );

        dateCal.set( Calendar.HOUR_OF_DAY, timeCal.get( Calendar.HOUR_OF_DAY ) );
        dateCal.set( Calendar.MINUTE, timeCal.get( Calendar.MINUTE ) );
        dateCal.set( Calendar.SECOND, timeCal.get( Calendar.SECOND ) );
        dateCal.set( Calendar.MILLISECOND, timeCal.get( Calendar.MILLISECOND ) );
        return dateCal.getTime();
    }

    public static Date newDate( int year, int month, int date )
    {
        return newDate( year, month, date, 0, 0, 0 );
    }

    public static Date newDate( int year, int month, int date, int hourOfDay, int minute, int second )
    {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set( year, month, date, hourOfDay, minute, second );
        return cal.getTime();
    }

    public static Date stripDate( Date date )
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime( date );
        cal.set( 1970, Calendar.JANUARY, 1 );

        return cal.getTime();
    }

    public static Date stripTime( Date date )
    {
        return DateUtils.truncate( date, Calendar.DAY_OF_MONTH );
    }

    private DateUtil()
    {
        // should not be instantiated
    }
}
