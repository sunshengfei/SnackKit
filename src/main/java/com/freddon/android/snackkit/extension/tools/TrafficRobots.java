package com.freddon.android.snackkit.extension.tools;


import android.os.Process;
import com.freddon.android.snackkit.log.Loger;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Created by fred on 16/6/13.
 */
public class TrafficRobots {

    public static LinkedHashMap<String, List<Record>> getMaps() {
        return maps;
    }

    static LinkedHashMap<String, List<Record>> maps;//TODO 此数据应该持久保存 目前仅做测试

    public static void record(String simpleName, Traffics traffics, boolean isEndPoint) {
        if (maps == null) {
            maps = new LinkedHashMap<>();
        }
        if (!maps.containsKey(simpleName)) {
            maps.put(simpleName, new ArrayList<Record>());
        }
        if ((traffics.mobileTx | traffics.mobileRx | traffics.wifiTx | traffics.wifiRx) == 0) return;
        List<Record> list = maps.get(simpleName);
        Record p = new Record();
        p.createTime = System.currentTimeMillis();
        p.traffics = new Traffics();
        p.isEndPoint = isEndPoint;
        p.traffics.mobileTx = traffics.mobileTx;
        p.traffics.mobileRx = traffics.mobileRx;
        p.traffics.wifiTx = traffics.wifiTx;
        p.traffics.wifiRx = traffics.wifiRx;
        list.add(p);
    }


    public static synchronized void logTraffic(String key, String tag, boolean isEndPoint) {
        TrafficRobots.Traffics data = TrafficRobots.update();
        Loger.e("[Traffic]", "[%s][P=%s][T=%s] wifi rx/tx=%d/%d, mobile rx/tx=%d/%d", System.currentTimeMillis(), key,
                tag,
                data.wifiRx, data.wifiTx, data.mobileRx, data.mobileTx);
        TrafficRobots.record(key, data, isEndPoint);
    }

    public static String readRecord() {
        if (maps != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("<!DOCTYPE html><html><head><meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\"><style>.wifi{color:green;}.mobile{color:red;}td{ border:1px solid #666;}</style></head><body>");
            sb.append("<table>");
            sb.append("<thead><tr><td>页面</td><td>统计时间</td><td>WIFI 下行（B）</td><td>WIFI 上行（B）</td><td>CMNET 下行（B）</td><td>CMNET 上行（B）</td></tr></thead>");
            sb.append("<tbody>");
            for (String key : maps.keySet()) {
                List<Record> records = maps.get(key);
                for (int i = 0; i < records.size(); i++) {
                    Record data = records.get(i);
                    sb.append("<tr> ");
                    sb.append("<td>");
                    sb.append(key);
                    sb.append("</td>");
                    sb.append("<td>");
                    sb.append(data.createTime);
                    sb.append("</td>");
                    sb.append("<td class='wifi'>");
                    sb.append(data.traffics.wifiRx);
                    sb.append("</td>");
                    sb.append("<td class='wifi'>");
                    sb.append(data.traffics.wifiTx);
                    sb.append("</td>");
                    sb.append("<td class='mobile'>");
                    sb.append(data.traffics.mobileRx);
                    sb.append("</td>");
                    sb.append("<td  class='mobile'>");
                    sb.append(data.traffics.mobileTx);
                    sb.append("</td>");
                    sb.append("</tr>");
                }
            }
            sb.append("</tbody>");
            sb.append("</table></body></html>");
            return sb.toString();
        }
        return null;
    }

    static class Record {
        public long createTime;//记录创建时间

        public Traffics traffics;

        public boolean isEndPoint;

        public Record() {
        }

    }

    private TrafficRobots() {

    }

    static TrafficRobots trafficRobot;

    public static TrafficRobots getInstance() {
        if (trafficRobot == null) {
            trafficRobot = new TrafficRobots();
        }
        return trafficRobot;
    }


    public static long getWifiTx() {
        return wifiTx;
    }

    public static long getWifiRx() {
        return wifiRx;
    }

    public static long getMobileTx() {
        return mobileTx;
    }

    public static long getMobileRx() {
        return mobileRx;
    }


    private static long lossMobileTx;//消耗的移动上行流量
    private static long lossMobileRx;//消耗的移动下行流量
    private static long lossWifiTx;//消耗的WIFI上行流量
    private static long lossWifiRx;//消耗的WIFI下行流量

