package com.beihui.market.util;

/**
 * Created by Administrator on 2017/1/22.
 * 倒计时
 */

import android.os.CountDownTimer;
import android.widget.TextView;

public class DownTimerUtils extends CountDownTimer {
    private TextView mTextView;

    /**
     * @param textView          The TextView
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receiver
     *                          {@link #onTick(long)} callbacks.
     */
    public DownTimerUtils(TextView textView, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mTextView = textView;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mTextView.setText(millisUntilFinished / 1000 + "");  //设置倒计时时间
    }

    @Override
    public void onFinish() {
        mTextView.setText("0");
    }
}