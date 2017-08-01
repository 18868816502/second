package com.beihui.market.ui.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.beihui.market.R;
import com.beihui.market.api.NetConstants;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.News;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.dialog.ShareDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.umeng.socialize.UMShareAPI;

import butterknife.BindView;


public class NewsDetailActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.web_view)
    WebView webView;

    private News.Row news;

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_detail;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        ImmersionBar.with(this).titleBar(toolbar).init();
    }

    @Override
    public void initDatas() {
        news = getIntent().getParcelableExtra("news");
        if (news != null) {
            webView.loadUrl(NetConstants.generateNewsUrl(news.getId()));
        }
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        new ShareDialog().show(getSupportFragmentManager(), ShareDialog.class.getSimpleName());
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
