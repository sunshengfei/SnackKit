package com.freddon.android.snackkit.extension.widget.container;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.freddon.android.snackkit.R;


public class WrapViewContainer extends FrameLayout {
    private final static String TAG = "WrapViewContainer";

    private int maxLine = -1;    //最大行数
    private int lineWidth;     //行间距
    private int horizontalSpace;   //水平间距
    boolean isMinWidthMode;

    private int lastShownPosition;  //最后一个显示的view位置
    int row;


    public int getMaxLine() {
        return maxLine;
    }

    public void setMaxLine(int maxLine) {
        this.maxLine = maxLine;
    }

    public WrapViewContainer(Context context) {
        super(context);
    }

    public WrapViewContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.WrapViewContainer);
        lineWidth = (int) a.getDimension(R.styleable.WrapViewContainer_lineWidth, 0);
        horizontalSpace = (int) a.getDimension(R.styleable.WrapViewContainer_horizontalSpace, 0);
        maxLine = a.getInteger(R.styleable.WrapViewContainer_maxLine, -1);
        isMinWidthMode = a.getBoolean(R.styleable.WrapViewContainer_minWidthMode, false);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Log.d(TAG,"widthMeasureSpec = "+widthMeasureSpec+" heightMeasureSpec"+heightMeasureSpec);

        row = 0;
        int column = 0;
        int height = 0;
        int width = 0;
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int layoutWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
//        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);
        int maxLineWidth = 0;  //最宽的一行宽度
        int cHeight = 0;    //一个view的高度
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            cHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            int cWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;  //一个子控件的宽度
            if (width + cWidth + (i == 0 ? 0 : horizontalSpace) <= layoutWidth) {
                width += cWidth + (i == 0 ? 0 : horizontalSpace);
                if (width > maxLineWidth) {
                    maxLineWidth = width;
                }
            } else {
                if (width > maxLineWidth) {
                    maxLineWidth = width;
                }
                if (maxLine == -1 || row < maxLine - 1) {
                    row++;
                    width = cWidth;
                } else {
                    break;
                }
            }
            lastShownPosition = i;
//            if (height > maxHeight) {
//                height = maxHeight;
//            }
        }
        height = row * cHeight + cHeight + lineWidth * row + getPaddingBottom() + getPaddingTop();
        if (isMinWidthMode) {
            setMeasuredDimension(maxLineWidth, height);
        } else {
            setMeasuredDimension(maxWidth, height);
        }
    }

    public boolean isMinWidthMode() {
        return isMinWidthMode;
    }

    public void setIsMinWidthMode(boolean isMinWidthMode) {
        this.isMinWidthMode = isMinWidthMode;
    }

    public int getRow() {
        return row;
    }

    public int getLastShownPosition() {
        return lastShownPosition;
    }

    public void setLastShownPosition(int lastShownPosition) {
        this.lastShownPosition = lastShownPosition;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int row = 0; // which row lay you view relative to parent
        int lengthX = 0; // right position of child relative to parent
        int lengthY = 0; // bottom position of child relative to parent
        for (int i = 0; i < count; i++) {
            final View child = this.getChildAt(i);
            FrameLayout.LayoutParams lp = (LayoutParams) child.getLayoutParams();
            int width = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin; // 一个控件占据的总的宽度
            int height = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin; // 一个控件占据的高度
            if (i == 0) {
                lengthX = width + getPaddingLeft();
            } else if (lengthX + width + horizontalSpace < r - l - getPaddingRight()) {
                lengthX += width + horizontalSpace;
            } else {
                lengthX = width + getPaddingLeft();
                row++;
            }
            lengthY = row * height + height + lineWidth * row + getPaddingTop();
            child.layout(lengthX - width - lp.leftMargin, lengthY - height + lp.topMargin, lengthX - lp.rightMargin,
                    lengthY - lp.bottomMargin);
        }
    }

}
