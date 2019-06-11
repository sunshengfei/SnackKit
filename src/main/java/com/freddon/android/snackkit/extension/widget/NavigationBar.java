package com.freddon.android.snackkit.extension.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Outline;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.freddon.android.snackkit.R;

public class NavigationBar extends LinearLayout implements View.OnClickListener {

    private ImageView backIcon;
    private TextView title;
    private TextView subtitle;
    private ImageView menuCustom;
    private ImageView menuMore;
    private ViewGroup menuLayout;

    public NavigationBar(Context context) {
        this(context, null);
    }

    public NavigationBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavigationBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context);
    }

    private void initLayout(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (layoutInflater != null) {
            layoutInflater.inflate(R.layout.p_toolbar, this);
            backIcon = findViewById(R.id.backIcon);
            title = findViewById(R.id.title);
            subtitle = findViewById(R.id.subtitle);
            menuLayout = findViewById(R.id.menuLayout);
            menuCustom = findViewById(R.id.menuCustom);
            menuMore = findViewById(R.id.menuMore);
            title.setOnClickListener(this);
            backIcon.setOnClickListener(this);
            menuCustom.setOnClickListener(this);
            menuMore.setOnClickListener(this);
        }
    }


    public void setTitle(CharSequence title) {
        if (title != null) {
            this.title.setText(title);
        }
    }

    public void setSubTitle(CharSequence title) {
        if (title != null) {
            this.subtitle.setText(title);
        }
        this.subtitle.setVisibility(title != null ? VISIBLE : GONE);
    }


    @Override
    public void onClick(View v) {
        if (onNavigationEvent == null) {
            return;
        }
        int id = v.getId();
        if (id == R.id.backIcon) {
            if (onNavigationEvent.onBackClicked()) {
                return;
            }
            if (getContext() != null && getContext() instanceof Activity) {
                ((Activity) getContext()).onBackPressed();
            }
        } else if (id == R.id.title) {
            onNavigationEvent.onTitleClicked();
        } else if (id == R.id.menuCustom) {
            onNavigationEvent.onMenuCustomClicked();
        } else if (id == R.id.menuMore) {
            onNavigationEvent.onMenuMoreClicked();
        }
    }

    public void setOnNavigationEvent(OnNavigationEvent onNavigationEvent) {
        this.onNavigationEvent = onNavigationEvent;
    }

    private OnNavigationEvent onNavigationEvent;

    public void showBack() {
        if (backIcon != null)
            backIcon.setVisibility(VISIBLE);
    }

    public void hideBack() {
        if (backIcon != null)
            backIcon.setVisibility(GONE);
    }

    public void setBackIcon(int res) {
        if (backIcon != null) {
            backIcon.setImageResource(res);
        }
    }

    public interface OnNavigationEvent {

        boolean onBackClicked();

        void onTitleClicked();

        void onMenuCustomClicked();

        void onMenuMoreClicked();
    }
}
