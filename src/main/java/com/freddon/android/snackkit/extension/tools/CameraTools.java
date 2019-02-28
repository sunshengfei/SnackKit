package com.freddon.android.snackkit.extension.tools;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.FrameLayout.LayoutParams;

import java.util.List;

public class CameraTools {

    // 所有角度枚举
    public final class Degree {
        public static final int ROTATION_0 = 0;
        public static final int ROTATION_90 = 90;
        public static final int ROTATION_180 = 180;
        public static final int ROTATION_270 = 270;
        public static final int ROTATION_360 = 360;
    }

    /**
     * 检查设备是否提供摄像头
     */
    public static boolean checkCameraHardware(Context context) {
        if (context == null)
            return false;

        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // 摄像头存在
            return true;
        } else {
            // 摄像头不存在
            return false;
        }
    }

    // 旋转图片
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    // 获取相机实例
    public static Camera getCameraInstance(int mCameraId) {
        Camera c = null;
        try {
            c = Camera.open(mCameraId);
            Camera.Parameters parameters;
            parameters = c.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
            List<Camera.Size> SupportedPreviewSizes = parameters
                    .getSupportedPreviewSizes();// 获取支持预览照片的尺寸
            Camera.Size previewSize = SupportedPreviewSizes.get(0);// 从List取出Size
            parameters
                    .setPreviewSize(previewSize.width, previewSize.height);// 设置预览照片的大小
            List<Camera.Size> supportedPictureSizes = parameters
                    .getSupportedPictureSizes();// 获取支持保存图片的尺寸
            Camera.Size pictureSize = supportedPictureSizes.get(supportedPictureSizes.size() / 2);
            for (int i = 0; i < supportedPictureSizes.size(); i++) {
                Camera.Size pictureSizeTemp = supportedPictureSizes.get(i);// 从List取出Size
                if (pictureSizeTemp.width >= 2500 && pictureSizeTemp.width <= 3000 && pictureSizeTemp.height <= 2500) {
                    pictureSize = pictureSizeTemp;
                    break;
                }
            }
            parameters
                    .setPictureSize(pictureSize.width, pictureSize.height);// 设置照片的大小
            c.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    /**
     * 寻找w/h比例相当并且h高度绝对值相差最小的Size
     *
     * @param sizes
     * @param w
     * @param h
     * @return
     */
    public static Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null)
            return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    /**
     * 等比例缩放画幅大小[按比例,宽或高达到100%]
     *
     * @param lP 目标布局的宽高[依赖于当前布局的高宽,缩放后此高宽会改变]
     * @param w  当前画幅真实宽度
     * @param h  当前画幅真实宽度
     * @return
     */
    public static void getOptimalSurfaceSize(LayoutParams lP, int w, int h) {
        if (lP != null && lP.height > 0 && lP.width > 0) {
            if (w == lP.width && h == lP.height) {
                return;
            }
            // 为等比缩放计算输出布局的宽度及高度
            double rate1 = ((double) w) / (double) lP.width;
            double rate2 = ((double) h) / (double) lP.height;
            // 根据缩放比率大的进行缩放控制
            double rate = rate1 > rate2 ? rate1 : rate2;
            lP.width = (int) (((double) w) / rate);
            lP.height = (int) (((double) h) / rate);
        }
    }

    /**
     * 等比例缩放画幅大小
     *
     * @param lP              目标布局的宽高[依赖于当前布局的高宽,缩放后此高宽会改变]
     * @param w               当前画幅真实宽度
     * @param h               当前画幅真实宽度
     * @param bSmallToStretch 当前画幅完全小于布局高宽是否需要拉伸
     * @return
     */
    public static void getOptimalSurfaceSize2(LayoutParams lP, int w, int h,
                                              boolean bSmallToStretch) {
        if (lP != null && lP.height > 0 && lP.width > 0) {
            if (w < lP.width && h < lP.height && !bSmallToStretch) {
                lP.width = w;
                lP.height = h;
                return;
            }
            getOptimalSurfaceSize(lP, w, h);
        }
    }

    /**
     * 获取手握设备方向
     *
     * @param activity 不传值则默认为正握0
     * @return
     */
    public static int getDisplayRotation(Activity activity) {
        if (activity == null)
            return Degree.ROTATION_0;
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return Degree.ROTATION_0;
            case Surface.ROTATION_90:
                return Degree.ROTATION_90;
            case Surface.ROTATION_180:
                return Degree.ROTATION_180;
            case Surface.ROTATION_270:
                return Degree.ROTATION_270;
        }
        return Degree.ROTATION_0;
    }

    public static int getDisplayOrientation(int degrees, int cameraId) {
        // See android.hardware.Camera.setDisplayOrientation for
        // documentation.
        CameraInfo info = new CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int result;
        if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % Degree.ROTATION_360;
            result = (Degree.ROTATION_360 - result) % Degree.ROTATION_360; // compensate
            // the
            // mirror
        } else { // back-facing
            result = (info.orientation - degrees + Degree.ROTATION_360)
                    % Degree.ROTATION_360;
        }
        return result;
    }

    public static int getCameraOrientation(int cameraId) {
        CameraInfo info = new CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        return info.orientation;
    }

    public static int getJpegRotation(int cameraId, int orientation) {
        // See android.hardware.Camera.Parameters.setRotation for
        // documentation.
        int rotation = 0;
        if (orientation != OrientationEventListener.ORIENTATION_UNKNOWN) {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(cameraId, info);
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                rotation = (info.orientation - orientation + Degree.ROTATION_360)
                        % Degree.ROTATION_360;
            } else { // back-facing camera
                rotation = (info.orientation + orientation)
                        % Degree.ROTATION_360;
            }
        }
        return rotation;
    }

    public static Size getOptimalPreviewSize(Activity currentActivity,
                                             List<Size> sizes, double targetRatio) {

        Point[] points = new Point[sizes.size()];

        int index = 0;
        for (Size s : sizes) {
            points[index++] = new Point(s.width, s.height);
        }

        int optimalPickIndex = getOptimalPreviewSize(currentActivity, points,
                targetRatio);
        return (optimalPickIndex == -1) ? null : sizes.get(optimalPickIndex);
    }

    public static int getOptimalPreviewSize(Activity currentActivity,
                                            Point[] sizes, double targetRatio) {
        // Use a very small tolerance because we want an exact match.
        final double ASPECT_TOLERANCE = 0.01;
        if (sizes == null)
            return -1;

        int optimalSizeIndex = -1;
        double minDiff = Double.MAX_VALUE;

        // Because of bugs of overlay and layout, we sometimes will try to
        // layout the viewfinder in the portrait orientation and thus get the
        // wrong size of preview surface. When we change the preview size, the
        // new overlay will be created before the old one closed, which causes
        // an exception. For now, just get the screen size.
        Point point = CameraTools.getScreenMetrics(currentActivity);
        int targetHeight = Math.min(point.x, point.y);
        // Try to find an size match aspect ratio and size
        for (int i = 0; i < sizes.length; i++) {
            Point size = sizes[i];
            double ratio = (double) size.x / size.y;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.y - targetHeight) < minDiff) {
                optimalSizeIndex = i;
                minDiff = Math.abs(size.y - targetHeight);
            }
        }
        // Cannot find the one match the aspect ratio. This should not happen.
        // Ignore the requirement.
        if (optimalSizeIndex == -1) {
            Log.w(TAG, "No preview size match the aspect ratio");
            minDiff = Double.MAX_VALUE;
            for (int i = 0; i < sizes.length; i++) {
                Point size = sizes[i];
                if (Math.abs(size.y - targetHeight) < minDiff) {
                    optimalSizeIndex = i;
                    minDiff = Math.abs(size.y - targetHeight);
                }
            }
        }
        return optimalSizeIndex;
    }

    private static Point getScreenMetrics(Activity currentActivity) {
        WindowManager wm = (WindowManager) currentActivity
                .getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        if (wm != null) {
            wm.getDefaultDisplay().getRealSize(size);
        }
        return size;
    }

    /**
     * For still image capture, we need to get the right fps range such that the
     * camera can slow down the framerate to allow for less-noisy/dark
     * viewfinder output in dark conditions.
     *
     * @param params Camera's parameters.
     * @return null if no appropiate fps range can't be found. Otherwise, return
     * the right range.
     */
    public static int[] getPhotoPreviewFpsRange(Parameters params) {
        List<int[]> frameRates = params.getSupportedPreviewFpsRange();
        if (frameRates.size() == 0) {
            Log.e(TAG, "No suppoted frame rates returned!");
            return null;
        }

        // Find the lowest min rate in supported ranges who can cover 30fps.
        int lowestMinRate = MAX_PREVIEW_FPS_TIMES_1000;
        for (int[] rate : frameRates) {
            int minFps = rate[Parameters.PREVIEW_FPS_MIN_INDEX];
            int maxFps = rate[Parameters.PREVIEW_FPS_MAX_INDEX];
            if (maxFps >= PREFERRED_PREVIEW_FPS_TIMES_1000
                    && minFps <= PREFERRED_PREVIEW_FPS_TIMES_1000
                    && minFps < lowestMinRate) {
                lowestMinRate = minFps;
            }
        }

        // Find all the modes with the lowest min rate found above, the pick the
        // one with highest max rate.
        int resultIndex = -1;
        int highestMaxRate = 0;
        for (int i = 0; i < frameRates.size(); i++) {
            int[] rate = frameRates.get(i);
            int minFps = rate[Parameters.PREVIEW_FPS_MIN_INDEX];
            int maxFps = rate[Parameters.PREVIEW_FPS_MAX_INDEX];
            if (minFps == lowestMinRate && highestMaxRate < maxFps) {
                highestMaxRate = maxFps;
                resultIndex = i;
            }
        }

        if (resultIndex >= 0) {
            return frameRates.get(resultIndex);
        }
        Log.e(TAG, "Can't find an appropiate frame rate range!");
        return null;
    }

    public static int[] getMaxPreviewFpsRange(Parameters params) {
        List<int[]> frameRates = params.getSupportedPreviewFpsRange();
        if (frameRates != null && frameRates.size() > 0) {
            // The list is sorted. Return the last element.
            return frameRates.get(frameRates.size() - 1);
        }
        return new int[0];
    }

    /**
     * 打印支持的聚焦模式
     *
     * @param params
     */
    public void printSupportFocusMode(Parameters params) {
        List<String> focusModes = params.getSupportedFocusModes();
        for (String mode : focusModes) {
            Log.i(TAG, "focusModes--" + mode);
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    private static final String TAG = "CameraUtil";

    // For calculate the best fps range for still image capture.
    private final static int MAX_PREVIEW_FPS_TIMES_1000 = 400000;
    private final static int PREFERRED_PREVIEW_FPS_TIMES_1000 = 30000;
}
