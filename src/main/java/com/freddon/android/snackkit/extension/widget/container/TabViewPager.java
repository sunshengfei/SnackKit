package com.freddon.android.snackkit.extension.widget.container;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by fred on 16/8/4.
 */
public class TabViewPager extends ViewPager {

    public boolean isWrapped() {
        return wrapped;
    }

    public void setWrapped(boolean wrapped) {
        this.wrapped = wrapped;
    }

    private boolean wrapped;

    /**
     * 设置ViewPager是否可以滑动翻页
     *
     * @param isCanFlip
     */
    public void setCanFlip(boolean isCanFlip) {
        this.isCanFlip = isCanFlip;
    }

    private boolean isCanFlip = true;

    public TabViewPager(Context context) {
        super(context);
    }

    public TabViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (isCanFlip) return super.onInterceptTouchEvent(arg0);
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (isCanFlip) return super.onTouchEvent(arg0);
        return false;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (wrapped) {
            int height = 0;
            //下面遍历所有child的高度
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.measure(widthMeasureSpec,
                        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int h = child.getMeasuredHeight();
                if (h > height) //采用最大的view的高度。
                    height = h;
            }
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                    MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
