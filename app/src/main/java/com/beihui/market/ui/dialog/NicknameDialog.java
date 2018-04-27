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

import com.beihui.market.R;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.viewutils.ToastUtils;

public class NicknameDialog extends DialogFragment {

    private EditText etNickName;

    private NickNameChangedListener listener;

    private String nickName;

    public interface NickNameChangedListener {
        void onNickNameChanged(String amount);
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
        View view = inflater.inflate(R.layout.dialog_nickname_edit, container, false);
        etNickName = view.findViewById(R.id.et_nickname);
        if (!TextUtils.isEmpty(nickName)){
            etNickName.setText(nickName);
        }
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.cancel) {
                    InputMethodUtil.closeSoftKeyboard(getContext(), etNickName);

                    etNickName.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dismiss();
                        }
                    }, 100);
                } else {
                    String nickname = etNickName.getText().toString();

                    if (nickname.length() > 0) {
                        if (listener != null) {
                            listener.onNickNameChanged(nickname);
                        }

                        InputMethodUtil.closeSoftKeyboard(getContext(), etNickName);
                        etNickName.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dismiss();
                            }
                        }, 100);
                    } else {
                        ToastUtils.showShort(getContext(), "请输入昵称", null);
                    }
                }
            }
        };
        view.findViewById(R.id.cancel).setOnClickListener(clickListener);
        view.findViewById(R.id.confirm).setOnClickListener(clickListener);

        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodUtil.openSoftKeyboard(getContext(), etNickName);
            }
        }, 100);

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

    public NicknameDialog setNickNameChangedListener(NickNameChangedListener listener) {
        this.listener = listener;
        return this;
    }

    public NicknameDialog setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }
}
