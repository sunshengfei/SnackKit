package com.freddon.android.snackkit.extension.widget.tab;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;


import com.freddon.android.snackkit.R;
import com.freddon.android.snackkit.extension.widget.tab.cell.BaseCell;

/**
 * Created by fred on 16/7/25.
 */
public class TabCell extends BaseCell {

    public TabCell(Context context) {
        this(context, null);
    }

    public TabCell(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabCell(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TabCell(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getLayoutId() {
        return R.layout.ext_tab_cell;
    }

    @Override
    public void onInflated() {
        super.onInflated();
        ivIcon = findViewById(R.id.iv_icon);
        tvTitle = findViewById(R.id.tv_title);
    }

    public void setTintWhenSelected(@ColorInt int color, @ColorInt int lossColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ivIcon.setImageTintList(createColorStateList(lossColor, color, color, lossColor));
            tvTitle.setTextColor(createColorStateList(lossColor, color, color, lossColor));
        }
    }

    private ColorStateList createColorStateList(int normal, int pressed, int focused, int unable) {
        int[] colors = new int[]{pressed, focused, normal, focused, unable, normal};
        int[][] states = new int[6][];
        states[0] = new int[]{android.R.attr.state_selected, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_focused};
        states[4] = new int[]{android.R.attr.state_window_focused};
        states[5] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }


    @Override
    public boolean isSelected() {
        if (ivIcon != null) {
            return ivIcon.isSelected();
        }
        return super.isSelected();
    }
}
