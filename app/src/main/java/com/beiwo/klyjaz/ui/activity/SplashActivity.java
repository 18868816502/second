package com.beiwo.klyjaz.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.beiwo.klyjaz.App;
import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.entity.AdBanner;
import com.beiwo.klyjaz.entity.Audit;
import com.beiwo.klyjaz.helper.DataStatisticsHelper;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/11/1
 */

public class SplashActivity extends BaseComponentActivity {
    @BindView(R.id.iv_spread)
    ImageView iv_spread;

    private Activity activity;
    private Timer timer = new Timer();
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void configViews() {
        activity = this;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //获取启动页广告
                Api.getInstance().querySupernatant(1)
                        .compose(RxResponse.<List<AdBanner>>compatT())
                        .subscribe(new ApiObserver<List<AdBanner>>() {
                            @Override
                            public void onNext(@NonNull List<AdBanner> data) {
                                if (data != null && data.size() > 0) {
                                    final AdBanner ad = data.get(0);
                                    if (ad != null) {
                                        Glide.with(activity)
                                                .load(ad.getImgUrl())
                                                .error(R.color.transparent)
                                                .into(iv_spread);
                                        iv_spread.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (handler != null)
                                                    handler.removeCallbacksAndMessages(null);
                                                //统计点击
                                                DataStatisticsHelper.getInstance(activity).onAdClicked(ad.getId(), 1);
                                                //pv，uv统计
                                                DataStatisticsHelper.getInstance(activity).onCountUv(DataStatisticsHelper.ID_CLICK_SPLASH_AD);
                                                if (!TextUtils.isEmpty(ad.getUrl())) {
                                                    String url = ad.getUrl();
                                                    if (url.contains("USERID") && UserHelper.getInstance(activity).getProfile() != null) {
                                                        url = url.replace("USERID", UserHelper.getInstance(activity).getProfile().getId());
                                                    }
                                                    Intent intent = new Intent(activity, ComWebViewActivity.class);
                                                    intent.putExtra("title", ad.getTitle());
                                                    intent.putExtra("url", url);
                                                    startActivity(intent);
                                                } else if (!TextUtils.isEmpty(ad.getLocalId())) {
                                                    String id = UserHelper.getInstance(activity).isLogin() ? UserHelper.getInstance(activity).id() : App.androidId;
                                                    Api.getInstance().queryGroupProductSkip(id, ad.getLocalId())
                                                            .compose(RxResponse.<String>compatT())
                                                            .subscribe(new ApiObserver<String>() {
                                                                @Override
                                                                public void onNext(@NonNull String data) {
                                                                    Intent intent = new Intent(activity, WebViewActivity.class);
                                                                    intent.putExtra("webViewUrl", data);
                                                                    intent.putExtra("webViewTitleName", ad.getTitle());
                                                                    startActivity(intent);
                                                                }
                                                            });
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        });
            }
        }, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /*是否审核状态*/
                Api.getInstance().audit()
                        .compose(RxResponse.<Audit>compatT())
                        .subscribe(new ApiObserver<Audit>() {
                            @Override
                            public void onNext(Audit data) {
                                App.audit = data.audit;
                                launch();
                            }

                            @Override
                            public void onError(@NonNull Throwable t) {
                                super.onError(t);
                                launch();
                            }
                        });
            }
        }, 3000);
    }

    @Override
    public void initDatas() {
    }

    private void launch() {
        if (App.audit == 2) MainActivity.init(this);
        else VestMainActivity.init(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}