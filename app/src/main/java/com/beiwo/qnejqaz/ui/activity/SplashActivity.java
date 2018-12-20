package com.beiwo.qnejqaz.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.qnejqaz.App;
import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.api.Api;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.entity.AdBanner;
import com.beiwo.qnejqaz.entity.Audit;
import com.beiwo.qnejqaz.helper.DataHelper;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.beiwo.qnejqaz.tang.rx.RxResponse;
import com.beiwo.qnejqaz.tang.rx.observer.ApiObserver;
import com.beiwo.qnejqaz.ui.busevents.UserLoginWithPendingTaskEvent;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

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
    @BindView(R.id.tv_skip)
    TextView tv_skip;

    private Activity activity;
    private Timer timer = new Timer();
    private int skipTime = 4;
    private Handler handler = new Handler(Looper.getMainLooper());
    private UserHelper userHelper;
    private boolean adClicked = false;
    private boolean stopExcuted;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void configViews() {
        activity = this;
        userHelper = UserHelper.getInstance(this);
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

    private void adData() {
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
                                        tv_skip.setVisibility(View.VISIBLE);
                                        skipTime--;
                                        if (skipTime == 0) {
                                            if (timer != null) timer.cancel();
                                        }

                                        tv_skip.setText(skipTime + "s");
                                        if (activity == null) return;
                                        Glide.with(activity)
                                                .load(ad.getImgUrl())
                                                .error(R.color.transparent)
                                                .into(iv_spread);
                                        iv_spread.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (handler != null)
                                                    handler.removeCallbacksAndMessages(null);
                                                adClicked = true;
                                                //统计点击
                                                DataHelper.getInstance(activity).onAdClicked(ad.getId(), 1);
                                                //pv，uv统计
                                                DataHelper.getInstance(activity).onCountUv(DataHelper.ID_CLICK_SPLASH_AD);
                                                //是否需要登录
                                                if (ad.needLogin() && !userHelper.isLogin()) {
                                                    EventBus.getDefault().post(new UserLoginWithPendingTaskEvent(ad));
                                                    MainActivity.init(activity, ad);
                                                } else goProduct(ad);
                                            }
                                        });
                                    } else tv_skip.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        }, 1000, 1000);
    }

    private void goProduct(final AdBanner ad) {
        if (!TextUtils.isEmpty(ad.getUrl())) {
            String url = ad.getUrl();
            if (url.contains("USERID") && userHelper.isLogin()) {
                url = url.replace("USERID", userHelper.id());
            }
            Intent intent = new Intent(activity, ComWebViewActivity.class);
            intent.putExtra("title", ad.getTitle());
            intent.putExtra("url", url);
            startActivity(intent);
        } else if (!TextUtils.isEmpty(ad.getLocalId())) {
            String id = userHelper.isLogin() ? userHelper.id() : App.androidId;
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

    @Override
    protected void onResume() {
        super.onResume();
        if (adClicked) MainActivity.init(activity, null);
        if (stopExcuted) MainActivity.init(activity, null);
    }

    @Override
    public void initDatas() {
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopExcuted = true;
        if (timer != null) timer.cancel();
        if (handler != null) handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onBackPressed() {//取消back按钮事件
        //super.onBackPressed();
    }

    @OnClick({R.id.tv_skip})
    public void onClick(View view) {
        if (timer != null) timer.cancel();
        handler.removeCallbacksAndMessages(null);
        if (App.audit == 2) MainActivity.init(activity, null);
        else VestMainActivity.init(activity);
    }

    private void launch() {
        if (App.audit == 2) {
            adData();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MainActivity.init(activity, null);
                }
            }, 4000);
        } else VestMainActivity.init(this);
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
        activity = null;
    }
}