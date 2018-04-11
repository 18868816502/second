package com.beihui.market.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.beihui.market.R;
import com.beihui.market.api.NetConstants;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.HotNews;
import com.beihui.market.entity.News;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.dialog.ShareDialog;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.util.SPUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import butterknife.BindView;


public class NewsDetailActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private News.Row news;
    private HotNews hotNews;

    private String newsUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //pv，uv统计
        DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_RESUME_NEWS);
    }

    @Override
    protected void onDestroy() {
        webView.getSettings().setJavaScriptEnabled(false);
        webView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_detail;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void configViews() {
        setupToolbar(toolbar);
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
        SlidePanelHelper.attach(this);

        webView.setWebViewClient(new WebViewClient());
    }

    @Override
    public void initDatas() {
        news = getIntent().getParcelableExtra("news");
        hotNews = getIntent().getParcelableExtra("hotNews");
        String newsId = getIntent().getStringExtra("newsId");
        if (newsId == null && news != null) {
            newsId = news.getId();
        }
        if (newsId == null && hotNews != null) {
            newsId = hotNews.getId();
        }
        String userId;
        if (UserHelper.getInstance(this).getProfile() != null) {
            userId = UserHelper.getInstance(this).getProfile().getId();
        } else {
            userId = SPUtils.getCacheUserId(this);
        }
        newsUrl = NetConstants.generateNewsUrl(newsId, userId);
        webView.loadUrl(newsUrl);
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
        //umeng统计
        Statistic.onEvent(Events.NEWS_DETAIL_SHARE);

        UMWeb web = new UMWeb(newsUrl.replace("&isApp=1", ""));
        String imageUrl = null;
        String title = null;
        String description = null;
        if (hotNews != null) {
            imageUrl = hotNews.getFilePath();
            title = hotNews.getTitle();
            description = hotNews.getExplain();
        } else if (news != null) {
            imageUrl = news.getImage();
            title = news.getTitle();
            description = news.getExplain();
        }
        if (imageUrl != null) {
            UMImage thumb = new UMImage(this, imageUrl);
            web.setThumb(thumb);
        }
        if (title != null) {
            web.setTitle(title);
        }
        if (description != null) {
            web.setDescription(description);
        }
        new ShareDialog()
                .setUmWeb(web)
                .show(getSupportFragmentManager(), ShareDialog.class.getSimpleName());
        return super.onOptionsItemSelected(item);
    }
}
