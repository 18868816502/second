package com.beihui.market.ui.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.beihui.market.R;
import com.beihui.market.api.NetConstants;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;

public class CreditCardWebActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.web_view)
    WebView webView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_credit_card;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
        setupToolbarBackNavigation(toolbar, R.mipmap.left_arrow_black);

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

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivityWithoutOverride(intent);
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
        webView.loadUrl(NetConstants.H5_CREDIT_CARD_CENTER);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }
}
