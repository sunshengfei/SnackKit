package com.freddon.android.snackkit.extension.widget.layer;

import android.content.Context;
import android.util.AttributeSet;

import com.freddon.android.snackkit.extension.widget.tab.cell.BaseCell;

/**
 * Created by fred on 16/7/25.
 */
public class GridCell extends BaseCell {

    public GridCell(Context context) {
        this(context, null);
    }

    public GridCell(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridCell(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public GridCell(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
    }

}
