package com.freddon.android.snackkit.extension.widget.container;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import com.freddon.android.snackkit.extension.animation.Rotate3DAnimation;

/**
 * Created by fred on 2016/11/8.
 */

public class ViewReversalSwitcher extends FrameLayout {

    private final int CHILD_COUNT = 2;
    private boolean isSwitchedToNext;

    public View getInitialTopView() {
        return initialTopView;
    }

    public View getInitialBottomView() {
        return initialBottomView;
    }

    public View getPresentView() {
        return presentView;
    }

    private View initialTopView;
    private View initialBottomView;
    private View presentView;

    public ViewReversalSwitcher(Context context) {
        this(context, null);
    }

    public ViewReversalSwitcher(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewReversalSwitcher(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        if (getChildCount() != CHILD_COUNT) return;
        initialTopView = getChildAt(1);
        initialBottomView = getChildAt(0);
        presentView = initialTopView;
    }

    /**
     * @param topView
     * @param bottomView
     */
    public void putViewsIntoLayout(@NonNull View topView, @NonNull View bottomView) {
        if (getChildCount() > 0) {
            removeAllViews();
        }
        addView(bottomView);
        addView(topView);
        init();
    }


    public Animation getReverseAnimation(float startDegree, float endDegree, boolean isReverse, long duration) {
        final Rotate3DAnimation rotation = new Rotate3DAnimation(startDegree, endDegree,
                this.getWidth() / 2, this.getHeight() / 2, 50.0f, isReverse);
        rotation.setDuration(duration);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        return rotation;
    }


    public Animation getReverseAnimation(float startDegree, float endDegree, boolean isReverse) {
        return getReverseAnimation(startDegree, endDegree, isReverse, 800);
    }


}
