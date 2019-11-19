package com.freddon.android.snackkit.extension.tools;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.NonNull;

import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

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
        if (clipboardManager != null) {
            clipboardManager.setPrimaryClip(clipData);
        }
    }

    /**
     * 获取剪贴板最新内容
     *
     * @param context
     * @return
     */
    public static Hashtable<Long, CharSequence> postFromClipBoard(Context context) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager != null && clipboardManager.hasPrimaryClip()) {
            ClipData primary = clipboardManager.getPrimaryClip();
            Hashtable<Long, CharSequence> hashtable = new Hashtable<>();
            long timeStamp = 0;
            if (primary != null) {
                ClipData.Item item = primary.getItemAt(0);
                ClipDescription clipDescription = primary.getDescription();
                Class<?> f = clipDescription.getClass();
                Field field = null;
                try {
                    field = f.getDeclaredField("mTimeStamp");
                    field.setAccessible(true);
                    timeStamp = (long) field.get(clipDescription);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                hashtable.put(timeStamp, item.getText());
                return hashtable;
            }
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

    public static File getDBDir(Context context, String fileName) {
        String cachePath =
                context.getDatabasePath(fileName).getAbsolutePath();
        return new File(cachePath);
    }


    public static void unzipSingleToFile(File file, File outFile) {
        try {
            ZipFile zf = new ZipFile(file);
            BufferedInputStream bi;
            Enumeration e = zf.entries();
            while (e.hasMoreElements()) {
                ZipEntry ze2 = (ZipEntry) e.nextElement();
                String entryName = ze2.getName();
                String path = outFile.getAbsolutePath();
                if (!ze2.isDirectory()) {
                    System.out.println("正在创建解压文件 - " + entryName);
                    String fileDir = path.substring(0, path.lastIndexOf("/"));
                    File fileDirFile = new File(fileDir);
                    if (!fileDirFile.exists()) {
                        fileDirFile.mkdirs();
                    }
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outFile));
                    bi = new BufferedInputStream(zf.getInputStream(ze2));
                    byte[] readContent = new byte[1024];
                    int readCount = bi.read(readContent);
                    while (readCount != -1) {
                        bos.write(readContent, 0, readCount);
                        readCount = bi.read(readContent);
                    }
                    bos.close();
                }
            }
            zf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void unzipSingleToFile(InputStream in, File outFile) {
        try {
            ZipInputStream zp = new ZipInputStream(in);
            ZipEntry ze;
            while ((ze = zp.getNextEntry()) != null) {
                if (!ze.isDirectory()) {
                    if (outFile.exists()) {
                        outFile.delete();
                    }
                    int pos;
                    final int BUFFER_SIZE = 4096;
                    byte[] buf = new byte[BUFFER_SIZE];
                    try (OutputStream bos = new FileOutputStream(outFile);) {
                        while ((pos = zp.read(buf, 0, BUFFER_SIZE)) > 0) {
                            bos.write(buf, 0, pos);
                        }
                        bos.flush();
                    } catch (Exception e) {

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
