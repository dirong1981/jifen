package com.gljr.jifen.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

    /**
     * 获取当天的开始时间
     * @return Date
     */
    public static Date getNowDayStart(){
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();

    }

    /**
     * 获取当天的结束时间
     * @return Date
     */
    public static Date getNowDayEnd(){
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 获取几天后的时间结束时间
     * @param severalDays 几天后
     * @return Date
     */
    public static Date getSeveralDaysEnd(int severalDays){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, severalDays);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 获取几天前的时间结束时间
     * @param severalDays 几天前
     * @return Date
     */
    public static Date getSeveralDaysStart(int severalDays){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, - severalDays);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取某一天后的时间开始时间
     * @param date 时间
     * @return Date
     */
    public static Date getOneDayStart(Date date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取某一天后的时间结束时间
     * @param date 时间
     * @return Date
     */
    public static Date getOneDayEnd(Date date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 获取某一天后的时间结束时间
     * @param date 时间
     * @param days 前后多少天，正数往后，负数往前
     * @return Date
     */
    public static Date getRallDate(Date date, int days){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    /**
     * 将字符串时间转为date
     * @param dateStr 字符串时间
     * @return Date
     */
    public static Date formatToDate(String dateStr) {
        return formatToDate(dateStr, "yyyyMMdd");
    }

    public static Date formatToDate(String dateStr, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
        return date;
    }

    /**
     * 日期转换为字符串 格式自定义
     *
     * @param date 日期
     * @param f 日期格式
     * @return String
     */
    public static String formatToString(Date date, String f) {
        if (date == null || f == null) return "";
        SimpleDateFormat format = new SimpleDateFormat(f);
        String str = format.format(date);
        return str;
    }

    public static Date yesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTime();
    }

    public static boolean validate(String date, String format) {
        return formatToDate(date, format) != null;
    }

    public static long diffSeconds(Date past, Date future) {
        return (future.getTime() - past.getTime()) / 1000L;
    }
}
