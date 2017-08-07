package com.beihui.market.ui.activity;


import android.annotation.SuppressLint;
import android.support.v7.widget.Toolbar;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.injection.component.AppComponent;

import butterknife.BindView;

public class ComWebViewActivity extends BaseComponentActivity {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView titleTv;
    @BindView(R.id.web_view)
    WebView webView;


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
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
            }
        });
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
    protected void configureComponent(AppComponent appComponent) {

    }
}
