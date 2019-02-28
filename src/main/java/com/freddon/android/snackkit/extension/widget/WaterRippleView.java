package com.freddon.android.snackkit.extension.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import com.freddon.android.snackkit.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by fred on 2017/6/15.
 */
public class WaterRippleView extends View {

    private final float CONTENT_SIZE_DEFAULT = sp2px(22f);
    private Context mContext;
    private Paint mContentPaint;
    private Paint mBackgroundPaint;
    private Paint mRipplePaint;
    private int mBackgroundColor;
    private int mShadowColor;
    private int mContentColor;
    private float mContentSize;
    private int mCenterX;
    private int mCenterY;
    private String mCenterContent;
    private float mRadius;
    private float mPercent = 1.0f;
    private float mRatio = 0.01f;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mValueAnimator.start();
                    break;
            }
        }
    };
    private Paint mBoomPaint;
    private float mMaxRippleRadius;
    private Interpolator mInterpolator;
    private LinearInterpolator mLinearInterpolator;
    private ValueAnimator mValueAnimator;
    private OvershootInterpolator mOvershootInterpolator;
    private ValueAnimator mRippleValueAnimator;
    private ValueAnimator mSecRippleValueAnimator;
    private float mRippleAlpha;
    private long mTotalDuration;
    private String mDescContent;
    private Paint mDescPaint;
    private int mDescColor;
    private float mDescSize;
    private Paint mDesc2Paint;
    private String mDesc2Content;
    private long mRippleDuration;
    private long mBeatDuration;

    private int mRippleColor;

    public WaterRippleView(Context context) {
        this(context, null);
    }

    public WaterRippleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaterRippleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        initInterpolator();
        initAttrs(attrs);
        initPaint();
