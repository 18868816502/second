package com.beihui.market.ui.activity;


import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
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
import butterknife.OnClick;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class RewardPointActivity extends BaseComponentActivity {

    private static final int REQUEST_CODE_MARKET = 1;
    private static final int REQUEST_CODE_INVITATION = 2;
    private static final int REQUEST_CODE_ADD_DEBT = 3;

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
    private String pendingTaskId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_reward_points;
    }

    @SuppressLint({"AddJavascriptInterface"})
    @Override
    public void configViews() {
        setupToolbar(toolbar, true);
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

        webView.addJavascriptInterface(this, "android");

        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        webView.loadUrl(NetConstants.generateRewardPointsUrl(UserHelper.getInstance(this).getProfile().getId()));
        titleList.push("我的积分");
        title.setText("我的积分");
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        webView.reload();
        if (requestCode == REQUEST_CODE_MARKET) {
            //打开应用市场超过10秒钟，就视为评论
            if (System.currentTimeMillis() - marketStartTime >= 10 * 1000) {
                checkMarketCommentReward(pendingTaskId);
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            titleList.pop();
            String pageTitle = titleList.peek();
            title.setText(pageTitle);
        } else {
            super.onBackPressed();
        }
    }

    @OnClick({R.id.close, R.id.explain})
    void onBindViewClicked(View view) {
        if (view.getId() == R.id.close) {
            finish();
        } else if (view.getId() == R.id.explain) {
            webView.loadUrl("javascript:explain()");
        }
    }

    @JavascriptInterface
    public void goToAppStore(final String taskId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                marketStartTime = System.currentTimeMillis();
                pendingTaskId = taskId;
                try {
                    startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getApplicationInfo().packageName)), REQUEST_CODE_MARKET);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @JavascriptInterface
    public void inviteFriend() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startActivityForResult(new Intent(RewardPointActivity.this, InvitationActivity.class), REQUEST_CODE_INVITATION);
            }
        });
    }

    @JavascriptInterface
    public void goToAddDebt() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startActivityForResult(new Intent(RewardPointActivity.this, DebtSourceActivity.class), REQUEST_CODE_ADD_DEBT);
            }
        });
    }

    private void checkMarketCommentReward(String taskId) {
        final UserHelper.Profile profile = UserHelper.getInstance(this).getProfile();
        if (profile != null) {
            final Api api = Api.getInstance();
            api.addRewardPoint(profile.getId(), taskId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .filter(new Predicate<ResultEntity>() {
                        @Override
                        public boolean test(ResultEntity result) throws Exception {
                            if (!result.isSuccess()) {
                                ToastUtils.showShort(RewardPointActivity.this, result.getMsg(), null);
                            }
                            return result.isSuccess();
                        }
                    })
                    .observeOn(Schedulers.io())
                    .flatMap(new Function<ResultEntity, ObservableSource<ResultEntity<List<RewardPoint>>>>() {
                        @Override
                        public ObservableSource<ResultEntity<List<RewardPoint>>> apply(ResultEntity resultEntity) throws Exception {
                            return api.queryRewardPoints(profile.getId(), Constant.REWARD_POINTS_TASK_NAME_COMMENT);
                        }
                    })
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
}
