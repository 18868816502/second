package com.beiwo.klyjaz.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.klyjaz.App;
import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.api.ResultEntity;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.entity.AdBanner;
import com.beiwo.klyjaz.entity.request.RequestConstants;
import com.beiwo.klyjaz.helper.DataStatisticsHelper;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.injection.component.DaggerSplashComponent;
import com.beiwo.klyjaz.util.RxUtil;
import com.beiwo.klyjaz.util.SPUtils;
import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
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

    private boolean adClicked;

    private SplashHandler handler;

    @Override
    protected void onDestroy() {
        if (adTimer != null) {
            adTimer.cancel();
        }
        adTimer = null;
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        handler.removeMessages(1);
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void configViews() {
        SPUtils.setValue(this, "splash");
        ignoreTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch();
            }
        });
    }

    @Override
    public void initDatas() {
        handler = new SplashHandler(this);
        Message msg = Message.obtain();
        msg.what = 1;
        handler.sendMessageDelayed(msg, 1000 * 5);

        try {
            String version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            if (version.equals(SPUtils.getLastInstalledVersion(this))) {
                checkAd();
            } else {
                handler.removeMessages(1);
                SPUtils.setLastInstalledVersion(this, version);
                Intent intent = new Intent(this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        } catch (PackageManager.NameNotFoundException e) {
            launch();
        }
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerSplashComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    private void launch() {
        handler.removeMessages(1);
        if (!adClicked) {
            if(App.audit == 1){
                Intent intent = new Intent(this, VestMainActivity.class);
                startActivity(intent);
            }else{
                MainActivity.main(this);
            }
            finish();
        }
    }

    private void checkAd() {
        disposable = api.querySupernatant(RequestConstants.SUP_TYPE_AD)
                .compose(RxUtil.<ResultEntity<List<AdBanner>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<AdBanner>>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<List<AdBanner>> result) throws Exception {
                                   handler.removeMessages(1);
                                   if (result.isSuccess()) {
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           startAd(result.getData().get(0));
                                       } else {
                                           launch();
                                       }
                                   } else {
                                       launch();
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                launch();
                            }
                        });
    }

    private void startAd(final AdBanner ad) {
        ignoreTv.setVisibility(View.VISIBLE);
        adTimer = new AdTimer(3 * 1000, 1000);
        adTimer.start();

        if (ad != null && ad.getImgUrl() != null) {
            adImageView.setVisibility(View.VISIBLE);
            adImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adClicked = true;
                    Context context = SplashActivity.this;
                    //统计点击
                    DataStatisticsHelper.getInstance().onAdClicked(ad.getId(), 1);

                    //pv，uv统计
                    DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_CLICK_SPLASH_AD);

                    //先跳转至首页
                    Intent intent = new Intent(context, App.audit == 2 ? MainActivity.class: VestMainActivity.class);
                    startActivity(intent);

                    //需要先登录并且用户还没登录
                    if (ad.needLogin() && UserHelper.getInstance(SplashActivity.this).getProfile() == null) {
                        UserAuthorizationActivity.launchWithPending(SplashActivity.this, ad);
                    } else {
                        //跳Native还是跳Web
                        if (ad.isNative()) {
                            intent = new Intent(context, LoanDetailActivity.class);
                            intent.putExtra("loanId", ad.getLocalId());
                            intent.putExtra("loanName", ad.getTitle());
                            startActivity(intent);
                        } else if (!TextUtils.isEmpty(ad.getUrl())) {
                            //跳转网页时，url不为空情况下才跳转
                            String url = ad.getUrl();
                            if (url.contains("USERID") && UserHelper.getInstance(SplashActivity.this).getProfile() != null) {
                                url = url.replace("USERID", UserHelper.getInstance(SplashActivity.this).getProfile().getId());
                            }
                            intent = new Intent(context, ComWebViewActivity.class);
                            intent.putExtra("url", url);
                            intent.putExtra("title", ad.getTitle());
                            startActivity(intent);
                        }
                    }

                    finish();
                }
            });

            Glide.with(this)
                    .load(ad.getImgUrl())
                    .asBitmap()
                    .into(adImageView);
        }
    }

    @Override
    public void finish() {
        override = false;
        super.finish();
    }

    private class AdTimer extends CountDownTimer {

        AdTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            ignoreTv.setText((millisUntilFinished / 1000 + 1) + "  跳过");
        }

        @Override
        public void onFinish() {
            launch();
        }
    }

    private static class SplashHandler extends Handler {
        private WeakReference<SplashActivity> weakReference;

        SplashHandler(SplashActivity splashActivity) {
            super(Looper.getMainLooper());
            weakReference = new WeakReference<>(splashActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1 && weakReference.get() != null) {
                weakReference.get().launch();
            }
        }
    }
}
