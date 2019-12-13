/*
 * Copyright (c) 2008-2019 akquinet tech@spree GmbH
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

import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.time.DateUtils;

/**
 * @author Carsten Erker
 */
public final class DateUtil {

    private DateUtil() {
        // should not be instantiated
    }

    public static Date joinDateAndTime(final Date date, final Date time) {
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(date);
        Calendar timeCal = Calendar.getInstance();
        timeCal.setTime(time);

        dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
        dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
        dateCal.set(Calendar.SECOND, timeCal.get(Calendar.SECOND));
        dateCal.set(Calendar.MILLISECOND, timeCal.get(Calendar.MILLISECOND));
        return dateCal.getTime();
    }

    public static Date newDate(final int year, final int month, final int date) {
        return newDate(year, month, date, 0, 0, 0);
    }

    public static Date newDate(final int year,
                               final int month,
                               final int date,
                               final int hourOfDay,
                               final int minute,
                               final int second) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(year, month, date, hourOfDay, minute, second);
        return cal.getTime();
    }

    public static Date stripDate(final Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(1970, Calendar.JANUARY, 1);

        return cal.getTime();
    }

    public static Date stripTime(final Date date) {
        return DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
    }
}
