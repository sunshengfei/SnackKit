package com.freddon.android.snackkit.extension.regex;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;

/**
 * Created by fred on 2016/11/1.
 */

public class RegexHelper {


    public static String replaceSpace(String str) {
        if (str != null) {
            return str.replaceAll("\\s", "");
        }
        return str;
    }


    public static String maskPhone(String str) {
        if (isEmpty(str)) return null;
        if (str.length() != 11) return str;
        return str.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }


    /**
     * 验证Host
     *
     * @param str
     * @return
     */
    public static boolean isHost(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return str.matches("[0-9a-zA-Z\\.]+[a-zA-Z]+");
    }

    /**
     * 验证IP
     *
     * @param str
     * @return
     */
    public static boolean isIP(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return str.matches("([0-1]?\\d{1,2}|2[0-4]\\d|25[0-5])\\.([0-1]?\\d{1,2}|2[0-4]\\d|25[0-5])\\.([0-1]?\\d{1,2}|2[0-4]\\d|25[0-5])\\.([0-1]?\\d{1,2}|2[0-4]\\d|25[0-5])");
    }


    /**
     * 验证包含字母数字
     *
     * @param str
     * @return
     */
    public static boolean email(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return str.matches("[0-9a-zA-Z\\.]*[0-9a-zA-Z]+@[0-9a-zA-Z]+[\\.a-zA-Z]+");
    }


    /**
     * 长度判定
     *
     * @param str
     * @param minCount
     * @param maxCount
     * @return
     */
    public static boolean strLen(String str, int minCount, int maxCount) {
        if (str == null) {
            return false;
        }
        if (minCount > maxCount) return false;
        return str.length() >= minCount && str.length() <= maxCount;
    }


    /**
     * 判断两组字符串是否相等
     *
     * @param l
     * @param r
     * @return
     */
    public static boolean eq(String l, String r) {
        if (r == null && l == null) {
            return true;
        }
        if (null != r && null != l) {
            return r.equals(l);
        } else {
            return false;
        }
    }

    /**
     * 密码判定
     *
     * @param str
     * @return
     */
    public static boolean password(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return str.matches("[0-9a-zA-Z@\\-_\\.!]+");
    }


    /**
     * 验证包含字母数字
     *
     * @param str
     * @return
     */
    public static boolean alpha(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return str.matches("[0-9a-zA-Z]+");
    }

    /**
     * @param str
     * @return
     */
    public static boolean alpha_undlerline(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return str.matches("[0-9a-zA-Z_]+");
    }


    /**
     * 验证手机号码格式是否正确，只验证以1开头的11位数字
     *
     * @param phone
     * @return
     */
    public static boolean phoneValidate(String phone) {
        if (phone == null) {
            return false;
        }

        return phone.matches("^1\\d{10}");
    }


    /**
     * 验证是否位数字
     * 支持科学技术法
     * by fred
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        if (isEmpty(str)) {
            return false;
        }
        if (TextUtils.isDigitsOnly(str)) {
            return true;
        }
        boolean isNumber = str.matches("\\d+(\\.\\d*)?");
        if (isNumber) {
            return isNumber;
        }
        //科学计数法
        //形如 1000 1,133 0,133 0.133 13,133.133,133
        isNumber = str.matches("(\\d{1,3}(,\\d{3})*)?(\\.(\\d{1,3}|(\\d{3},)*\\d{1,3}))?");
        if (isNumber) {
            return isNumber;
        }
        return false;
    }

    public static boolean isSerialNumber(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return str.matches("^\\d+$");
    }


    /**
     * 判断空
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> boolean isEmpty(T t) {
        if (t == null) return true;
        if (t instanceof String) {
            return "".equals(t);
        }
        if (t instanceof CharSequence) {
            return TextUtils.isEmpty((CharSequence) t);
        }
        if (t instanceof Map) {
            Map map = (Map) t;
            return map.entrySet().size() == 0;
        }
        if (t instanceof Iterable) {
            return !((Iterable) t).iterator().hasNext();
        }
        if (t instanceof Object[]) {
            return ((Object[]) t).length == 0;
        }
        return false;
    }

    public static <T> T isEmptyElse(T t, T defaultValue) {
        return isEmpty(t) ? defaultValue : t;
    }

    public static <T> boolean isNotEmpty(T t) {
        return !isEmpty(t);
    }


    public static String reactiveAmountHans(String amountStr) {
        if (!RegexHelper.isNumber(amountStr)) return "请输入正确金额";
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        DecimalFormat decimalNumFormat = new DecimalFormat("#");
        BigDecimal amount = new BigDecimal(amountStr);
        Double n = 0D;
        Long num = 0L;
        try {

            n = Double.parseDouble(decimalFormat.format(amount).replaceAll(",", ""));
            if (Double.isNaN(n)) return "请输入正确金额";
            num = Long.parseLong(decimalNumFormat.format(amount));
        } catch (NumberFormatException e) {
            return "请输入正确金额";
        }
//        String[] fraction = {"角", "分"};
        String[] digit = {
                "零", "壹", "贰", "叁", "肆",
                "伍", "陆", "柒", "捌", "玖"
        };
        String[][] unit = {
                {"元", "万", "亿"},
                {"", "拾", "佰", "仟"}
        };
        String head = n < 0 ? "欠" : "";
        n = Math.abs(n);
        String s = "";
//        for (int i = 0; i < fraction.length; i++) {
//            s = s + (digit[(int) (n  * Math.pow(10, i+1) % 10)] + fraction[i]).replaceAll("/ 零. /", "");
//        }
        s = RegexHelper.isEmpty(s) ? "整" : s;
        for (int i = 0; i < unit[0].length && num > 0; i++) {
            String p = "";
            for (int j = 0; j < unit[1].length && num > 0; j++) {
                p = digit[(int) (num % 10)] + unit[1][j] + p;
                num = num / 10;
            }
            s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i] + s;
        }
        return head + s.replaceAll("(零.)*零元", "元")
                .replaceAll("(零.)+", "零")
                .replaceAll("^整$", "零元整");
    }

    /**
     * 所有为空
     *
     * @param needle
     * @return
     */
    public static boolean isAnyEmpty(Object... needle) {
        if (needle == null) return true;
        for (int i = 0; i < needle.length; i++) {
            if (!RegexHelper.isEmpty(needle[i])) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAllNotEmpty(Object... needle) {
        if (needle == null) return false;
        for (int i = 0; i < needle.length; i++) {
            if (RegexHelper.isEmpty(needle[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 其中一个为空
     *
     * @param needle
     * @return
     */
    public static boolean isOneEmpty(Object... needle) {
        if (needle == null) return true;
        for (int i = 0; i < needle.length; i++) {
            if (RegexHelper.isEmpty(needle[i])) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPort(String port) {
        if (!isNumber(port)) return false;
        int portInt = Integer.parseInt(port);
        return portInt > 0 && portInt < 65536;
    }
}
