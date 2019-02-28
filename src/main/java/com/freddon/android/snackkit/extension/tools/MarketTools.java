package com.freddon.android.snackkit.extension.tools;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class MarketTools {

    public static void openApplicationMarket(Context context, String packageName) {
        try {
            String str = "market://details?id=" + packageName;
            Intent localIntent = new Intent(Intent.ACTION_VIEW);
            localIntent.setData(Uri.parse(str));
            context.startActivity(localIntent);
        } catch (Exception e) {
            // 打开应用商店失败 可能是没有手机没有安装应用市场
            // 调用系统浏览器进入商城
//            String url = "http://app.mi.com/detail/163525?ref=search";
//            openLinkBySystem(url);
        }
    }
}
