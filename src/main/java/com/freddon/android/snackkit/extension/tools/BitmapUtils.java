package com.freddon.android.snackkit.extension.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Bitmap工具类
 */
public class BitmapUtils {

    public static boolean decodeSampledBitmapFromFile(Context context, String filepath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, options);
        if (options.outWidth >= 3000 || options.outHeight >= 3000)
            options.inSampleSize = 4;
        else if (options.outWidth >= 1000 || options.outHeight >= 1000)
            options.inSampleSize = 2;
        else
            options.inSampleSize = 1;
        options.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeFile(filepath, options);
        File file = new File(CameraUtil.getInstance(context).getOutImagePath());
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream);
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Bitmap转byte[]数组
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        return baos.toByteArray();
    }

    /**
     * Bitmap按照规定尺寸缩小
     *
     * @param reqWidth  规定宽度
     * @param reqHeight 规定高度
     */
    public static Bitmap decodeSampledBitmapFromBitmap(Bitmap bitmap,
                                                       int reqWidth, int reqHeight) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, baos);
        byte[] data = baos.toByteArray();

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    /**
     * 相片按相框的比例动态缩放
     *
     * @param filePath 要缩放的图片地址
     * @param width    模板宽度
     * @param height   模板高度
     * @return
     */
    public static Bitmap upImageSize(String filePath, int width, int height) {
        if (filePath.isEmpty()) {
            return null;
        }
        Bitmap bmp = BitmapFactory.decodeFile(filePath);
        // 计算比例
        float scaleX = (float) width / bmp.getWidth();// 宽的比例
        float scaleY = (float) height / bmp.getHeight();// 高的比例
        //新的宽高
        int newW = 0;
        int newH = 0;
        if (scaleX > scaleY) {
            newW = (int) (bmp.getWidth() * scaleY);
            newH = (int) (bmp.getHeight() * scaleY);
        } else if (scaleX <= scaleY) {
            newW = (int) (bmp.getWidth() * scaleX);
            newH = (int) (bmp.getHeight() * scaleX);
        }
        return Bitmap.createScaledBitmap(bmp, newW, newH, true);
    }

    /**
     * 相片按相框的比例动态缩放
     *
     * @param bmp    要缩放的图片
     * @param width  模板宽度
     * @param height 模板高度
     * @return
     */
    public static Bitmap upImageSize(Bitmap bmp, int width, int height) {
        if (bmp == null) {
            return null;
        }
        // 计算比例
        float scaleX = (float) width / bmp.getWidth();// 宽的比例
        float scaleY = (float) height / bmp.getHeight();// 高的比例
        //新的宽高
        int newW = 0;
        int newH = 0;
        if (scaleX > scaleY) {
            newW = (int) (bmp.getWidth() * scaleY);
            newH = (int) (bmp.getHeight() * scaleY);
        } else if (scaleX <= scaleY) {
            newW = (int) (bmp.getWidth() * scaleX);
            newH = (int) (bmp.getHeight() * scaleX);
        }
        return Bitmap.createScaledBitmap(bmp, newW, newH, true);
    }

    /**
     * 活动模块把图片宽度放大到适应屏幕宽度
     */
    public static Bitmap upImageSize(Bitmap bmp, int width) {
        if (bmp == null) {
            return null;
        }

        // 计算比例
        float scaleX = (float) width / bmp.getWidth();// 宽的比例
        //新的宽高
        int newW = 0;
        int newH = 0;
        newW = (int) (bmp.getWidth() * scaleX);
        newH = (int) (bmp.getHeight() * scaleX);
        return Bitmap.createScaledBitmap(bmp, newW, newH, true);
    }

    /**
     * 计算图片缩小多少倍
     *
     * @param reqWidth  规定宽度
     * @param reqHeight 规定高度
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {

        final int picheight = options.outHeight;
        final int picwidth = options.outWidth;

        int targetheight = picheight;
        int targetwidth = picwidth;
        int inSampleSize = 1;

        if (targetheight > reqHeight || targetwidth > reqWidth) {
            while (targetheight >= reqHeight
                    && targetwidth >= reqWidth) {
                inSampleSize += 1;
                targetheight = picheight / inSampleSize;
                targetwidth = picwidth / inSampleSize;
            }
        }
        return inSampleSize;
    }

    /**
     * 计算图片宽高
     *
     * @param filePath 图片地址
     */
    public static String getPicWidthAndHeight(String filePath) {
        if (filePath != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);
            return options.outWidth + "," + options.outHeight;
        } else
            return null;
    }

    /**
     * 获取规定大小小图图片byte[]数组
     */
    public static byte[] BitmapPathToBytes(String filePath, int width,
                                           int height) {

        Bitmap bm = getSmallBitmap(filePath, width, height);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        return baos.toByteArray();
    }

    /**
     * 获取小图
     */
    public static Bitmap getSmallBitmap(String filePath, int width, int height) {
        Bitmap BitmapOrg = BitmapFactory.decodeFile(filePath);
        int oldWidth = BitmapOrg.getWidth();
        int oldHeight = BitmapOrg.getHeight();

        float scaleWidth = ((float) width) / oldWidth;
        float scaleHeight = ((float) height) / oldHeight;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);
        return Bitmap.createBitmap(BitmapOrg, 0, 0, oldWidth, oldHeight,
                matrix, true);

    }

    /**
     * 获取小图
     *
     * @param
     * @return
     */
    public static Bitmap getSmallBitmap(Bitmap BitmapOrg, int width, int height) {

        int oldWidth = BitmapOrg.getWidth();
        int oldHeight = BitmapOrg.getHeight();

        float scaleWidth = ((float) width) / oldWidth;
        float scaleHeight = ((float) height) / oldHeight;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);
        return Bitmap.createBitmap(BitmapOrg, 0, 0, oldWidth, oldHeight,
                matrix, true);

    }

    /**
     * 获取bitmap大小，单位kb
     */
    public static long getSizeOfBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);// 这里100的话表示不压缩质量
        int length = baos.size() / 1024;// 读出图片的kb大小
        try {
            baos.close();
        } catch (IOException e) {

        }
        return length;
    }

    /**
     * 图片质量压缩
     *
     * @param size 指定大小
     */
    public static int compressBitmap(Bitmap bitmap, float size) {
        int quality = 100;
        if (bitmap == null || getSizeOfBitmap(bitmap) <= size) {
            return quality;// 如果图片本身的大小已经小于这个大小了，就没必要进行压缩
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 如果签名是png的话，则不管quality是多少，都不会进行质量的压缩
        while (baos.size() / 1024f > size) {
            quality = quality - 4;// 每次都减少4
            baos.reset();// 重置baos即清空baos
            if (quality <= 0) {
                break;
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        }
        try {
            baos.close();
        } catch (IOException e) {

        }
        return quality;
    }

    /**
     * 旋转照片
     * s
     *
     * @param degress 角度
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }

    /**
     * 获得圆角图片
     *
     * @param bitmap
     * @param pixels
     * @return
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        //SRC_IN这种模式，两个绘制的效果叠加后取交集展现后图
        paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap getPushActivityImg(Bitmap bitmap, int width, int pixels) {
        Bitmap output = toRoundCorner(bitmap, pixels);
        return upImageSize(output, width);
    }


    /**
     * 压缩，有待调整
     *
     * @param var0
     * @param var1
     * @return
     */
    public static String getScaledImage(Context var0, String var1) {
        File var2 = new File(var1);
        if (!var2.exists()) {
            return var1;
        } else {
            long var3 = var2.length();
            if (var3 <= 51200L) {
                return var1;
            } else {
                Bitmap var5 = decodeScaleImage(var1, 1280, 1920);
                try {
                    File var6 = File.createTempFile("image", ".jpg", var0.getFilesDir());
                    FileOutputStream var7 = new FileOutputStream(var6);
                    var5.compress(Bitmap.CompressFormat.JPEG, 90, var7);
                    var7.close();
                    return var6.getAbsolutePath();
                } catch (Exception var8) {
                    var8.printStackTrace();
                    return var1;
                }
            }
        }
    }

    public static Bitmap decodeScaleImage(String var0, int var1, int var2) {
        BitmapFactory.Options var3 = getBitmapOptions(var0);
        int var4 = calculateInSampleSize(var3, var1, var2);
        var3.inSampleSize = var4;
        var3.inJustDecodeBounds = false;
        Bitmap var5 = BitmapFactory.decodeFile(var0, var3);
        int var6 = readPictureDegree(var0);
        Bitmap var7 = null;
        if (var5 != null && var6 != 0) {
            var7 = rotateImageView(var6, var5);
            var5.recycle();
            var5 = null;
            return var7;
        } else {
            return var5;
        }
    }


    public static Bitmap rotateImageView(int var0, Bitmap var1) {
        Matrix var2 = new Matrix();
        var2.postRotate((float) var0);
        Bitmap var3 = Bitmap.createBitmap(var1, 0, 0, var1.getWidth(), var1.getHeight(), var2, true);
        return var3;
    }

    public static BitmapFactory.Options getBitmapOptions(String var0) {
        BitmapFactory.Options var1 = new BitmapFactory.Options();
        var1.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(var0, var1);
        return var1;
    }

    public static int readPictureDegree(String var0) {
        short var1 = 0;

        try {
            ExifInterface var2 = new ExifInterface(var0);
            int var3 = var2.getAttributeInt("Orientation", 1);
            switch (var3) {
                case 3:
                    var1 = 180;
                    break;
                case 6:
                    var1 = 90;
                    break;
                case 8:
                    var1 = 270;
            }
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        return var1;
    }


    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        // 取 drawable 的颜色格式
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }
}
