package com.beihui.market.ui.activity;


import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.AdBanner;
import com.beihui.market.entity.request.RequestConstants;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerSplashComponent;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.RxUtil;
import com.bumptech.glide.Glide;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class SplashActivity extends BaseComponentActivity {
    @BindView(R.id.ad_image)
    ImageView adImageView;
    @BindView(R.id.ignore)
    TextView ignoreTv;
    @BindView(R.id.bottom_logo)
    ImageView bottomLogoIv;

    private AdTimer adTimer;

    @Inject
    Api api;

    private Disposable disposable;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void configViews() {
        int height = CommonUtils.getBottomStatusHeight(this);
        bottomLogoIv.setPadding(0, 0, 0, ((int) getResources().getDisplayMetrics().density * 80) - height);
        ignoreTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(null);
            }
        });
    }

    @Override
    public void initDatas() {
        checkAd();
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerSplashComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    protected void onDestroy() {
        if (adTimer != null) {
            adTimer.cancel();
        }
        adTimer = null;
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        super.onDestroy();
    }

    private void launch(String url) {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
        finish();
    }

    private void checkAd() {
        disposable = api.querySupernatant(RequestConstants.SUP_TYPE_AD)
                .compose(RxUtil.<ResultEntity<List<AdBanner>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<AdBanner>>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<List<AdBanner>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           startAd(result.getData().get(0));
                                       } else {
                                           launch(null);
                                       }
                                   } else {
                                       launch(null);
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                launch(null);
                            }
                        });
    }

    private void startAd(AdBanner ad) {
        ignoreTv.setVisibility(View.VISIBLE);
        ignoreTv.setText("3  跳过");
        adTimer = new AdTimer(3 * 1000, 1000);
        adTimer.start();

        if (ad != null && ad.getImgUrl() != null) {
            adImageView.setVisibility(View.VISIBLE);
            adImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            Glide.with(this)
                    .load(ad.getImgUrl())
                    .into(adImageView);
        }
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
            launch(null);
        }
    }
}
