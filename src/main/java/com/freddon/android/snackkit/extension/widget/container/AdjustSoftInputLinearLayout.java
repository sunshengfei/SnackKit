package com.freddon.android.snackkit.extension.widget.container;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by fred on 2018/6/8.
 */

public class AdjustSoftInputLinearLayout extends LinearLayout {

    private int[] mInsets = new int[4];


    public AdjustSoftInputLinearLayout(Context context) {
        super(context);
    }

    public AdjustSoftInputLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdjustSoftInputLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected final boolean fitSystemWindows(Rect insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Intentionally do not modify the bottom inset. For some reason,
            // if the bottom inset is modified, window resizing stops working.
            mInsets[0] = insets.left;
            mInsets[1] = insets.top;
            mInsets[2] = insets.right;
            insets.left = 0;
            insets.top = 0;
            insets.right = 0;
        }

        return super.fitSystemWindows(insets);
    }

    @Override
    public final WindowInsets onApplyWindowInsets(WindowInsets insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            mInsets[0] = insets.getSystemWindowInsetLeft();
            mInsets[1] = insets.getSystemWindowInsetTop();
            mInsets[2] = insets.getSystemWindowInsetRight();
            return super.onApplyWindowInsets(insets.replaceSystemWindowInsets(0, 0, 0,
                    insets.getSystemWindowInsetBottom()));
        } else {
            return insets;
        }
    }
}
