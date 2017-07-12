package com.beihui.market.ui.activity;

import android.webkit.WebView;

import com.beihui.market.R;
import com.beihui.market.base.BaseActivity;
import com.beihui.market.component.AppComponent;

import butterknife.BindView;


public class NewsDetailActivity extends BaseActivity {

    @BindView(R.id.web_view)
    WebView webView;

    @Override
    public void attachView() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_detail;
    }

    @Override
    public void configViews() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }
}
