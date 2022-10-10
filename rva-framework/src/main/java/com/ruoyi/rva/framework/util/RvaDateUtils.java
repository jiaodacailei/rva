package com.ruoyi.rva.framework.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author cailei
 * @version 1.0
 */
@Component
@Slf4j
public class RvaDateUtils {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println(parseDateTime("2021-05-28 11:01:02"));
    }

    /**
     * @param val
     * @return
     */
    public static Boolean isDate(Object val) {
        if (val == null) {
            return false;
        }
        return Pattern.matches(dateRegex, val.toString());
    }

    private static final String dateRegex = "([1-9]\\d\\d\\d)-([0-1]?\\d)-([0-3]?\\d)";

    private static final String datetimeRegex = dateRegex + " ([0-2]?\\d):([0-5]?\\d):([0-5]?\\d)";

    /**
     * @param val
     * @return
     */
    public static Boolean isDatetime(Object val) {
        if (val == null) {
            return false;
        }
        if (isDate(val)) {
            return true;
        }
        return Pattern.matches(datetimeRegex, val.toString());
    }

    /**
     * @param weekDay 周日-周六分别为： 1-7
     * @param suffix  format like : '12:30:30'
     * @return format like : '2009-01-09 12:30:30'
     */
    public static String getDatetimeInCurrentWeek(int weekDay, String suffix) {
        Calendar cal = Calendar.getInstance();
        int nowWeekDay = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, weekDay - nowWeekDay);
        return getYMD(cal) + " " + suffix;
    }

    /**
     * 将微信格式时间2021-06-24T16:23:17+08:00，转化为2021-06-24 16:23:17
     * @return
     */
    public static String getDatetimeByWx (String wxDatetime) {
        if (RvaUtils.isEmpty(wxDatetime)) {
            return null;
        }
        String[] strings = wxDatetime.split("[T+]");
        if (strings.length != 3) {
            throw new RuntimeException(String.format(RvaConstants.MSG_FORMAT, "日期时间", "2021-06-24T16:23:17+08:00"));
        }
        return strings[0] + " " + strings[1];
    }

    /**
     * @param date
     * @return
     */
    public static String getDatetime(Object date) {
        if (date == null) {
            return null;
        }
        if (date instanceof String) {
            return (String) date;
        }
        Calendar cal = Calendar.getInstance();
        if (date instanceof Date) {
            cal.setTime(((Date) date));
        } else if (date instanceof Long) {
            cal.setTimeInMillis(((Long) date));
        } else if (date instanceof Timestamp) {
            cal.setTimeInMillis(((Timestamp) date).getTime());
        } else if (date instanceof Calendar) {
            cal = (Calendar) date;
        }
        return getYMD(cal) + " " + getHMS(cal);
    }

    /**
     * @param dtStr 日期时间字符串，格式：2015-10-12 10:30:00.
     * @param min   分钟数.
     * @return
     */
    public static String getDatetimeAfterMinute(String dtStr, Integer min) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(parseDateTime(dtStr));
        cal.add(Calendar.MINUTE, min);
        return getDatetime(cal);
    }

    /**
     * @param min 分钟数.
     * @return
     */
    public static String getDatetimeAfterMinute(Integer min) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, min);
        return getDatetime(cal);
    }

    /**
     * @param day
     * @return
     */
    public static String getDatetimeAfterDay(Integer day) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, day);
        return getDatetime(cal);
    }

    /**
     * @param week
     * @return
     */
    public static String getDatetimeAfterWeek(Integer week) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR, week);
        return getDatetime(cal);
    }

    /**
     * @param mon
     * @return
     */
    public static String getDatetimeAfterMonth(Integer mon) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, mon);
        return getDatetime(cal);
    }

    /**
     * 获取当前日期和时间.
     *
     * @return format like : '2009-01-09 12:30:30'
     */
    public static String getNow() {
        Calendar cal = Calendar.getInstance();
        return getYMD(cal) + " " + getHMS(cal);
    }

    /**
     * @return format like : '20090109123030'
     */
    public static String getNow2() {
        Calendar cal = Calendar.getInstance();
        return "" + cal.get(Calendar.YEAR) + getDoubleInt(cal.get(Calendar.MONTH) + 1)
                + getDoubleInt(cal.get(Calendar.DAY_OF_MONTH)) + getDoubleInt(cal.get(Calendar.HOUR_OF_DAY))
                + getDoubleInt(cal.get(Calendar.MINUTE)) + getDoubleInt(cal.get(Calendar.SECOND));
    }

    /**
     * @return format like : '2009-01-09'
     */
    public static String getYMD(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return getYMD(cal);
    }

    /**
     * 返回当前的日期，格式：2010-09-07.
     *
     * @return
     */
    public static String getYMD() {
        Calendar cal = Calendar.getInstance();
        return getYMD(cal);
    }

    /**
     * @param day
     * @return
     */
    public static String getYMDAfterDay(Integer day) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, day);
        return getYMD(cal);
    }

    /**
     * 2010-09-07
     *
     * @return
     */
    public static String getYMD(Calendar cal) {
        String re = "" + cal.get(Calendar.YEAR) + "-" + getDoubleInt((cal.get(Calendar.MONTH) + 1)) + "-"
                + getDoubleInt((cal.get(Calendar.DAY_OF_MONTH)));
        return re;
    }

    /**
     * 返回Calendar所在月的第一天的日期，例如：2009-01-01.
     *
     * @param cal
     * @return
     */
    public static String getYMDFirstOfMonth(Calendar cal) {
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return getYMD(cal);
    }

    /**
     * 返回当前月的第一天的日期，例如：2009-01-01.
     *
     * @return
     */
    public static String getYMDFirstOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return getYMD(cal);
    }

    /**
     * 返回Calendar所在月的最后一天的日期，例如：2009-01-31.
     *
     * @param cal
     * @return
     */
    public static String getYMDLastOfMonth(Calendar cal) {
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return getYMD(cal);
    }

    /**
     * 返回当前月的最后一天的日期，例如：2009-01-31.
     *
     * @return
     */
    public static String getYMDLastOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return getYMD(cal);
    }

    /**
     * 201009
     *
     * @return
     */
    public static String getYearMonth() {
        return getYearMonth(Calendar.getInstance());
    }

    /**
     * @param cal
     * @return
     */
    public static String getYearMonth(Calendar cal) {
        String currentMonth = "" + cal.get(Calendar.YEAR) + RvaUtils.generateFixedLenNo(2, (cal.get(Calendar.MONTH) + 1));
        return currentMonth;
    }

    /**
     * @param date
     * @return
     */
    public static String getYearMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getYearMonth(calendar);
    }

    /**
     * 2010
     *
     * @return
     */
    public static String getYear() {
        Calendar cal = Calendar.getInstance();
        return String.valueOf(cal.get(Calendar.YEAR));
    }

    /**
     * 11
     *
     * @return
     */
    public static String getMonth() {
        Calendar cal = Calendar.getInstance();
        return String.valueOf(cal.get(Calendar.MONTH) + 1);
    }

    /**
     * 2
     *
     * @return
     */
    public static String getWeekOfMonth() {
        Calendar cal = Calendar.getInstance();
        return String.valueOf(cal.get(Calendar.WEEK_OF_MONTH));
    }

    /**
     * 获取当前时间.
     *
     * @return format like : '12:30:30'
     */
    public static String getHMS() {
        Calendar cal = Calendar.getInstance();
        return getHMS(cal);
    }

    /**
     * 获取min分钟后的时间.
     *
     * @return format like : '12:30:30'
     */
    public static String getHMSAfterMinute(int min) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, min);
        return getHMS(cal);
    }

    /**
     * 获取时分秒，格式：'12:30:30'.
     *
     * @return format like : '12:30:30'
     */
    public static String getHMS(Calendar cal) {
        return "" + getDoubleInt(cal.get(Calendar.HOUR_OF_DAY)) + ":" + getDoubleInt(cal.get(Calendar.MINUTE)) + ":"
                + getDoubleInt(cal.get(Calendar.SECOND));
    }

    /**
     * @param n
     * @return
     */
    private static String getDoubleInt(int n) {
        return RvaUtils.generateFixedLenNo(2, n);
    }

    /**
     * @param dateStr
     * @return
     */
    public static Date parseDateTime(String dateStr) {
        Matcher matcher = Pattern.compile(datetimeRegex).matcher(dateStr);
        if (matcher.find()) {
            Calendar instance = Calendar.getInstance();
            instance.set(Integer.valueOf(matcher.group(1)), Integer.valueOf(matcher.group(2)) - 1, Integer.valueOf(matcher.group(3)), Integer.valueOf(matcher.group(4)), Integer.valueOf(matcher.group(5)), Integer.valueOf(matcher.group(6)));
            return instance.getTime();
        }
        throw new RuntimeException(String.format(RvaConstants.MSG_PARSE, dateStr));
    }

    /**
     * @param dateStr
     * @return
     */
    public static Date parseDate(String dateStr) {
        Matcher matcher = Pattern.compile(datetimeRegex).matcher(dateStr);
        if (matcher.find()) {
            Calendar instance = Calendar.getInstance();
            instance.set(Integer.valueOf(matcher.group(1)), Integer.valueOf(matcher.group(2)), Integer.valueOf(matcher.group(3)));
            return instance.getTime();
        }
        throw new RuntimeException(String.format(RvaConstants.MSG_PARSE, dateStr));
    }

    /**
     * 将数据库中datetime字段值，转化为标准格式.
     *
     * @param sqlVal format:2009-03-23 23:02:23.0
     * @return 2009-03-23 23:02:23
     */
    public static String formatSqlValue(String sqlVal) {
        if (sqlVal != null) {
            int i = sqlVal.indexOf(".");
            if (i > 0) {
                return sqlVal.substring(0, i);
            }
        }
        return null;
    }

    /**
     * @param dt1
     * @param dt2
     * @return the value 0 if the time represented by the argument is equal to
     * the time represented by this Calendar; a value less than 0 if the
     * time of this Calendar is before the time represented by the
     * argument; and a value greater than 0 if the time of this Calendar
     * is after the time represented by the argument.
     */
    public static int compare(String dt1, String dt2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(parseDateTime(dt1));
        Calendar c2 = Calendar.getInstance();
        c2.setTime(parseDateTime(dt2));
        return c1.compareTo(c2);
    }

    /**
     * 两个日期时间相减的值，单位毫秒.
     *
     * @param dt1
     * @param dt2
     * @return
     */
    public static long subtract(String dt1, String dt2) {
        return parseDateTime(dt1).getTime() - parseDateTime(dt2).getTime();
    }

    /**
     * 两个日期时间相减的值，单位小时.
     *
     * @param dt1
     * @param dt2
     * @return
     */
    public static double subtractByHour(String dt1, String dt2) {
        return subtract(dt1, dt2) / 3600000.0;
    }

    /**
     * 两个日期时间相减的值，单位天.
     *
     * @param dt1
     * @param dt2
     * @return
     */
    public static double subtractByDay(String dt1, String dt2) {
        return subtract(dt1, dt2) / 3600000.0 / 24;
    }

    /**
     * @param date format like : '2012/5/8'
     * @return format like : '2012-5-8'
     */
    public static String getDateByFormat(String date) {
        if (RvaUtils.isEmpty(date)) {
            throw new RuntimeException(String.format(RvaConstants.MSG_REQUIRED, "时间"));
        }
        return date.replace('/', '-');
    }

    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
}