    private static long mobileTx;
    private static long mobileRx;
    private static long wifiTx;
    private static long wifiRx;


    public static long getWifiTx(long var0) {
        return wifiTx > var0 ? wifiTx : var0;
    }

    public static long getWifiRx(long var0) {
        return wifiRx > var0 ? wifiRx : var0;
    }

    public static long getMobileTx(long var0) {
        return mobileTx > var0 ? mobileTx : var0;
    }

    public static long getMobileRx(long var0) {
        return mobileRx > var0 ? mobileRx : var0;
    }


    public static long updateWifiTx(long var0) {
        update();
        return getWifiTx(var0);
    }

    public static long updateWifiRx(long var0) {
        update();
        return getWifiRx(var0);
    }

    public static long updateMobileTx(long var0) {
        update();
        return getMobileTx(var0);
    }

    public static long updateMobileRx(long var0) {
        update();
        return getMobileRx(var0);
    }

    public static void reset() {
        lossMobileTx = -1L;
        lossMobileRx = -1L;
        lossWifiTx = -1L;
        lossWifiRx = -1L;
        update();
    }


    /**
     *  Receive|
     *
     *  bytes  | packets  | errs  | drop  | fifo  | frame  | compressed  |  multicast
     */


    /**
     * Transfer |
     * <p/>
     * bytes    | packets  | errs  | drop  | fifo |  colls  | carrier  | compressed
     */

