package com.freddon.android.snackkit.extension.widget.container;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by fred on 2016/11/8.
 */

public class ReversLayout extends RelativeLayout {

    public boolean isReverse = true;

    public ReversLayout(Context context) {
        super(context);
    }

    public ReversLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReversLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isReverse) ev.setLocation(this.getWidth() - ev.getX(), ev.getY());
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void dispatchDraw(Canvas canvas) {

        if (isReverse)
            canvas.scale(-1, 1, getWidth() / 2, getHeight() / 2);
        super.dispatchDraw(canvas);
    }
}
