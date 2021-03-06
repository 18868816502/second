package com.beiwo.qnejqaz.util;


import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.widget.EditText;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;

public class CountDownTimerUtils extends CountDownTimer {
    private TextView targetView;
    private EditText phoneInput;
    private boolean isRunning;

    public CountDownTimerUtils(TextView targetView, EditText phoneInput) {
        super(60 * 1000, 1000);
        this.targetView = targetView;
        this.phoneInput = phoneInput;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTick(long millisUntilFinished) {
        isRunning = true;
        targetView.setBackgroundResource(R.drawable.round_login_btn_grey);
        targetView.setEnabled(false); //设置不可点击
        targetView.setText(millisUntilFinished / 1000 + "s");  //设置倒计时时间
    }

    @Override
    public void onFinish() {
        isRunning = false;
        targetView.setBackgroundResource(R.drawable.round_login_btn);
        targetView.setText("重新获取");
        targetView.setEnabled(true);
    }

    public boolean isRunning() {
        return isRunning;
    }
}