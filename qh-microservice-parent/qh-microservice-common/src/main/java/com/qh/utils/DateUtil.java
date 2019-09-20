package com.qh.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期工具类
 */

public class DateUtil {

    public static final List<String> FORMARTS = new ArrayList<>(4);

    static {
        FORMARTS.add("yyyy-MM");
        FORMARTS.add("yyyy-MM-dd");
        FORMARTS.add("yyyy-MM-dd HH:mm");
        FORMARTS.add("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 字符串转Date
     * @param source
     * @return
     */
    public static Date convert(String source) {
        String value = source.trim();
        if ("".equals(value)) {
            return null;
        }
        if (source.matches("^\\d{4}-\\d{1,2}$")) {
            return parseDate(source, FORMARTS.get(0));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            return parseDate(source, FORMARTS.get(1));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
            return parseDate(source, FORMARTS.get(2));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            return parseDate(source, FORMARTS.get(3));
        } else {
            throw new IllegalArgumentException("Invalid boolean value '" + source + "'");
        }

    }
    public static Date parseDate(String dateStr, String format) {
        Date date = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            date = dateFormat.parse(dateStr);
        } catch (Exception e) {
        }
        return date;
    }

    /**
     * 日期格式化 yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static String DateToStr(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);
        return str;
    }

    public static String DateToStr(Date date, String style) {

        SimpleDateFormat format = new SimpleDateFormat(style);
        String str = format.format(date);
        return str;
    }

    public static String getDay(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String str = format.format(date);
        return str;
    }

    public static String getDayNumber(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String str = format.format(date);
        return str;
    }

    public static String getTimeNumber(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("HHmmss");
        String str = format.format(date);
        return str;
    }

    public static String getTime(String style) {
        SimpleDateFormat sdf = new SimpleDateFormat(style);
        return sdf.format(new Date());
    }

    public static String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    public static String getBeginDate(String date, String style) {

        SimpleDateFormat sdf = new SimpleDateFormat(style);
        Calendar c = Calendar.getInstance();
        try {
            Date d = sdf.parse(date);
            c.setTime(d);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        return year + "-" + month + "-01";
    }

    public static String getEndDate(String date, String style) {
        SimpleDateFormat sdf = new SimpleDateFormat(style);
        Calendar c = Calendar.getInstance();
        try {
            Date d = sdf.parse(date);
            c.setTime(d);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = getDate(year, month);
        return year + "-" + month + "-" + day;
    }

    public static Integer getDate(int year, int month) {
        int days = 0;

        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                days = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                days = 30;
                break;
            case 2:
                if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
                    days = 29;
                else
                    days = 28;
                break;
        }
        return days;
    }

    public static String getNowDate(String style) {
        SimpleDateFormat sdf = new SimpleDateFormat(style);
        return sdf.format(new Date());
    }

    public static String getAYearAgoDate(String style) {
        SimpleDateFormat sdf = new SimpleDateFormat(style);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, -1);
        return sdf.format(c.getTime());
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.dateSumOrSub(new Date(), 6, true));
    }

    /**
     * @param date  日期
     * @param count 加减天数
     * @param flag  true为加 false为减
     * @return
     */
    public static String dateSumOrSub(Date date, int count, boolean flag) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (flag) {
            return df.format(new Date(date.getTime() + count * 24 * 60 * 60 * 1000L));
        } else {
            return df.format(new Date(date.getTime() - count * 24 * 60 * 60 * 1000L));
        }
    }

