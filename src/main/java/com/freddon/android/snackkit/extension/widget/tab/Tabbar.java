package com.freddon.android.snackkit.extension.widget.tab;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;


/**
 * Created by fred on 16/7/25.
 */
public class Tabbar extends LinearLayout implements View.OnClickListener {

    /**
     * 子节点个数
     * 每个代表一个菜单
     */
    private int childCount;

    public Tabbar(Context context) {
        super(context);
    }

    public Tabbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Tabbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Tabbar(Context context, AttributeSet attrs, int defStyleAttr, int defc) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        init();
    }

    /**
     * 初始化Cell点击监听
     */
    public void init() {
        //双击
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.setTag(i);
            child.setOnClickListener(this);
        }
    }

    public void selectTabbarCell(int selectPosition) {
        selectTabbarCell(selectPosition, -1,-1);
    }

    /**
     * @param selectPosition 选中的cell
     */
    public void selectTabbarCell(int selectPosition, int tintColor, int tintNormal) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.setSelected(i == selectPosition);
            if (tintColor != -1 && tintNormal != -1 && child instanceof TabCell) {
                ((TabCell) child).setTintWhenSelected(tintColor, tintNormal);
            }
        }
    }


    /**
     * TAbbarItem点击事件
     *
     * @param onTabCellClickEvent
     */
    public void setOnTabbarClickEvent(OnTabCellClickEvent onTabCellClickEvent) {
        this.onTabCellClickEvent = onTabCellClickEvent;
    }

    private OnTabCellClickEvent onTabCellClickEvent;

    @Override
    public void onClick(View v) {
//单击
        if (v.getTag() != null && v.getTag() instanceof Integer) {
            int pos = (Integer) v.getTag();
//            selectTabbarCell(pos);
            if (onTabCellClickEvent != null) {
                onTabCellClickEvent.onTabClick(pos, v);
            }
        }
    }

    public interface OnTabCellClickEvent {
        void onTabClick(int position, View v);
    }
}
