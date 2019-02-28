package com.freddon.android.snackkit.extension.widget;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;

/**
 * Created by fred on 2018/6/11.
 */

public class RowsTextView extends androidx.appcompat.widget.AppCompatTextView {


    public RowsTextView(Context context) {
        super(context);
    }

    public RowsTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RowsTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private int getAvailableWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int getAvailableHeight() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }


    /**
     * 判断是否溢出
     *
     * @param lineCount 溢出行数
     * @return
     */
    public boolean isOverFlowedHoz(int lineCount) {
        Paint paint = getPaint();
        float width = paint.measureText(getText().toString());
        if (width > lineCount * getAvailableWidth()) return true;
        return false;
    }
}