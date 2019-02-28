package com.freddon.android.snackkit.extension.widget.tab.cell;

import android.content.Context;
import android.util.AttributeSet;

import com.freddon.android.snackkit.R;

import butterknife.ButterKnife;

/**
 * Created by fred on 16/7/25.
 */
public class MenuCell extends BaseCell {

    public MenuCell(Context context) {
        this(context, null);
    }

    public MenuCell(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuCell(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MenuCell(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_grid_menu;
    }

    @Override
    public void onInflated() {
        super.onInflated();
        ButterKnife.bind(this);
    }



}
