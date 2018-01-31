package com.beihui.market.ui.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.NetConstants;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.view.BusinessWebView;
import com.gyf.barlibrary.ImmersionBar;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.OnClick;

public class CreditCardWebActivity extends BaseComponentActivity {

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

    private String pendingUrl;

    @Override
    public int getLayoutId() {
        return R.layout.activity_credit_card;
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();

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

        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(this, "android");

        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        String url = getIntent().getStringExtra("url");
        String title;
        if (!TextUtils.isEmpty(url)) {
            title = getIntent().getStringExtra("title");
        } else {
            String userId = null;
            if (UserHelper.getInstance(this).getProfile() != null) {
                userId = UserHelper.getInstance(this).getProfile().getId();
            }
            url = NetConstants.generateCreditCardUrl(userId);
            title = "信用卡中心";
        }

        webView.loadUrl(url);
        this.titleList.push(title);
        this.title.setText(title);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LOGIN) {
            if (UserHelper.getInstance(this).getProfile() != null) {
                String originalUrl = webView.getUrl();
                if (originalUrl.contains("userId=null")) {
                    webView.loadUrl(originalUrl.replace("userId=null", "userId=" + UserHelper.getInstance(this).getProfile().getId()));
                }
                webView.loadUrl(pendingUrl);
            }
        }
    }

    @OnClick(R.id.close)
    void onBindViewClicked() {
        finish();
    }

    @JavascriptInterface
    public void authorize(final String nextUrl) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pendingUrl = nextUrl;
                override = false;
                startActivityForResult(new Intent(CreditCardWebActivity.this, UserAuthorizationActivity.class), REQUEST_CODE_LOGIN);
            }
        });
    }

    @JavascriptInterface
    public void onPageChanged(final String titleParam) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String pageTitle = Uri.decode(titleParam);
                titleList.push(pageTitle);
                title.setText(pageTitle);
            }
        });
    }
}
