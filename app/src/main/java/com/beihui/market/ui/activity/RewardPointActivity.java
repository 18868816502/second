package com.beihui.market.ui.activity;


import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.Toolbar;
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
import com.beihui.market.api.NetConstants;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;

import java.util.LinkedList;

import butterknife.BindView;

public class RewardPointActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.web_view)
    WebView webView;

    private LinkedList<String> titleList = new LinkedList<>();

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
        try {
            Intent toMarket = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getApplicationInfo().packageName));
            startActivityWithoutOverride(toMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
