package com.freddon.android.snackkit.extension.widget;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.freddon.android.snackkit.R;
import com.freddon.android.snackkit.extension.animation.AnimatorCenter;

/**
 * Created by fred on 2016/9/9.
 */
public class CirclePlayBar extends View {

    /**
     * the foreground progress
     */
    private float progress = 0;

    /**
     * the foreground progress of Loading
     */
    private float loadingProgress = 0;//0~100

    /**
     * the foreground progress stroke line-width
     */
    private float strokeWidth = 3f;

    /**
     * the background progress stroke line-width
     */
    private float backgroundStrokeWidth = 2f;

    /**
     * the foreground progress stroke line-color
     */
    private int color = Color.BLACK;

    /**
     * the background progress stroke line-color
     */
    private int backgroundColor = Color.GRAY;

    /**
     * default start angle
     */
    private final static int START_ANGLE = -90;

    private RectF rectF;
    private Paint backgroundPaint;
    private Paint foregroundPaint;
    private Paint iconPaint;

    public static final int STATE_STOP = 0;
    public static final int STATE_RELEASE = 1;
    public static final int STATE_PAUSE = 1 << 1;
    public static final int STATE_LOADING = 1 << 2;
    public static final int STATE_PLAYING = 1 << 3;

    private Path iconPath;
    private RectF rectIcon, rectIconDivider;
    private RectF rectIconRight = new RectF();
    private Paint LoadingPaint;
    private RectF rectFLoading;
    private Paint iconDividerPaint;

    public int state = STATE_PLAYING;

    private ObjectAnimator objectAnimator;

    public CirclePlayBar(Context context) {
        this(context, null);
    }

