package com.beiwo.qnejqaz.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.api.NetConstants;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.entity.HotNews;
import com.beiwo.qnejqaz.entity.News;
import com.beiwo.qnejqaz.helper.DataHelper;
import com.beiwo.qnejqaz.helper.SlidePanelHelper;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.beiwo.qnejqaz.umeng.Events;
import com.beiwo.qnejqaz.umeng.Statistic;
import com.beiwo.qnejqaz.util.SPUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.umeng.socialize.UMShareAPI;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 资讯模块 资讯详情页面 WebView
 */
public class NewsDetailActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    FrameLayout toolbar;
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
        DataHelper.getInstance(this).onCountUv(DataHelper.ID_RESUME_NEWS);
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
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_detail;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).init();

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivityWithoutOverride(intent);
            }
        });
        SlidePanelHelper.attach(this);

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
            userId = SPUtils.getCacheUserId();
        }
        newsUrl = NetConstants.generateNewsUrl(newsId, userId);
        webView.loadUrl(newsUrl);
    }

    @OnClick({R.id.close, R.id.share})
    void onItemClicked(View view) {
        switch (view.getId()) {
            case R.id.close:
                finish();
                break;
            /**
             * 分享 分享微信 朋友圈 QQ 微博
             */
            case R.id.share:
                //umeng统计
                Statistic.onEvent(Events.NEWS_DETAIL_SHARE);

                /*UMWeb web = new UMWeb(newsUrl.replace("&isApp=1", ""));
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
                        .show(getSupportFragmentManager(), ShareDialog.class.getSimpleName());*/
                break;
        }
    }
}
