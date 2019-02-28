package com.freddon.android.snackkit.extension.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.freddon.android.snackkit.R;


/**
 * Created by fred on 2016/11/22.
 */

public class CommentBarView extends LinearLayout implements View.OnClickListener {


    LinearLayout viewPraise;
    LinearLayout viewComments;
    LinearLayout viewCommentDock;

    CheckBox cbPraise;
    CheckBox cbComments;

    public CommentBarView(Context context) {
        this(context, null);
    }

    public CommentBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.toolbar_comment, this);


        viewPraise=findViewById(R.id.view_praise);
        viewComments=findViewById(R.id.view_comments);
        viewCommentDock=findViewById(R.id.view_comment_dock);

        viewPraise.setOnClickListener(this);
        viewComments.setOnClickListener(this);
        viewCommentDock.setOnClickListener(this);

        cbPraise=findViewById(R.id.cb_praise);
        cbComments=findViewById(R.id.cb_comments);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.view_praise) {
            if (onCommentBarClickListener != null) {
                onCommentBarClickListener.onPraiseClick(view);
            }

        } else if (i == R.id.view_comments) {
            if (onCommentBarClickListener != null) {
                onCommentBarClickListener.onCommentsClick(view);
            }

        } else if (i == R.id.view_comment_dock) {
            if (onCommentBarClickListener != null) {
                onCommentBarClickListener.onCommentClick(view);
            }

        }
    }

    public void setPraiseChecked(boolean isChecked) {
        cbPraise.setChecked(isChecked);
    }


    public void setPraiseCount(int count) {
        cbPraise.setText(String.valueOf(count));
    }

    public void setCommentsCount(int count) {
        cbComments.setText(String.valueOf(count));
    }


    public void setOnCommentBarClickListener(OnCommentBarClickListener onCommentBarClickListener) {
        this.onCommentBarClickListener = onCommentBarClickListener;
    }

    private OnCommentBarClickListener onCommentBarClickListener;

    public interface OnCommentBarClickListener {

        void onPraiseClick(View view);

        void onCommentsClick(View view);

        void onCommentClick(View view);
    }
}
