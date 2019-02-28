package com.freddon.android.snackkit.extension.widget.layer;

import com.google.android.material.snackbar.Snackbar;
import android.view.View;

/**
 * Created by fred on 16/8/5.
 */
public class NotificationView {

    /**
     * 提示消息
     * @param v
     * @param text
     */
    public static synchronized void show(View v,CharSequence text){
        Snackbar.make(v,text,Snackbar.LENGTH_SHORT).show();
    }

    /**
     * 带操作的提示消息
     * @param v
     * @param text
     * @param duration
     * @param actionText
     * @param onClickListener
     */
    public static synchronized void showAndCallbacks(View v, CharSequence text,int duration, CharSequence actionText, View.OnClickListener onClickListener){
        Snackbar.make(v,text,duration)
                .setAction(actionText,onClickListener)
                .show();
    }
}
