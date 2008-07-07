package org.hibersap.util;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class DateUtilTest
{
  @Test
  public void stripTime()
  {
    Date datetime = DateUtil.newDate(2008, Calendar.JULY, 3, 22, 56, 32);
    Date expected = DateUtil.newDate(2008, Calendar.JULY, 3);

    Date stripped = DateUtil.stripTime(datetime);

    assertEquals(expected, stripped);
  }

  @Test
  public void stripDate()
  {
    Date datetime = DateUtil.newDate(2008, Calendar.JULY, 3, 22, 56, 32);
    Date expected = DateUtil.newDate(1970, Calendar.JANUARY, 1, 22, 56, 32);

    Date stripped = DateUtil.stripDate(datetime);

    assertEquals(expected, stripped);
  }

  @Test
  public void joinDateAndTime()
  {
    Date date = DateUtil.newDate(2008, Calendar.JULY, 3);
    Date time = DateUtil.newDate(1970, Calendar.JANUARY, 1, 22, 56, 32);
    Date expected = DateUtil.newDate(2008, Calendar.JULY, 3, 22, 56, 32);

    Date joined = DateUtil.joinDateAndTime(date, time);

    assertEquals(expected, joined);
  }
}
