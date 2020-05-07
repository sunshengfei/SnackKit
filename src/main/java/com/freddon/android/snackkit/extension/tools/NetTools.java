package com.freddon.android.snackkit.extension.tools;

import androidx.annotation.NonNull;

import com.freddon.android.snackkit.extension.regex.RegexHelper;

public class NetTools {

    public static String ipv4Toipv6(String ipv4) {
        if (RegexHelper.isUniformIP(ipv4)) {
            String[] ips = ipv4.split(".");
            for (int i = 0; i < ips.length; i++) {
                int bit = Integer.parseInt(ips[i]);
                System.out.println(bit);
            }
        }
        return null;
    }

    /**
     * A类
     * 10.0.0.0--10.255.255.255
     *
     * @return
     */
    public static boolean isIpv4ASegments(@NonNull String ipv4) {
//        10.0.0.0--10.255.255.255
        if (RegexHelper.isUniformIP(ipv4)) {
            return ipv4.startsWith("10.");
        }
        return false;
    }

    /**
     * 172.16.0.0--172.31.255.255
     *
     * @param ipv4
     * @return
     */
    public static boolean isIpv4BSegments(@NonNull String ipv4) {
        if (RegexHelper.isUniformIP(ipv4)) {
            String[] ipArr = ipv4.split("\\.");
            if ("172".equals(ipArr[0])) {
                int ip1 = Integer.parseInt(ipArr[1]);
                return ip1 >= 16 && ip1 <= 31;
            }
        }
        return false;
    }

    /**
     * 192.168.0.0--192.168.255.255
     *
     * @param ipv4
     * @return
     */
    public static boolean isIpv4CSegments(@NonNull String ipv4) {
        if (RegexHelper.isUniformIP(ipv4)) {
            return ipv4.startsWith("192.168");
        }
        return false;
    }


    /**
     * 内网地址
     *
     * @param ipv4
     * @return
     */
    public static boolean isIpv4InnerAddress(@NonNull String ipv4) {
        return isIpv4ASegments(ipv4) || isIpv4BSegments(ipv4) || isIpv4CSegments(ipv4);
    }

}
