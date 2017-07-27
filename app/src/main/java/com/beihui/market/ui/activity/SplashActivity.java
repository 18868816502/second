package com.beihui.market.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.util.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.ad_image)
    ImageView adImageView;
    @BindView(R.id.ignore)
    TextView ignoreTv;
    @BindView(R.id.bottom_logo)
    ImageView bottomLogoIv;

    private AdTimer adTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        int height = CommonUtils.getBottomStatusHeight(this);
        bottomLogoIv.setPadding(0, 0, 0, ((int) getResources().getDisplayMetrics().density * 80) - height);
        checkAd();
    }

    @Override
    protected void onDestroy() {
        if (adTimer != null) {
            adTimer.cancel();
        }
        adTimer = null;
        super.onDestroy();
    }

    private void launch() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void checkAd() {
        ignoreTv.setText("3  跳过");
        ignoreTv.setVisibility(View.VISIBLE);
        adTimer = new AdTimer(4 * 1000, 1000);
        adTimer.start();
    }

    private class AdTimer extends CountDownTimer {

        AdTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            ignoreTv.setText((millisUntilFinished / 1000) + "  跳过");
        }

        @Override
        public void onFinish() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    launch();
                }
            });
        }
    }
}
