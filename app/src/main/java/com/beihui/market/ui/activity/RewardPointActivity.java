package com.beihui.market.ui.activity;


import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.NetConstants;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.base.Constant;
import com.beihui.market.entity.RewardPoint;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.viewutils.ToastUtils;
import com.beihui.market.view.BusinessWebView;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

public class RewardPointActivity extends BaseComponentActivity {

    private static final int REQUEST_CODE_MARKET = 1;
    private static final int REQUEST_CODE_LOGIN = 2;

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.web_view)
    BusinessWebView webView;

    private LinkedList<String> titleList = new LinkedList<>();

    private long marketStartTime;

    @Override
    public int getLayoutId() {
        return R.layout.activity_reward_points;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void configViews() {
        setupToolbar(toolbar);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    if (progressBar.getVisibility() != View.VISIBLE) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        webView.setWebViewClient(new WebViewClient() {

        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("?title=")) {
                    int index = url.lastIndexOf("?title=");
                    String realUrl = url.substring(0, index);
                    String pageTitle = Uri.decode(url.substring(index + 7, url.length()));
                    titleList.push(pageTitle);
                    title.setText(pageTitle);
                    webView.loadUrl(realUrl);
                    return true;
                } else if (url.contains("&title=")) {
                    int index = url.lastIndexOf("&title=");
                    String realUrl = url.substring(0, index);
                    String pageTitle = Uri.decode(url.substring(index + 7, url.length()));
                    titleList.push(pageTitle);
                    title.setText(pageTitle);
                    webView.loadUrl(realUrl);
                    return true;
                }
                return super.shouldOverrideUrlLoading(webView, url);
            }
        });

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        webView.loadUrl(NetConstants.H5_REWARD_POINTS);
        titleList.push("我的积分");
        title.setText("我的积分");
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            titleList.pop();
            String pageTitle = titleList.peek();
            title.setText(pageTitle);
            return true;
        } else {
            return super.onKeyUp(keyCode, event);
        }
    }

    @JavascriptInterface
    public void goToAppStore() {
        marketStartTime = System.currentTimeMillis();
        try {
            Intent toMarket = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getApplicationInfo().packageName));
            startActivityWithoutOverride(toMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void authorize() {
        startActivityForResult(new Intent(this, UserAuthorizationActivity.class), REQUEST_CODE_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_MARKET) {
                //打开应用市场超过10秒钟，就视为评论
                if (System.currentTimeMillis() - marketStartTime >= 10 * 1000) {
                    checkMarketCommentReward();
                }
            } else if (requestCode == REQUEST_CODE_LOGIN) {
                if (UserHelper.getInstance(this).getProfile() != null) {
                    reloadWithUserId();
                }
            }
        }
    }

    private void checkMarketCommentReward() {
        UserHelper.Profile profile = UserHelper.getInstance(this).getProfile();
        if (profile != null) {
            Api.getInstance().queryRewardPoints(profile.getId(), Constant.REWARD_POINTS_TASK_NAME_COMMENT)
                    .compose(RxUtil.<ResultEntity<List<RewardPoint>>>io2main())
                    .subscribe(new Consumer<ResultEntity<List<RewardPoint>>>() {
                                   @Override
                                   public void accept(ResultEntity<List<RewardPoint>> result) throws Exception {
                                       if (result.isSuccess()) {
                                           String msg = null;
                                           if (result.getData() != null && result.getData().size() > 0) {
                                               int points = 0;
                                               for (RewardPoint point : result.getData()) {
                                                   if (point.getFlag() == 1) {
                                                       points += point.getInteg();
                                                       readReward(point.getRecordId());
                                                   }
                                               }
                                               if (points > 0) {
                                                   msg = "应用评论 积分+" + points;
                                               }
                                           }
                                           if (msg != null) {
                                               ToastUtils.showShort(RewardPointActivity.this, msg, R.drawable.toast_smile);
                                           }
                                       }
                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    Log.e(RewardPointActivity.class.getSimpleName(), throwable.toString());
                                }
                            });
        }
    }

    private void readReward(String recordId) {
        Api.getInstance().sendReadPointRead(recordId)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity resultEntity) throws Exception {
                                   if (!resultEntity.isSuccess()) {
                                       Log.e(RewardPointActivity.class.getSimpleName(), resultEntity.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e(RewardPointActivity.class.getSimpleName(), throwable.toString());
                            }
                        });
    }

    private void reloadWithUserId() {

    }
}
