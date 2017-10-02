package com.gljr.jifen.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static Date getTime() {
        Date date = new Date();
        return date;
    }

    public static String getTimeStr(Date date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String getTimeStr(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static long getShortTimeStamp() {
        return (new Date().getTime() - 1485619199999L) / 1000L;
    }
}
