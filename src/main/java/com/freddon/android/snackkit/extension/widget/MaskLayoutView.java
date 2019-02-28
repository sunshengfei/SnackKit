package com.freddon.android.snackkit.extension.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import androidx.annotation.DrawableRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;


import com.freddon.android.snackkit.R;
import com.freddon.android.snackkit.extension.animation.AnimationCenter;

/**
 * Created by fred on 2016/11/5.
 */

public class MaskLayoutView extends ConstraintLayout {
    ImageView ivIconMaskLayers;
    TextView tvMsgMaskLayers;

    public MaskLayoutView(Context context) {
        this(context, null);
    }

    public MaskLayoutView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaskLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.MaskLayoutView);
            int n = a.getIndexCount();
            for (int i = 0; i < n; i++) {
                int attr = a.getIndex(i);
                if (attr == R.styleable.MaskLayoutView_maskIcon) {
                    setIcon(a.getDrawable(attr));
                } else if (attr == R.styleable.MaskLayoutView_maskText) {
                    setText(a.getString(attr));
                }
            }
            a.recycle();
        }
    }

    private void initLayout(Context context) {
        View.inflate(context, R.layout.mask_layers_layout, this);
        ivIconMaskLayers=findViewById(R.id.iv_icon_kit_mask_layers);
        tvMsgMaskLayers=findViewById(R.id.tv_msg_kit_mask_layers);
        ivIconMaskLayers.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }


    // region : @fred  [2016/11/5]

    public void setFPIcon(@DrawableRes int resId) {
        setIcon(resId);
    }

    public void setIcon(@DrawableRes int resId) {
        ivIconMaskLayers.setImageResource(resId);
    }

    public void setIcon(Drawable drawable) {
        ivIconMaskLayers.setImageDrawable(drawable);
    }

    public void setText(CharSequence charSequence) {
        if (charSequence == null) {
            charSequence = "";
        }
        tvMsgMaskLayers.setText(charSequence);
    }
    public void setText(String str) {
        if (str == null) {
            str = "";
        }
        setText(Html.fromHtml(str));
    }

    public void showWithAnimation(Animation animation) {
        if (animation == null) {
            AnimationCenter.showAnimation(this, 400);
        } else {
            this.startAnimation(animation);
        }
    }


    // endregion
}
