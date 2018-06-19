package com.beihui.market.ui.dialog;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.viewutils.ToastUtils;
import com.beihui.market.view.EditTextUtils;

/**
 * 修改备注的对话框
 */
public class TextViewDialog extends DialogFragment {

    private TextView textView;

    public String content = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_text_view, container, false);


        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.rv_cancel) {
                    dismiss();
                } else if (v.getId() == R.id.tv_confirm) {
                    getActivity().finish();
                }
            }
        };
        view.findViewById(R.id.rv_cancel).setOnClickListener(clickListener);
        view.findViewById(R.id.tv_confirm).setOnClickListener(clickListener);
        textView = (TextView) view.findViewById(R.id.tv_content);
        if (!TextUtils.isEmpty(content)) {
            textView.setText(content);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = (int) (getResources().getDisplayMetrics().density * 280);
            window.setAttributes(lp);
        }
    }


    public TextViewDialog setName(String content) {
        this.content = content;
        return this;
    }
}
