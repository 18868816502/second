package com.beihui.market.ui.dialog;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.beihui.market.R;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.viewutils.ToastUtils;

import java.io.UnsupportedEncodingException;

public class DebtChannelNewDialog extends DialogFragment {

    private EditText channelName;

    private DebtAddChannelListener listener;

    public interface DebtAddChannelListener {
        void onChannelAdded(String channelName);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_debt_channel_add, container, false);
        channelName = (EditText) view.findViewById(R.id.channel_name);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.cancel) {
                    InputMethodUtil.closeSoftKeyboard(getContext(), channelName);

                    channelName.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dismiss();
                        }
                    }, 100);
                } else {
                    String name = channelName.getText().toString();

                    if (name.length() > 0) {
                        if (listener != null) {
                            listener.onChannelAdded(name);
                        }
                        InputMethodUtil.closeSoftKeyboard(getContext(), channelName);
                        channelName.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dismiss();
                            }
                        }, 100);
                    } else {
                        ToastUtils.showShort(getContext(), "请输入渠道名", null);
                    }
                }
            }
        };
        view.findViewById(R.id.cancel).setOnClickListener(clickListener);
        view.findViewById(R.id.confirm).setOnClickListener(clickListener);

        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodUtil.openSoftKeyboard(getContext(), channelName);
            }
        }, 100);

        channelName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Editable editable = channelName.getText();
                if (editable.length() > 0) {
                    try {
                        if (editable.toString().getBytes("gb2312").length > 10) {
                            editable.delete(editable.length() - 1, editable.length());
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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

    public DebtChannelNewDialog setDebtChannelAddListener(DebtAddChannelListener listener) {
        this.listener = listener;
        return this;
    }
}
