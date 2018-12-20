package com.beiwo.qnejqaz.ui.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.helper.SlidePanelHelper;
import com.beiwo.qnejqaz.util.AndroidBug5497Fix;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;

/**
 * 合作的产品 (如; 我要借款点击按钮跳转)
 */
public class ComWebViewActivity extends BaseComponentActivity {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView titleTv;
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;


    @Override
    protected void onDestroy() {
        webView.getSettings().setJavaScriptEnabled(false);
        webView.destroy();
        webView = null;
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_comm_web_view;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void configViews() {
        setupToolbar(toolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();

        String style = getIntent().getStringExtra("style");
        if (!TextUtils.isEmpty(style) && style.equals("light")) {
            toolbar.setBackgroundColor(Color.WHITE);
            titleTv.setTextColor(Color.BLACK);
            ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
        } else {
            ImmersionBar.with(this).titleBar(toolbar).init();
        }
        WebSettings webSettings = webView.getSettings();
        webView.getSettings().setJavaScriptEnabled(true);
        //解决图片不显示
        webSettings.setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        //avoiding load url in other web app,and 'denied to start' problem
        webView.setWebViewClient(new WebViewClient());

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivityWithoutOverride(intent);
            }
        });

        AndroidBug5497Fix.assistActivity(this);
        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        String url = getIntent().getStringExtra("url");
        if (url != null) {
            webView.loadUrl(url);
        }
        String title = getIntent().getStringExtra("title");
        if (title != null) {
            titleTv.setText(title);
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
