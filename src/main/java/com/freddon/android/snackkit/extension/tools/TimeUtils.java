package com.freddon.android.snackkit.extension.tools;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class TimeUtils {
    public static final long MILLIS_IN_DAY = 1000L * 60 * 60 * 24;

    /**
     * 资讯发布时间（旧）
     *
     * @param Currentime 当前时间
     * @param time       发布时间
     *                   时间的显示方式为：刚刚（1分钟内）、n分钟前（1小时内）、n小时前（5小时内）、hh:mm（每天24点前）、yy.mm.dd（其余时间）
     */
    public static String FormatTime(long Currentime, long time) {
        String timeText;
        long diff = Currentime - time;
        long days = diff / (1000 * 60 * 60 * 24);
        long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
                * (1000 * 60 * 60))
                / (1000 * 60);
        if (isSameDay(time,Currentime)){
            if (hours >= 5) {
                timeText = LocalToMatchTime(time);
            } else if (hours >= 1) {
                timeText = hours + "小时前";
            } else if (minutes >= 1) {
                timeText = minutes + "分钟前";
            } else
                timeText = "刚刚";
        }else
            timeText = FormatTimeDate3(time);
        return timeText;
    }

    /**
     * 获取相差多少时间，天为单位
     *
     * @param Currentime 当前时间
     * @param time       比较时间
     * @return 1天
     */
    public static long FormatTimeByDay(long Currentime, long time) {
        long diff = Currentime - time;
        long days = diff / (1000 * 60 * 60 * 24);
        return days;
    }

    /**
     * 格式化时间
     *
     * @param Currentime 当前时间
     * @return yyyy年MM月dd日    HH:mm:ss
     */
    public static String FormatTimeWhole(long Currentime) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy年MM月dd日    HH:mm:ss");
        Date curDate = new Date(Currentime);// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 北京时间 yyyy/MM/dd
     *
     * @param Currentime 当前时间
     * @return yyyy/MM/dd
     */
    public static String FormatTimeDate(long Currentime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date curDate = new Date(Currentime);// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 北京时间 yyyy.MM.dd  HHmm
     */
    public static String FormatTimeDate2(long Currentime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd  HH:mm");
        Date curDate = new Date(Currentime);// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 按秒换算
     * @param Currentime
     */
    public static String FormatTimeDate2_seconds(long Currentime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd  HH:mm");
        Date curDate = new Date(Currentime * 1000);// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 北京时间 yyyy.MM.dd  HHmm
     */
    public static String FormatTimeDate3(long Currentime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
        Date curDate = new Date(Currentime);// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 北京时间 yyyy-MM-dd HH:mm:ss
     */
    public static String FormatTimeDate4(long Currentime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(Currentime);// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }
    /**
     * 北京时间 yyyy-MM-dd HH:mm:ss
     */
    public static String FormatTimeDate5(long Currentime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy - MM - dd");
        Date curDate = new Date(Currentime);// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }
    /**
     * 北京时间 yyyy-MM-dd HH:mm:ss
     */
    public static String FormatTimeDate6(long Currentime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
        Date curDate = new Date(Currentime);// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 格林威治 yyyyMMddHHmmss
     *
     * @param currentTime
     * @return
     */
    public static String LocalToGTM_ALL(long currentTime) {
        TimeZone timeZoneLondon = TimeZone.getTimeZone("GMT");
        SimpleDateFormat formatAll = new SimpleDateFormat("yyyyMMddHHmmss");
        formatAll.setTimeZone(timeZoneLondon);
        Date date = new Date(currentTime);
        String str = formatAll.format(date);
        return str;
    }

    /**
     * 格林威治yyyyMMdd
     */
    public static String LocalToGTM_PART(long currentTime) {
        TimeZone timeZoneLondon = TimeZone.getTimeZone("GMT");// 格林威治
        SimpleDateFormat formatAll = new SimpleDateFormat("yyyyMMdd");
        formatAll.setTimeZone(timeZoneLondon);
        Date date = new Date(currentTime);
        String str = formatAll.format(date);
        return str;
    }

    /**
     * 格林威治yyyyMM
     */
    public static String LocalToGTM_YEAR_MONTH(long currentTime) {
        TimeZone timeZoneLondon = TimeZone.getTimeZone("GMT");// 格林威治
        SimpleDateFormat formatAll = new SimpleDateFormat("yyyyMM");
        formatAll.setTimeZone(timeZoneLondon);
        Date date = new Date(currentTime);
        String str = formatAll.format(date);
        return str;
    }


    /**
     * HH:mm
     */
    public static String LocalToMatchTime(long currentTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date curDate = new Date(currentTime);// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }


    /**
     * MM.dd
     */
    public static String getDayAndMonth(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM.dd");
        Date date = new Date(time);
        return formatter.format(date);
    }

    /**
     * MM-dd
     */
    public static String getMonthAndDay(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
        Date date = new Date(time);
        return formatter.format(date);
    }

    /**
     * dd
     */
    public static String getDayOfMonth(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd");
        Date date = new Date(time);
        return formatter.format(date);
    }

    /**
     * aa hh:mm
     */
    public static String getTimeofDay(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("aa hh:mm");
        Date date = new Date(time);
        return formatter.format(date);
    }

    public static String getTimeOfMonth(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("M月d日aa hh:mm");
        Date date = new Date(time);
        return formatter.format(date);
    }

    /**
     * 根据某日的毫秒数,返回本周的星期几
     */
    public static String getDayOfWeek(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        Date date = new Date(time);
        return formatter.format(date).replace("星期", "周");
    }



    /**
     * 判断两个毫秒数是否为同一天
     */
    public static boolean isSameDay(long beforeTime, long afterTime) {
        long time = beforeTime - afterTime;
        time = Math.abs(time);
        return time < MILLIS_IN_DAY && toDay(beforeTime) == toDay(afterTime);
    }

    private static long toDay(long millis) {
        return (millis + TimeZone.getDefault().getOffset(millis)) / MILLIS_IN_DAY;
    }

    /**
     * 毫秒转化为时分秒
     *
     * @param time
     * @return
     */
    public static String timeMillisToHMS(long time) {
        int hour = (int) (time / (60 * 60 * 1000));
        int minute = (int) ((time - hour * 60 * 60 * 1000) / (60 * 1000));
        int second = (int) ((time - hour * 60 * 60 * 1000 - minute * 60 * 1000) / 1000);
        return String.format("%02d", hour) + ":"
                + String.format("%02d", minute) + ":"
                + String.format("%02d", second);
    }

    /**
     * 比赛开始时间距离现在还有多少时间
     *
     * @param time 比赛时间
     * @return 还需多少的时间
     */
    public static String getCountDownDay(long time) {
        String day = "";
        int days = (int) (time / (24 * 60 * 60 * 1000));
        switch (days) {
            case 0:
                day = timeMillisToHMS(time);
                break;
            case 1:
                day = "一天后";
                break;
            case 2:
                day = "二天后";
                break;
            case 3:
                day = "三天后";
                break;
            case 4:
                day = "四天后";
                break;
            case 5:
                day = "五天后";
                break;
            case 6:
                day = "六天后";
                break;
            case 7:
                day = "七天后";
                break;
        }
        return day;
    }

    /**
     * 根据毫秒数返回当前年份
     *
     * @param time 当前时间
     * @return 格式2016
     */
    public static String getYear(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        Date date = new Date(time);
        return formatter.format(date);
    }


    /**
     * 是否超过30s
     *
     * @param before
     * @param after
     * @return
     */
    public static boolean intervalTimeIsLong(long before, long after) {
        long intervalTime = before - after;
        if (intervalTime < 0L) {
            intervalTime = -intervalTime;
        }
        return intervalTime < 60000L;
    }

    public static String getTomorrowDate() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, 1);
        java.text.SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        String time = sdf.format(now.getTime());
        return time;
    }

    public static String getTomorrowDay() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, 1);
        String mWay = String.valueOf(now.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return "星期" + mWay;
    }

}
