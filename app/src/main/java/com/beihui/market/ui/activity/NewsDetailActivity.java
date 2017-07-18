package com.beihui.market.ui.activity;

import android.webkit.WebView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.component.AppComponent;

import butterknife.BindView;


public class NewsDetailActivity extends BaseComponentActivity {

    @BindView(R.id.web_view)
    WebView webView;


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
    protected void configureComponent(AppComponent appComponent) {

    }
}
