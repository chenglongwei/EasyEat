package com.easyeat.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by chenglongwei on 12/5/16.
 */

public class Util {
    /**
     * Pass your date format and no of days for minus from current
     * If you want to get previous date then pass days with minus sign
     * else you can pass as it is for next date
     *
     * @param dateFormat
     * @param days
     * @return Calculated Date
     */
    public static String getCalculatedDate(String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat, Locale.US);
        cal.add(Calendar.DAY_OF_YEAR, days);
        return s.format(new Date(cal.getTimeInMillis()));
    }
}