    public static Traffics update() {
        long mTx = 0L;//记录非wifi上行流量
        long mRx = 0L;//记录非wifi下行流量
        long wTx = 0L;//记录wifi上行流量
        long wRx = 0L;//记录wifi下行流量

        Scanner devFileScanner;
        try {

            /**
             * Interface  |   Receive                                                    |  Transmit
             * bytes    packets errs drop fifo frame compressed multicast|bytes    packets errs drop fifo colls carrier compressed
             * rev_rmnet1:       0       0    0    0    0     0          0         0        0       0    0    0    0     0       0          0
             * rmnet6:       0       0    0    0    0     0          0         0        0       0    0    0    0     0       0          0
             * rev_rmnet5:       0       0    0    0    0     0          0         0        0       0    0    0    0     0       0          0
             * rmnet1:       0       0    0    0    0     0          0         0        0       0    0    0    0     0       0          0
             * lo:       0       0    0    0    0     0          0         0        0       0    0    0    0     0       0          0
             * rev_rmnet0:       0       0    0    0    0     0          0         0        0       0    0    0    0     0       0          0
             * rmnet5:       0       0    0    0    0     0          0         0        0       0    0    0    0     0       0          0
             * rev_rmnet4:       0       0    0    0    0     0          0         0        0       0    0    0    0     0       0          0
             * rmnet0:       0       0    0    0    0     0          0         0        0       0    0    0    0     0       0          0
             * rev_rmnet8:       0       0    0    0    0     0          0         0        0       0    0    0    0     0       0          0
             * rmnet4:       0       0    0    0    0     0          0         0        0       0    0    0    0     0       0          0
             * sit0:       0       0    0    0    0     0          0         0        0       0    0    0    0     0       0          0
             * dummy0:       0       0    0    0    0     0          0         0        0       0    0    0    0     0       0          0
             * rev_rmnet3:       0       0    0    0    0     0          0         0        0       0    0    0    0     0       0          0
             * wlan0: 306614616  336663    0  111    0     0          0         0 38000348  323723    0    0    0     0       0          0
             * rmnet_usb0:       0       0    0    0    0     0          0         0        0       0    0    0    0     0       0          0
             * rev_rmnet7:       0       0    0    0    0     0          0         0        0       0    0    0    0     0       0          0
             * rmnet3:       0       0    0    0    0     0          0         0        0       0    0    0    0     0       0          0
             * rev_rmnet2:       0       0    0    0    0     0          0         0        0       0    0    0    0     0       0          0
             * rmnet7:       0       0    0    0    0     0          0         0        0       0    0    0    0     0       0          0
             * rev_rmnet6:       0       0    0    0    0     0          0         0        0       0    0    0    0     0       0          0
             * rmnet2:       0       0    0    0    0     0          0         0        0       0    0    0    0     0       0          0
             * p2p0:       0       0    0    0    0     0          0         0        0       0    0    0    0     0       0          0
             */


            //开始读取dev文件
            (devFileScanner = new Scanner(new File("/proc/" + Process.myPid() + "/net/dev"))).nextLine();//向下读一行 读到表头
            devFileScanner.nextLine();//向下读一行 读到列头

            while (devFileScanner.hasNext()) {
                String[] cols;//记录读取的一行 正则空格切为 行头： + 数字 [rev_rmnet1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                // 如果行头长度为0（防止出现多余的空格冒号干扰 如[,rev_rmnet1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]）
                // 则将指向interface及数据的index+1 否则为index
                int rowHeadIndex = (cols = devFileScanner.nextLine().split("[ :\t]+"))[0].length() == 0 ? 1 : 0;

                /**
                 * 累计所有的非wifi流量
                 */
                if (!cols[0].equals("lo") && cols[rowHeadIndex + 0].startsWith("rmnet")) {
                    mTx += Long.parseLong(cols[rowHeadIndex + 9]);
                    mRx += Long.parseLong(cols[rowHeadIndex + 1]);
                }

                /**
                 * 累计所有的wifi流量
                 */
                if (!cols[rowHeadIndex + 0].equals("lo") && !cols[rowHeadIndex + 0].startsWith("rmnet")) {
                    wTx += Long.parseLong(cols[rowHeadIndex + 9]);
                    wRx += Long.parseLong(cols[rowHeadIndex + 1]);
                }
            }

            devFileScanner.close();
            //如果没有消耗移动上行流量 则默认为当前记录的总移动上行流量
            if (lossMobileTx < 0L) {
                lossMobileTx = mTx;
                Loger.v("TrafficRobots", "fix loss newMobileTx %d", new Object[]{Long.valueOf(mTx)});
            }

            //同理
            if (lossMobileRx < 0L) {
                lossMobileRx = mRx;
                Loger.v("TrafficRobots", "fix loss newMobileRx %d", new Object[]{Long.valueOf(mRx)});
            }
            //同理
            if (lossWifiTx < 0L) {
                lossWifiTx = wTx;
                Loger.v("TrafficRobots", "fix loss newWifiTx %d", new Object[]{Long.valueOf(wTx)});
            }
            //同理
            if (lossWifiRx < 0L) {
                lossWifiRx = wRx;
                Loger.v("TrafficRobots", "fix loss newWifiRx %d", new Object[]{Long.valueOf(wRx)});
            }

            if (wRx - lossWifiRx < 0L) {
                Loger.v("TrafficRobots", "minu %d", new Object[]{Long.valueOf(wRx - lossWifiRx)});
            }

            if (wTx - lossWifiTx < 0L) {
                Loger.v("TrafficRobots", "minu %d", new Object[]{Long.valueOf(wTx - lossWifiTx)});
            }

            mobileTx = mTx >= lossMobileTx ? mTx - lossMobileTx : mTx;
            mobileRx = mRx >= lossMobileRx ? mRx - lossMobileRx : mRx;
            wifiTx = wTx >= lossWifiTx ? wTx - lossWifiTx : wTx;
            wifiRx = wRx >= lossWifiRx ? wRx - lossWifiRx : wRx;
            lossMobileTx = mTx;
            lossMobileRx = mRx;
            lossWifiTx = wTx;
            lossWifiRx = wRx;
        } catch (Exception var11) {
            devFileScanner = null;
            var11.printStackTrace();
        }
        if (traffics == null) {
            traffics = new Traffics();
        }
        traffics.mobileTx = mobileTx;
        traffics.mobileRx = mobileRx;
        traffics.wifiTx = wifiTx;
        traffics.wifiRx = wifiRx;
        Loger.d("TrafficRobots", "current system traffic: wifi rx/tx=%d/%d, mobile rx/tx=%d/%d", new Object[]{Long.valueOf(wifiRx), Long.valueOf(wifiTx), Long.valueOf(mobileRx), Long.valueOf(mobileTx)});
        return traffics;
    }

    static Traffics traffics;

    public static class Traffics {
        public long mobileTx;
        public long mobileRx;
        public long wifiTx;
        public long wifiRx;
        public long timeMilles;
    }
}