    /**
     * 时间想减 获取相差时间
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int dateSubMinute(Date date1, Date date2) {

        long s = date2.getTime() - date1.getTime();
        int d = (int) (s / 1000 / 60);

        return d;
    }

    /**
     * 时间相减，返回天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int dateday(Date date1, Date date2) {

        long s = date2.getTime() - date1.getTime();
        int d = (int) (s / 1000 / 60 / 60 / 24);

        return d;
    }

    public static Date StringToDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = sdf.parse(date);
            return date1;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static Date StringToDate(String date, String style) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(style);
            Date date1 = sdf.parse(date);
            return date1;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取一周前的时间
     *
     * @param style
     * @return
     */
    public static String getAWEEkAgoDate(String style) {
        SimpleDateFormat sdf = new SimpleDateFormat(style);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.WEEK_OF_YEAR, -1);
        return sdf.format(c.getTime());
    }

    /**
     * 获取一周后的时间
     *
     * @param style
     * @return
     */
    public static String getAWEEkAgoDate2(String style) {
        SimpleDateFormat sdf = new SimpleDateFormat(style);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.WEEK_OF_YEAR, 1);
        return sdf.format(c.getTime());
    }

    /**
     * 日期转星期
     *
     * @param datetime
     * @return
     */
    public static String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = {"0", "1", "2", "3", "4", "5", "6"};
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 本月最后一天
     *
     * @return
     */
    public static String Mlastdata(SimpleDateFormat format) {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = format.format(ca.getTime());
        return last;

    }

    /**
     * 本月第一天
     *
     * @return
     */
    public static String Mfirstdata(SimpleDateFormat format) {
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.MONTH, 0);
        ca.set(Calendar.DAY_OF_MONTH, 1);
        String last = format.format(ca.getTime());
        return last;

    }

    /**
     * 两个时间段相差月数
     *
     * @param strdata1
     * @param strdata2
     * @param format
     * @return
     */
    public static int sectiontimeM(String strdata1, String strdata2, SimpleDateFormat format) {
        Calendar bef = Calendar.getInstance();
        Calendar aft = Calendar.getInstance();
        try {
            bef.setTime(format.parse(strdata1));
            aft.setTime(format.parse(strdata2));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int result = aft.get(Calendar.MONTH) - bef.get(Calendar.MONTH);
        int month = (aft.get(Calendar.YEAR) - bef.get(Calendar.YEAR)) * 12;
        return result + month;

    }

    /**
     * 获取多少天前的日期
     *
     * @param s
     * @return
     */
    public static Date Futuretime(String s) {
        Date dt = new Date();
        Calendar cd = Calendar.getInstance();

        cd.setTime(dt);
        cd.add(Calendar.DATE, -Integer.parseInt(s));
        dt = cd.getTime();

        return dt;

    }

    /**
     * 多少天后的时间
     *
     * @param s
     * @return
     */
    public static String getaftertime(String s, String time, SimpleDateFormat format) {
        Date dt = null;
        try {
            dt = format.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Calendar cd = Calendar.getInstance();

        cd.setTime(dt);
        cd.add(Calendar.DATE, Integer.parseInt(s));
        dt = cd.getTime();

        return format.format(dt);

    }


    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String getDate() {
        return getDate("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String formatDate(Date date, Object... pattern) {
        String formatDate = null;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    /**
     * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }


    /**
     * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String getDateTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当前年份字符串 格式（yyyy）
     */
    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    /**
     * 得到当前月份字符串 格式（MM）
     */
    public static String getMonth() {
        return formatDate(new Date(), "MM");
    }

    /**
     * 得到当天字符串 格式（dd）
     */
    public static String getDay() {
        return formatDate(new Date(), "dd");
    }

    /**
     * 得到当前星期字符串 格式（E）星期几
     */
    public static String getWeek() {
        return formatDate(new Date(), "E");
    }

    /**
     * 日期型字符串转化为日期 格式 { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
     * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy.MM.dd",
     * "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
     */

    /**
     * 获取过去的天数
     *
     * @param date
     * @return
     */
    public static long pastDays(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (24 * 60 * 60 * 1000);
    }

    /**
     * 获取过去的小时
     *
     * @param date
     * @return
     */
    public static long pastHour(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (60 * 60 * 1000);
    }

    /**
     * 获取过去的分钟
     *
     * @param date
     * @return
     */
    public static long pastMinutes(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (60 * 1000);
    }

    /**
     * 转换为时间（天,时:分:秒.毫秒）
     *
     * @param timeMillis
     * @return
     */
    public static String formatDateTime(long timeMillis) {
        long day = timeMillis / (24 * 60 * 60 * 1000);
        long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
        long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
        return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
    }

    /**
     * 获取两个日期之间的天数
     *
     * @param before
     * @param after
     * @return
     */
    public static double getDistanceOfTwoDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
    }

    /**
     * 把日期字符串格式化成日期类型
     *
     * @param dateStr
     * @param format
     * @return
     */
    public static Date convert2Date(String dateStr, String format) {
        SimpleDateFormat simple = new SimpleDateFormat(format);
        try {
            simple.setLenient(false);
            return simple.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 把日期类型格式化成字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String convert2String(Date date, String format) {
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            return formater.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 转sql的time格式
     *
     * @param date
     * @return
     */
    public static java.sql.Timestamp convertSqlTime(Date date) {
        java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
        return timestamp;
    }

    /**
     * 转sql的日期格式
     *
     * @param date
     * @return
     */
    public static java.sql.Date convertSqlDate(Date date) {
        java.sql.Date Datetamp = new java.sql.Date(date.getTime());
        return Datetamp;
    }

    /**
     * 获取当前日期
     *
     * @param format
     * @return
     */
    public static String getCurrentDate(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }

    /**
     * 获取时间戳
     *
     * @return
     */
    public static long getTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * 获取月份的天数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getDaysOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取日期的年
     *
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取日期的月
     *
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取日期的日
     *
     * @param date
     * @return
     */


    /**
     * 获取日期的时
     *
     * @param date
     * @return
     */
    public static int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR);
    }

    /**
     * 获取日期的分种
     *
     * @param date
     * @return
     */
    public static int getMinute(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 获取日期的秒
     *
     * @param date
     * @return
     */
    public static int getSecond(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.SECOND);
    }

    /**
     * 获取星期几
     *
     * @param date
     * @return
     */
    public static int getWeekDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek - 1;
    }

    /**
     * 获取哪一年共有多少周
     *
     * @param year
     * @return
     */
    public static int getMaxWeekNumOfYear(int year) {
        Calendar c = new GregorianCalendar();
        c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
        return getWeekNumOfYear(c.getTime());
    }

    /**
     * 取得某天是一年中的多少周
     *
     * @param date
     * @return
     */
    public static int getWeekNumOfYear(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setMinimalDaysInFirstWeek(7);
        c.setTime(date);
        return c.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 取得某天所在周的第一天
     *
     * @param year
     * @return
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
        return c.getTime();
    }

    /**
     * 取得某天所在周的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6);
        return c.getTime();
    }

    /**
     * 取得某年某周的第一天 对于交叉:2008-12-29到2009-01-04属于2008年的最后一周,2009-01-05为2009年第一周的第一天
     *
     * @param year
     * @param week
     * @return
     */
    public static Date getFirstDayOfWeek(int year, int week) {
        Calendar calFirst = Calendar.getInstance();
        calFirst.set(year, 0, 7);
        Date firstDate = getFirstDayOfWeek(calFirst.getTime());

        Calendar firstDateCal = Calendar.getInstance();
        firstDateCal.setTime(firstDate);

        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, firstDateCal.get(Calendar.DATE));

        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, (week - 1) * 7);
        firstDate = getFirstDayOfWeek(cal.getTime());

        return firstDate;
    }

    /**
     * 取得某年某周的最后一天 对于交叉:2008-12-29到2009-01-04属于2008年的最后一周, 2009-01-04为
     * 2008年最后一周的最后一天
     *
     * @param year
     * @param week
     * @return
     */
    public static Date getLastDayOfWeek(int year, int week) {
        Calendar calLast = Calendar.getInstance();
        calLast.set(year, 0, 7);
        Date firstDate = getLastDayOfWeek(calLast.getTime());

        Calendar firstDateCal = Calendar.getInstance();
        firstDateCal.setTime(firstDate);

        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, firstDateCal.get(Calendar.DATE));

        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, (week - 1) * 7);
        Date lastDate = getLastDayOfWeek(cal.getTime());

        return lastDate;
    }

    /*
        * 1则代表的是对年份操作， 2是对月份操作， 3是对星期操作， 5是对日期操作， 11是对小时操作， 12是对分钟操作， 13是对秒操作， 14是对毫秒操作
        */
    public static Date add(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(calendarField, amount);
            return c.getTime();
        }
    }

   /*
    * 1则代表的是对年份操作， 2是对月份操作， 3是对星期操作， 5是对日期操作， 11是对小时操作， 12是对分钟操作， 13是对秒操作， 14是对毫秒操作
    */

    /**
     * 增加年
     *
     * @param date
     * @param amount
     * @return
     */
    public static Date addYears(Date date, int amount) {
        return add(date, 1, amount);
    }

    /**
     * 增加月
     *
     * @param date
     * @param amount
     * @return
     */
    public static Date addMonths(Date date, int amount) {
        return add(date, 2, amount);
    }

    /**
     * 增加周
     *
     * @param date
     * @param amount
     * @return
     */
    public static Date addWeeks(Date date, int amount) {
        return add(date, 3, amount);
    }

    /**
     * 增加天
     *
     * @param date
     * @param amount
     * @return
     */
    public static Date addDays(Date date, int amount) {
        return add(date, 5, amount);
    }

    /**
     * 增加时
     *
     * @param date
     * @param amount
     * @return
     */
    public static Date addHours(Date date, int amount) {
        return add(date, 11, amount);
    }

    /**
     * 增加分
     *
     * @param date
     * @param amount
     * @return
     */
    public static Date addMinutes(Date date, int amount) {
        return add(date, 12, amount);
    }

    /**
     * 增加秒
     *
     * @param date
     * @param amount
     * @return
     */
    public static Date addSeconds(Date date, int amount) {
        return add(date, 13, amount);
    }

    /**
     * 增加毫秒
     *
     * @param date
     * @param amount
     * @return
     */
    public static Date addMilliseconds(Date date, int amount) {
        return add(date, 14, amount);
    }

    /**
     * time差
     *
     * @param before
     * @param after
     * @return
     */
    public static long diffTimes(Date before, Date after) {
        return after.getTime() - before.getTime();
    }

    /**
     * 秒差
     *
     * @param before
     * @param after
     * @return
     */
    public static long diffSecond(Date before, Date after) {
        return (after.getTime() - before.getTime()) / 1000;
    }

    /**
     * 分种差
     *
     * @param before
     * @param after
     * @return
     */
    public static int diffMinute(Date before, Date after) {
        return (int) (after.getTime() - before.getTime()) / 1000 / 60;
    }

    /**
     * 时差
     *
     * @param before
     * @param after
     * @return
     */
    public static int diffHour(Date before, Date after) {
        return (int) (after.getTime() - before.getTime()) / 1000 / 60 / 60;
    }

    /**
     * 天数差
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int diffDay(Date before, Date after) {
        return Integer.parseInt(String.valueOf(((after.getTime() - before.getTime()) / 86400000)));
    }

    /**
     * 月差
     *
     * @param before
     * @param after
     * @return
     */
    public static int diffMonth(Date before, Date after) {
        int monthAll = 0;
        int yearsX = diffYear(before, after);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(before);
        c2.setTime(after);
        int monthsX = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
        monthAll = yearsX * 12 + monthsX;
        int daysX = c2.get(Calendar.DATE) - c1.get(Calendar.DATE);
        if (daysX > 0) {
            monthAll = monthAll + 1;
        }
        return monthAll;
    }

    /**
     * 年差
     *
     * @param before
     * @param after
     * @return
     */
    public static int diffYear(Date before, Date after) {
        return getYear(after) - getYear(before);
    }

    /**
     * 设置23:59:59
     *
     * @param date
     * @return
     */
    public static Date setEndDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 设置00:00:00
     *
     * @param date
     * @return
     */
    public static Date setStartDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        return calendar.getTime();
    }

    /**
     * 英文全称 如：2010-12-01 23:15:06
     */
    public static String FORMAT_LONG = "yyyy-MM-dd HH:mm:ss";

    /**
     * 使用预设格式格式化日期
     *
     * @param date 日期
     * @return 格式格式化日期
     */
    public static String format(Date date) {

        return format(date, getDatePattern());
    }

    /**
     * 获得默认的 date pattern
     *
     * @return 日期格式
     */
    public static String getDatePattern() {

        return FORMAT_LONG;
    }

    /**
     * 使用用户格式格式化日期
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return 日期格式
     */
    public static String format(Date date, String pattern) {

        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return (returnValue);
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     * String 转换成 Date 类型
     *
     * @param str
     * @return
     */
    public static Date StringDate(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * yyyy-MM-dd HH:mm:ss
     * Date 转换成 String 类型
     *
     * @param str
     * @return
     */
    public static String DateString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = sdf.format(date);
        return str;
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     * String  转换成 String
     *
     * @param str
     * @return
     */
    public static String String3Date(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String format = sdf.format(date);
        return format;
    }

    /**
     * String  转换成 String   年月日
     *
     * @param str
     * @return
     */
    public static String String4Date(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String format = sdf.format(date);
        return format;
    }

    /**
     * 得到 当前的 年-月-日 时间
     * 2017-12-12
     */
    public static String getInteger() {
        Calendar now = Calendar.getInstance();
        int hst_year = now.get(Calendar.YEAR);   //年份
        String year = String.valueOf(hst_year);
        /**
         * 默认0 ~ 11
         */
        int hst_Month = now.get(Calendar.MONTH) + 1; // 获取系统当前时间 月份
        int hst_date = now.get(Calendar.DATE);  //当前日期
        String month = null;
        String date = null;
        if (hst_Month < 10) {
            month = "0" + String.valueOf(+hst_Month);
        } else {
            month = String.valueOf(hst_Month);
        }
        if (hst_date < 10) {
            date = "0" + String.valueOf(+hst_date);
        } else {
            date = String.valueOf(hst_date);
        }
        System.err.println(month + "--" + date);
        String dateString = year + "-" + month + "-" + date;  //  日期
        return dateString;
    }

    /**
     * 得到 当前的  年月  时间
     * 201712
     */
    public static Integer getInteger1() {
        Calendar now = Calendar.getInstance();
        int hst_year = now.get(Calendar.YEAR);   //年份
        /**
         * 默认0 ~ 11
         */
        int hst_Month = now.get(Calendar.MONTH) + 1; // 获取系统当前时间 月份
        String month = null;
        if (hst_Month < 10) {
            month = String.valueOf("0" + hst_Month);
        } else {
            month = String.valueOf(hst_Month);
        }
        String year = String.valueOf(hst_year);
        Integer dateString = Integer.parseInt(year + month);  //  日期
        return dateString;
    }
}



