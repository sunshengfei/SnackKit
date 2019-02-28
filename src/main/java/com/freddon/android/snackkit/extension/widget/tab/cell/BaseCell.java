package com.freddon.android.snackkit.extension.widget.tab.cell;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.freddon.android.snackkit.R;
import com.freddon.android.snackkit.extension.animation.AnimationCenter;

/**
 * Created by fred on 16/7/25.
 */
public class BaseCell extends RelativeLayout {

    protected ImageView ivIcon;
    protected TextView tvTitle;

    public int getLayoutId() {
        return 0;
    }

    public BaseCell(Context context) {
        this(context, null);
    }

    public BaseCell(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseCell(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public void onInflated(){}

    public BaseCell(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        LayoutInflater layoutInflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (layoutInflater!=null){
            if (getLayoutId() > 0) {
                layoutInflater.inflate(getLayoutId(), this);
                onInflated();
            } else {
                layoutInflater.inflate(R.layout.ext_grid_cell, this);
                ivIcon=findViewById(R.id.iv_ext_icon);
                tvTitle=findViewById(R.id.tv_ext_title);
            }
        }
        TypedArray typedArray = context.obtainStyledAttributes(
                attrs, R.styleable.BaseCell, defStyleAttr, defStyleRes);
        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.BaseCell_text) {
                setLabelText(typedArray.getString(attr));

            } else if (attr == R.styleable.BaseCell_textSize) {
                setLabelTextSize(typedArray.getDimensionPixelSize(attr, 16));

            } else if (attr == R.styleable.BaseCell_textColor) {
                setLabelTextColor(typedArray.getColor(attr, Color.BLACK));

            } else if (attr == R.styleable.BaseCell_tabIcon) {
                setIcon(typedArray.getResourceId(attr, R.drawable.icon_msg));

            } else if (attr == R.styleable.BaseCell_scaleType) {
                setScaleType(ImageView.ScaleType.valueOf(String.valueOf(typedArray.getText(attr))));
            }
        }
        typedArray.recycle();
    }

    @Override
    public void setSelected(boolean selected) {
        if (tvTitle == null || ivIcon == null) return;
        tvTitle.setSelected(selected);
        ivIcon.setSelected(selected);
        if (selected) ivIcon.startAnimation(AnimationCenter.showBulbAnimation(0.85F, 1F));
    }

    public void setLabelTextSize(int dimensionPixelSize) {
        if (tvTitle == null) return;
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimensionPixelSize);
    }

    public void setScaleType(ImageView.ScaleType type) {
        if (ivIcon == null) return;
        if (type == null) ivIcon.setScaleType(ImageView.ScaleType.CENTER);
        ivIcon.setScaleType(type);
    }

    public void setIcon(@DrawableRes int resId) {
        if (ivIcon == null) return;
        ivIcon.setImageResource(resId);
    }

    public void setIcon(Drawable drawable) {
        if (ivIcon == null) return;
        ivIcon.setImageDrawable(drawable);
    }


    public void setLabelText(@StringRes int resid) {
        if (tvTitle == null) return;
        tvTitle.setText(resid);
    }

    public void setLabelText(CharSequence charSequence) {

        if (tvTitle == null) return;
        tvTitle.setText(charSequence);
    }

    public void setLabelTextColor(@ColorInt int color) {
        if (tvTitle == null) return;
        tvTitle.setTextColor(color);
    }


}
