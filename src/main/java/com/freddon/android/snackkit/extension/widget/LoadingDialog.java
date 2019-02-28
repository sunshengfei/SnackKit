package com.freddon.android.snackkit.extension.widget;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.freddon.android.snackkit.R;

/**
 * Created by fred on 2017/4/26.
 */

public class LoadingDialog extends DialogFragment {


    private TextView tvMsg;

    private final static String BUNDLE_MSG_TEXT = "BUNDLE_MSG_TEXT";


    public static LoadingDialog build(CharSequence msgText) {
        LoadingDialog dialog = new LoadingDialog();
        Bundle bundle = new Bundle();
        bundle.putCharSequence(BUNDLE_MSG_TEXT, msgText);
        dialog.setArguments(bundle);
        return dialog;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inset_loading, container, false);
        tvMsg = (TextView) view.findViewById(R.id.tv_msg_mask_loading_layers);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        renderArguments();
    }

    private void renderArguments() {
        Bundle bundle = getArguments();
        if (null != bundle && bundle.containsKey(BUNDLE_MSG_TEXT)) {
            CharSequence text = bundle.getCharSequence(BUNDLE_MSG_TEXT);
            tvMsg.setText(text);
        }
    }


}