//        initTask();
        initValueAnim();
    }

    private void initValueAnim() {
        mLinearInterpolator = new LinearInterpolator();
        mOvershootInterpolator = new OvershootInterpolator(3);
        mValueAnimator = ValueAnimator.ofFloat(0, 1000, 2000).setDuration(mBeatDuration);
        mValueAnimator.setInterpolator(mLinearInterpolator);
        mValueAnimator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue() / 1000f;
            Log.d("WaterRippleView", "value:" + value);
            if (value < 1) {
                if (animation.getInterpolator() == mOvershootInterpolator) {
                    animation.setInterpolator(new LinearInterpolator());
                }
                mPercent = 1 + value * 0.03f;
            } else {
                if (animation.getInterpolator() == mLinearInterpolator) {
                    animation.setInterpolator(new OvershootInterpolator(3));
                }
                mPercent = 1 + (2 - value) * 0.03f;
            }
            Log.d("WaterRippleView", "mPercent:" + mPercent);

            mRadius = initRadius() * mPercent;
            postInvalidateDelayed(10);
        });
        mValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startRipple();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void startRipple() {
        mRippleValueAnimator = ValueAnimator.ofFloat(0, 1000).setDuration(mRippleDuration);
        mRippleValueAnimator.setInterpolator(new LinearInterpolator());
        mRippleValueAnimator.addUpdateListener(animation -> {
//                float animatedValue = (float) animation.getAnimatedValue() / 1000f;
            //水波纹系数
            postInvalidateDelayed(10);
        });
        mRippleValueAnimator.start();

        mSecRippleValueAnimator = ValueAnimator.ofFloat(0, 1000).setDuration(mRippleDuration);
        mSecRippleValueAnimator.setInterpolator(new LinearInterpolator());
        mSecRippleValueAnimator.addUpdateListener(animation -> {
//                float animatedValue = (float) animation.getAnimatedValue() / 1000f;
            //水波纹系数
//                mSecondCirclePercent = animatedValue;
            postInvalidateDelayed(10);
        });
        mHandler.postDelayed(() -> mSecRippleValueAnimator.start(), 800);
    }

    private void initInterpolator() {
        mInterpolator = new LinearInterpolator();
    }

    private void initTask() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new RippleTask(), mTotalDuration, mTotalDuration);
    }

    class RippleTask extends TimerTask {

        @Override
        public void run() {
            mRatio = mRatio * (-1f);
            mHandler.sendEmptyMessage(0);
        }
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.WaterRippleView);
        mBackgroundColor = typedArray.getColor(R.styleable.WaterRippleView_background_color, Color.WHITE);
        mShadowColor = typedArray.getColor(R.styleable.WaterRippleView_shadow_color, Color.TRANSPARENT);
        mContentColor = typedArray.getColor(R.styleable.WaterRippleView_content_color, Color.BLACK);
        mDescColor = typedArray.getColor(R.styleable.WaterRippleView_desc_color, Color.BLACK);
        mRippleColor = typedArray.getColor(R.styleable.WaterRippleView_ripple_color, Color.BLACK);
        mContentSize = typedArray.getDimension(R.styleable.WaterRippleView_content_size, CONTENT_SIZE_DEFAULT);
        mDescSize = typedArray.getDimension(R.styleable.WaterRippleView_desc_size, CONTENT_SIZE_DEFAULT);
        mCenterContent = typedArray.getString(R.styleable.WaterRippleView_center_content);
        mDescContent = typedArray.getString(R.styleable.WaterRippleView_desc_content);
        mDesc2Content = typedArray.getString(R.styleable.WaterRippleView_desc2_content);
        mTotalDuration = typedArray.getInt(R.styleable.WaterRippleView_total_duration, 4000);
        mRippleDuration = typedArray.getInt(R.styleable.WaterRippleView_ripple_duration, 3000);
        mBeatDuration = typedArray.getInt(R.styleable.WaterRippleView_beat_duration, 600);
        mRippleAlpha = typedArray.getFloat(R.styleable.WaterRippleView_ripple_alpha, 0.6f);
        typedArray.recycle();
    }

    public void setmContentColor(int mContentColor) {
        this.mContentColor = mContentColor;
        mContentPaint.setColor(mContentColor);
    }

    private void initPaint() {

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(mBackgroundColor);

        mContentPaint = new Paint();
        mContentPaint.setColor(mContentColor);
        mContentPaint.setStyle(Paint.Style.FILL);
        mContentPaint.setAntiAlias(true);
        mContentPaint.setTextSize(mContentSize);

        mDescPaint = new Paint();
        mDescPaint.setColor(mDescColor);
        mDescPaint.setStyle(Paint.Style.FILL);
        mDescPaint.setAntiAlias(true);
        mDescPaint.setTextSize(mDescSize);

        mDesc2Paint = new Paint();
        mDesc2Paint.setColor(mDescColor);
        mDesc2Paint.setStyle(Paint.Style.FILL);
        mDesc2Paint.setAntiAlias(true);
        mDesc2Paint.setTextSize(mDescSize);

        mBoomPaint = new Paint();
        mBoomPaint.setStyle(Paint.Style.FILL);
        mBoomPaint.setColor(mShadowColor);

        mRipplePaint = new Paint();
        mRipplePaint.setStyle(Paint.Style.STROKE);
        mRipplePaint.setStrokeWidth(3f);
        mRipplePaint.setColor(mRippleColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mCenterX = this.getMeasuredWidth() / 2;
        mCenterY = this.getMeasuredHeight() / 2;
        mRadius = initRadius();
        mMaxRippleRadius = initRadius() * 1.25f;
    }

    private float initRadius() {
        return mCenterY * 8 / 10f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画背景
        drawGradientBackground(canvas);
        //画中心文字内容
        drawCenterText(canvas);
        //画描述文字内容
        drawDescText(canvas);
        drawDesc2Text(canvas);
        //画周围扩散水波纹
//        if (mRippleValueAnimator != null) {
//            drawRippleCircle(canvas, mRipplePercent);
//
//            if (mSecRippleValueAnimator != null && mSecRippleValueAnimator.isRunning()) {
//                drawRippleCircle(canvas, mSecondCirclePercent);
//            }
//        }
    }

    private void drawRippleCircle(Canvas canvas, float ripplePercent) {
        float radius = rippleRadius(ripplePercent);
        mRipplePaint.setAlpha(rippleAlpha(ripplePercent));
        /*mRipplePaint.setShader(new RadialGradient(mCenterX, mCenterY, radius
                , new int[]{Color.parseColor("#30EBFFEC"), Color.parseColor("#00FFFFFF"), Color.parseColor("#00FFFFFF"), Color.parseColor("#30EBFFEC")},
                null, Shader.TileMode.REPEAT));*/
        canvas.drawCircle(mCenterX, mCenterY, radius, mRipplePaint);
    }

    public int rippleAlpha(float ripplePercent) {
        float interpolation = mInterpolator.getInterpolation(ripplePercent);
        if (interpolation < 0.5f) {
            return (int) ((interpolation * 2) * 255 * mRippleAlpha);//0-1
        } else {
            return (int) ((2.0f - interpolation * 2) * 255 * mRippleAlpha);//1-0
        }
    }

    public float rippleRadius(float ripplePercent) {
        float tempR = mInterpolator.getInterpolation(ripplePercent) * (mMaxRippleRadius - initRadius());
        return initRadius() + tempR;
    }

    public void start() {
        mValueAnimator.start();
    }

    private void drawGradientBackground(Canvas canvas) {
        if (mRadius <= 0) {
            mRadius = dipToPx(2);
        }
        mBackgroundPaint.setShader(new RadialGradient(mCenterX, mCenterY, mRadius
                , new int[]{mBackgroundPaint.getColor(), mBackgroundPaint.getColor()},
                new float[]{0.9f, 1f}, Shader.TileMode.CLAMP));
        canvas.drawCircle(mCenterX, mCenterY, mRadius, mBackgroundPaint);
    }

    private void drawDesc2Text(Canvas canvas) {
        float middle = mDesc2Paint.measureText(mDesc2Content);
        canvas.drawText(mDesc2Content, mCenterX - middle / 2,
                mCenterY + mDesc2Paint.getTextSize() + mDescPaint.getTextSize() + ((mDesc2Paint.descent() + mDesc2Paint.ascent()) / 2) + mDescPaint.descent() + initRadius() / 4,
                mDesc2Paint);
    }

    private void drawDescText(Canvas canvas) {
        float middle = mDescPaint.measureText(mDescContent);
        canvas.drawText(mDescContent, mCenterX - middle / 2,
                mCenterY + mDescPaint.getTextSize() + ((mDescPaint.descent() + mDescPaint.ascent()) / 2) + initRadius() / 4,
                mDescPaint);
    }

    private void drawCenterText(Canvas canvas) {
        float middle = mContentPaint.measureText(mCenterContent);
        canvas.drawText(mCenterContent, mCenterX - middle / 2,
                mCenterY - mContentPaint.getTextSize() / 2 - ((mContentPaint.descent() + mContentPaint.ascent()) / 2) + initRadius() / 4,
                mContentPaint);
    }

    public float getRippleAlpha() {
        return mRippleAlpha;
    }

    public void setRippleAlpha(float rippleAlpha) {
        mRippleAlpha = rippleAlpha;
    }

    public long getTotalDuration() {
        return mTotalDuration;
    }

    public void setTotalDuration(long totalDuration) {
        mTotalDuration = totalDuration;
    }

    public String getDescContent() {
        return mDescContent;
    }

    public void setDescContent(String descContent) {
        mDescContent = descContent;
    }

    public int getDescColor() {
        return mDescColor;
    }

    public void setDescColor(int descColor) {
        mDescColor = descColor;
    }

    public float getDescSize() {
        return mDescSize;
    }

    public void setDescSize(float descSize) {
        mDescSize = descSize;
    }

    public Paint getDesc2Paint() {
        return mDesc2Paint;
    }

    public void setDesc2Paint(Paint desc2Paint) {
        mDesc2Paint = desc2Paint;
    }

    public String getDesc2Content() {
        return mDesc2Content;
    }

    public void setDesc2Content(String desc2Content) {
        mDesc2Content = desc2Content;
    }

    public long getRippleDuration() {
        return mRippleDuration;
    }

    public void setRippleDuration(long rippleDuration) {
        mRippleDuration = rippleDuration;
    }

    public long getBeatDuration() {
        return mBeatDuration;
    }

    public void setBeatDuration(long beatDuration) {
        mBeatDuration = beatDuration;
    }

    public int getRippleColor() {
        return mRippleColor;
    }

    public void setRippleColor(int rippleColor) {
        mRippleColor = rippleColor;
    }

    public String getCenterContent() {
        return mCenterContent;
    }

    public void setCenterContent(String centerContent) {
        mCenterContent = centerContent;
        postInvalidate();
    }

    /**
     * dip 转换成px
     *
     * @param dip
     * @return
     */
    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    private int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
