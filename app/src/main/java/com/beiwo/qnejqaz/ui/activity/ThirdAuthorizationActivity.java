package com.beiwo.qnejqaz.ui.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.api.NetConstants;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.helper.SlidePanelHelper;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.beiwo.qnejqaz.umeng.Events;
import com.beiwo.qnejqaz.umeng.Statistic;
import com.gyf.barlibrary.ImmersionBar;

import java.util.List;

import butterknife.BindView;

public class ThirdAuthorizationActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.speediness_back)
    ImageView speedinessBack;
    @BindView(R.id.speediness_cancel)
    ImageView speedinessCancel;
    @BindView(R.id.speediness_refresh)
    ImageView speedinessRefresh;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.web_view)
    WebView webView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_third_authorization;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).init();
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("relevantRecommendations.html")) {
                    //umeng统计
                    Statistic.onEvent(Events.RESUME_RELEVANT_PRODUCT_FROM_ONE_KEY_LOAN);

                    String[] str = url.split("borrowingHighText");
                    String lastStr = str[str.length - 1];

                    Intent intent = new Intent(ThirdAuthorizationActivity.this, RecommendProductActivity.class);
                    intent.putExtra("amount", Integer.parseInt(lastStr.substring(1, lastStr.length())));
                    startActivity(intent);
                    return true;
                } else if (url.contains("?title=")) {
                    int index = url.lastIndexOf("?title=");
                    String realUrl = url.substring(0, index);
                    title.setText(Uri.decode(url.substring(index + 7, url.length())));
                    webView.loadUrl(realUrl);
                    return true;
                } else if (url.contains("&title=")) {
                    int index = url.lastIndexOf("&title=");
                    String realUrl = url.substring(0, index);
                    title.setText(Uri.decode(url.substring(index + 7, url.length())));
                    webView.loadUrl(realUrl);
                    return true;
                }
                return super.shouldOverrideUrlLoading(webView, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                boolean isResult = url.contains("oneKeyRegistrationResult.html");
                speedinessBack.setVisibility(isResult ? View.GONE : View.VISIBLE);
                speedinessCancel.setVisibility(isResult ? View.VISIBLE : View.GONE);
                speedinessRefresh.setVisibility(isResult ? View.VISIBLE : View.GONE);

                if (url.contains("oneKeyRegistration.html") || url.contains("oneKeyRegistrationResult.html")) {
                    title.setVisibility(View.GONE);
                } else {
                    title.setVisibility(View.VISIBLE);
                }
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

        speedinessBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    onBackPressed();
                }
            }
        });
        speedinessCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        speedinessRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.reload();
            }
        });

        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        List<String> ids = getIntent().getStringArrayListExtra("ids");
        String url = NetConstants.generateOneKeyLoanUrl(ids, UserHelper.getInstance(this).getProfile().getId());
        webView.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN
                && speedinessBack.getVisibility() == View.VISIBLE && webView.canGoBack()) {
            //有返回键时才返回
            webView.goBack();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
