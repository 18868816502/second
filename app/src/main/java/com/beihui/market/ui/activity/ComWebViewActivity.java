package com.beihui.market.ui.activity;


import android.support.v7.widget.Toolbar;
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

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        webView.getSettings().setJavaScriptEnabled(true);
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
