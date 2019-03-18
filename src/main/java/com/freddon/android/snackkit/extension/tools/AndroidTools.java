package com.freddon.android.snackkit.extension.tools;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.File;

/**
 * Created by fred on 2017/1/26.
 */

public class AndroidTools {

    /**
     * 隐藏虚拟底部导航栏
     *
     * @param window
     */
    public static void hideVirtualNavBar(@NonNull Window window) {
        View decorView = window.getDecorView();
        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= 16) {
            flags |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        }
        if (Build.VERSION.SDK_INT < 19) {
            flags |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        } else {
            flags |= 0x00001000;
        }
        decorView.setSystemUiVisibility(flags);
    }

    /**
     * 显示虚拟底部导航栏
     *
     * @param activity
     */
    public static void showVirtualNavBar(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }


    /**
     * 复制文本到剪贴板
     *
     * @param context
     * @param text
     */
    public static void copyToClipBoard(@NonNull Context context, String text) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(null, text);
        if (clipboardManager!=null){
            clipboardManager.setPrimaryClip(clipData);
        }
    }

    /**
     * 获取剪贴板最新内容
     *
     * @param context
     * @param text
     * @return
     */
    public static CharSequence postFromClipBoard(Context context, String text) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager != null || clipboardManager.hasPrimaryClip()) {
            return clipboardManager.getPrimaryClip().getItemAt(0).getText();
        }
        return null;
    }


    public static void openInputKeyBoard(Context mContext, EditText mEditText) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static void closeInputKeyBoard(Context mContext, EditText mEditText) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    public static File getCacheDir(Context context, String uniqueName) {
        String cachePath =
                context.getCacheDir().getPath();
        return new File(cachePath + File.separator + uniqueName);
    }

    public static File getFilesDir(Context context) {
        String cachePath =
                context.getFilesDir().getAbsolutePath();
        return new File(cachePath);
    }
}
