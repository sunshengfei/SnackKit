package com.freddon.android.snackkit.extension.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by fred on 2017/3/18.
 */

public class CommandTextBox extends EditText {


    private int defaultTextColor= Color.GREEN;
    private int defaultBgColor= Color.BLACK;
    private int defaultHlColor= Color.RED;


    public CommandTextBox(Context context) {
        super(context);
    }

    public CommandTextBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommandTextBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //bg
        super.onDraw(canvas);
        //fg
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_ENTER){

        }
        return super.onKeyUp(keyCode, event);
    }
}
