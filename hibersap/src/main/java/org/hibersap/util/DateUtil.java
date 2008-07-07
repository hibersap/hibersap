package org.hibersap.util;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

public class DateUtil
{
  private DateUtil()
  {
  }

  public static Date stripDate(Date date)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.set(1970, Calendar.JANUARY, 1);

    return cal.getTime();
  }

  public static Date stripTime(Date date)
  {
    return DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
  }

  public static Date joinDateAndTime(Date date, Date time)
  {
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

  public static Date newDate(int year, int month, int date)
  {
    return newDate(year, month, date, 0, 0, 0);
  }

  public static Date newDate(int year, int month, int date, int hourOfDay, int minute, int second)
  {
    Calendar cal = Calendar.getInstance();
    cal.clear();
    cal.set(year, month, date, hourOfDay, minute, second);
    return cal.getTime();
  }
}
