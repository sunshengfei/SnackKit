package com.freddon.android.snackkit.extension.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Created by fred on 2017/1/8.
 */

public class TypedProgressBar extends ProgressBar {
    public TypedProgressBar(Context context) {
        this(context,null);
    }

    public TypedProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TypedProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

}
