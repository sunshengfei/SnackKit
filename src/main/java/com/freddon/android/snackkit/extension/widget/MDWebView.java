package com.freddon.android.snackkit.extension.widget;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by fred on 2016/11/6.
 */
public class MDWebView extends WebView {
    public MDWebView(Context context) {
        this(context, null);
    }

    public MDWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MDWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            initSettings();
        }
    }

    private void initSettings() {
        WebSettings webSetting = this.getSettings();
        webSetting.setDisplayZoomControls(false);
//        webSetting.setUserAgentString(webSetting.getUserAgentString() + " ClientType/APP" + " AppName/" + EnvConfig.APPNAME);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //解决issues # android.view.ThreadedRenderer.finalize() timed out after 10 seconds 或 设置android:hardwareAccelerated="true"
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            if (0 != (getContext().getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE)) {
                setWebContentsDebuggingEnabled(true);
            }
        }

        webSetting.setSupportZoom(true);
//        ws.setDefaultZoom(ZoomDensity.CLOSE);
        webSetting.setJavaScriptEnabled(true);
        //设置 缓存模式
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        webSetting.setDomStorageEnabled(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    }


}