    public CirclePlayBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePlayBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        rectF = new RectF();
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CirclePlayBar, 0, 0);
        //Reading values from the XML layout
        try {
            // Value
            progress = typedArray.getFloat(R.styleable.CirclePlayBar_cpb_progress, progress);
            loadingProgress = typedArray.getFloat(R.styleable.CirclePlayBar_cpb_progress_loading, loadingProgress);
            state = typedArray.getInt(R.styleable.CirclePlayBar_state, state);
            // StrokeWidth
            strokeWidth = typedArray.getDimension(R.styleable.CirclePlayBar_cpb_progressbar_width, strokeWidth);
            backgroundStrokeWidth = typedArray.getDimension(R.styleable.CirclePlayBar_cpb_background_progressbar_width, backgroundStrokeWidth);
            // Color
            color = typedArray.getInt(R.styleable.CirclePlayBar_cpb_progressbar_color, color);
            backgroundColor = typedArray.getInt(R.styleable.CirclePlayBar_cpb_background_progressbar_color, backgroundColor);
        } finally {
            typedArray.recycle();
        }

        // Init Background
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(backgroundStrokeWidth);

        // Init Foreground
        foregroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        foregroundPaint.setColor(color);
        foregroundPaint.setStyle(Paint.Style.STROKE);
        foregroundPaint.setStrokeWidth(strokeWidth);

        // Loading Foreground
        LoadingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        LoadingPaint.setColor(color);
        LoadingPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        LoadingPaint.setStrokeWidth(backgroundStrokeWidth);

        iconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        iconPaint.setColor(color);
        iconPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        iconPaint.setStrokeWidth(backgroundStrokeWidth);

        iconDividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        iconDividerPaint.setColor(Color.WHITE);
        iconDividerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        iconDividerPaint.setStrokeWidth(backgroundStrokeWidth);

        iconPath = new Path();
        rectIcon = new RectF();
        rectIconDivider = new RectF();
        rectFLoading = new RectF();

        setLoadingProgress(loadingProgress);
        setState(state);
    }
    //endregion

    //region Draw Method
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawOval(rectF, backgroundPaint);
        float angle = 360 * progress / 100;
        canvas.drawArc(rectF, START_ANGLE, angle, false, foregroundPaint);
        //画中间icon
        final float centerX = (rectF.right + rectF.left) * 1.0F / 2;
        final float centerY = (rectF.bottom + rectF.top) * 1.0F / 2;
        final float cW = (rectF.right - rectF.left) * 1.0F / 6;
        if (state == STATE_RELEASE) {
            iconPath.moveTo(centerX - cW, centerY - cW);
            iconPath.lineTo(centerX - cW, centerY + cW);
            iconPath.lineTo(centerX + cW, centerY);
            iconPath.close();
            canvas.drawPath(iconPath, iconPaint);
        } else if (state == STATE_PLAYING || state == STATE_PAUSE) {

//            rectIconDivider.left = centerX - cW / 6;
//            rectIconDivider.top = centerY - cW - 3;
//            rectIconDivider.right = centerX + cW / 6;
//            rectIconDivider.bottom = centerY + cW + 3;
            //2cw / 3
            float borderWidth = iconPaint.getStrokeWidth();
            rectIcon.left = centerX - cW;
            rectIcon.top = centerY - cW;
            rectIcon.right = centerX - cW / 3 - borderWidth / 2;
            rectIcon.bottom = centerY + cW;

            rectIconRight.top = centerY - cW;
            rectIconRight.right = centerX + cW;
            rectIconRight.bottom = centerY + cW;
            rectIconRight.left = centerX + cW / 3 + borderWidth / 2;


            canvas.drawRect(rectIcon, iconPaint);
            canvas.drawRect(rectIconRight, iconPaint);
//            canvas.drawRect(rectIconDivider, iconDividerPaint);
        } else if (state == STATE_LOADING) {
            rectFLoading.left = rectF.left + backgroundStrokeWidth * 2.0F;
            rectFLoading.top = rectF.top + backgroundStrokeWidth * 2.0F;
            rectFLoading.right = rectF.right - backgroundStrokeWidth * 2.0F;
            rectFLoading.bottom = rectF.bottom - backgroundStrokeWidth * 2.0F;
            float loadingweepAngle = loadingProgress * 360F / 100F;
            canvas.drawArc(rectFLoading, START_ANGLE, loadingweepAngle, true, LoadingPaint);
        } else if (state == STATE_STOP) {
            rectIcon.left = centerX - cW;
            rectIcon.top = centerY - cW;
            rectIcon.right = centerX + cW;
            rectIcon.bottom = centerY + cW;

            canvas.drawRect(rectIcon, iconPaint);
        }

    }
    //endregion

    //region Mesure Method
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        float highStroke = (strokeWidth > backgroundStrokeWidth) ? strokeWidth : backgroundStrokeWidth;
        rectF.set(0 + highStroke / 2, 0 + highStroke / 2, min - highStroke / 2, min - highStroke / 2);
    }
    //endregion

    //region Method Get/Set
    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = (progress <= 100) ? progress : 100;
        invalidate();
    }

    public float getLoadingProgress() {
        return loadingProgress;
    }

    public void setLoadingProgress(float loadingProgress) {
        this.loadingProgress = loadingProgress < 0 ? 0 : (loadingProgress > 100 ? 100 : loadingProgress);
        invalidate();
    }


    public void setState(int state) {
        this.state = state;
        if (objectAnimator != null && objectAnimator.isRunning()) {
            objectAnimator.cancel();
        }
        if (state == STATE_RELEASE || state == STATE_STOP) {
            setProgress(0);
            loadingProgress = 0;
        } else if (state == STATE_PLAYING || state == STATE_PAUSE) {
            loadingProgress = 0;
        } else if (state == STATE_LOADING) {
            setProgress(0);
        }
        invalidate();
    }

    private void removeAnimation() {
        if (objectAnimator != null) {
            objectAnimator.cancel();
            objectAnimator = null;
        }
    }


    public float getProgressBarWidth() {
        return strokeWidth;
    }

    public void setProgressBarWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        foregroundPaint.setStrokeWidth(strokeWidth);
        requestLayout();//Because it should recalculate its bounds
        invalidate();
    }

    public float getBackgroundProgressBarWidth() {
        return backgroundStrokeWidth;
    }

    public void setBackgroundProgressBarWidth(float backgroundStrokeWidth) {
        this.backgroundStrokeWidth = backgroundStrokeWidth;
        backgroundPaint.setStrokeWidth(backgroundStrokeWidth);
        requestLayout();//Because it should recalculate its bounds
        invalidate();
    }

    public void setMaskColor(int color) {
        this.color = color;
        iconPaint.setColor(color);
        foregroundPaint.setColor(color);
        LoadingPaint.setColor(color);
        this.backgroundColor = color;
        backgroundPaint.setColor(backgroundColor);
        invalidate();
    }

    public void setColor(int color) {
        this.color = color;
        iconPaint.setColor(color);
        LoadingPaint.setColor(color);
        foregroundPaint.setColor(color);
        invalidate();
        requestLayout();
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        backgroundPaint.setColor(backgroundColor);
        invalidate();
        requestLayout();
    }
    //endregion

    //region Other Method


    /**
     * Set the loading animation
     *
     * @param loadingProgress
     * @param duration
     */
    public ObjectAnimator getLoadingAnimation(float loadingProgress, int duration) {
        removeAnimation();
        objectAnimator = ObjectAnimator.ofFloat(this, "loadingProgress", 360F + loadingProgress);
        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.setRepeatCount(Integer.MAX_VALUE >> 1);
        return objectAnimator;
    }

    public ObjectAnimator getLoadingAnimator(int duration) {
        removeAnimation();
        objectAnimator = ObjectAnimator.ofFloat(this, "loadingProgress", 0, 100);
        objectAnimator.setDuration(duration);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.setRepeatCount(Integer.MAX_VALUE >> 1);
        return objectAnimator;
    }
    //endregion

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeAnimation();
    }
}
