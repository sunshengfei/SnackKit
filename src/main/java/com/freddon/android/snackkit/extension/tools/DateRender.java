package com.freddon.android.snackkit.extension.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by fred on 2017/1/14.
 */

public class DateRender {


    private static ThreadLocal<SimpleDateFormat> simpleDateFormatThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<SimpleDateFormat> simpleDateTimeFormatThreadLocal = new ThreadLocal<>();

    // region : @fred Period Time Render {时间段显示} [2017/1/14]

    /**
     * 刚刚 1~60分钟前  今天几点  昨天几点  年-月-日
     */

    public static CharSequence periodRenderer(long timeMills) {
        Calendar t1 = Calendar.getInstance();
        t1.setTimeInMillis(timeMills);
        return periodRenderer(t1, Calendar.getInstance());
    }

    public static CharSequence periodRenderer(String dateTime) {
        Calendar date = stringToCalendar(dateTime);
        if (date != null) {
            return periodRenderer(date, Calendar.getInstance());
        } else {
            return "";
        }
    }

    public static CharSequence periodRenderer(String timestamp, String currentTime) {
        return periodRenderer(stringToCalendar(timestamp), stringToCalendar(currentTime));
    }

    /**
     * 根据传入的时间戳,返回绝对时间差字符串
     *
     * @param timestamp   单位毫秒
     * @param currentTime 单位毫秒
     * @return 格式化的字符串
     */
    public static String periodRenderer(long timestamp, long currentTime) {
        Calendar t1 = Calendar.getInstance();
        Calendar t2 = Calendar.getInstance();
        t1.setTimeInMillis(timestamp);
        t2.setTimeInMillis(currentTime);
        return periodRenderer(t1, t2);
    }

    public static String periodRenderer(Calendar calendar, Calendar current) {
        String ret;
        Long sub = Math.abs(calendar.getTimeInMillis() - current.getTimeInMillis()) / 1000L;
        if (sub < 60) {
            ret = "刚刚";
            return ret;
        }
        if (sub < 3600) {
            ret = sub / 60 + "分钟前";
            return ret;
        }
        //今天几点
        if (isSameDay(calendar, current)) {
            ret = "今天 " + pad10ZeroStart(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + pad10ZeroStart(calendar.get(Calendar.MINUTE));
            return ret;
        }

        if (isBeforeADay(calendar, current)) {
            ret = "昨天 " + pad10ZeroStart(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + pad10ZeroStart(calendar.get(Calendar.MINUTE));
            return ret;
        }
        ret = calendar.get(Calendar.YEAR) + "-" + pad10ZeroStart(calendar.get(Calendar.MONTH) + 1) + "-" + pad10ZeroStart(calendar.get(Calendar.DAY_OF_MONTH));
        return ret;
    }


    public static String pad10ZeroStart(int number) {
        if (number < 10) {
            return "0" + number;
        }
        return String.valueOf(number);
    }


    // endregion


    /**
     * 是否为同一天
     *
     * @param cal
     * @param now
     * @return
     */
    public static boolean isSameDay(Calendar cal, Calendar now) {
        return cal.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
                (cal.get(Calendar.DAY_OF_YEAR) ^ now.get(Calendar.DAY_OF_YEAR)) == 0;
    }

    public static boolean isBeforeADay(Calendar cal, Calendar now) {
        if (cal.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
            return cal.get(Calendar.DAY_OF_YEAR) - now.get(Calendar.DAY_OF_YEAR) == -1;
        }
        return dayDiff(cal, now) == -1;
    }


    /**
     * 是否为今天
     *
     * @param cal
     * @return
     */
    public static boolean isToday(Calendar cal) {
        Calendar pre = Calendar.getInstance();
        return isSameDay(cal, pre);
    }

    /**
     * 是否是昨天
     *
     * @param cal
     * @return
     */
    public static boolean isYesterday(Calendar cal) {
        Calendar now = Calendar.getInstance();
        return isBeforeADay(cal, now);
    }


    public static long dayDiff(Calendar cal, Calendar now) {
        Calendar cCalendar = Calendar.getInstance();
        cCalendar.setTime(cal.getTime());
        cCalendar.set(Calendar.HOUR_OF_DAY, 0);
        cCalendar.set(Calendar.MINUTE, 0);
        cCalendar.set(Calendar.SECOND, 0);
        cCalendar.set(Calendar.MILLISECOND, 0);

        Calendar nCalendar = Calendar.getInstance();
        nCalendar.setTime(now.getTime());
        nCalendar.set(Calendar.HOUR_OF_DAY, 0);
        nCalendar.set(Calendar.MINUTE, 0);
        nCalendar.set(Calendar.SECOND, 0);
        nCalendar.set(Calendar.MILLISECOND, 0);
        return (cCalendar.getTimeInMillis() - nCalendar.getTimeInMillis()) / (24 * 3600 * 1000);
    }


    public static Calendar stringToCalendar(String formatTime) {
        SimpleDateFormat formatter = getDateTimeFormatterInstance();
        try {
            Calendar c = Calendar.getInstance();
            Date start = formatter.parse(formatTime.trim());
            c.setTime(start);
            return c;
        } catch (ParseException e) {
            return null;
        }
    }


    /**
     * 获取 SimpleDateFormat yyyy-MM-dd HH:mm:ss实例
     *
     * @return
     */
    public static SimpleDateFormat getDateTimeFormatterInstance() {
        if (simpleDateTimeFormatThreadLocal.get() == null) {
            simpleDateTimeFormatThreadLocal.set(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA));
        }
        return simpleDateTimeFormatThreadLocal.get();
    }

    /**
     * 获取 SimpleDateFormat yyyy-MM-dd实例
     *
     * @return
     */
    public static SimpleDateFormat getDateFormatterInstance() {
        if (simpleDateFormatThreadLocal.get() == null) {
            simpleDateFormatThreadLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return simpleDateFormatThreadLocal.get();
    }

}
