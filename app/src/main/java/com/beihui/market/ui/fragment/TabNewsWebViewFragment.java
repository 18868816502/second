package com.beihui.market.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.beihui.market.R;
import com.beihui.market.api.NetConstants;
import com.beihui.market.base.BaseTabFragment;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.activity.MainActivity;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.SPUtils;
import com.beihui.market.view.BusinessWebView;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;

/**
 * @date 20180419
 * @version 2.1.1
 * @author xhb
 * 发现 模块 使用WebView页面 (非资讯详情页)
 */
public class TabNewsWebViewFragment extends BaseTabFragment {

    @BindView(R.id.tl_news_header_tool_bar)
    Toolbar toolbar;
    @BindView(R.id.bwv_news_web_view)
    BusinessWebView webView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    //依赖的activity
    public Activity mActivity;


    public static TabNewsWebViewFragment newInstance() {
        return new TabNewsWebViewFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mActivity = getActivity();

        //pv，uv统计
        DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_CLICK_TAB_NEWS);

        //umeng统计
        Statistic.onEvent(Events.ENTER_NEWS_PAGE);

        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onDestroy() {
        webView.getSettings().setJavaScriptEnabled(false);
        webView.destroy();
        super.onDestroy();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_tab_news_web_view;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void configViews() {

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
            }
        });
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                ((MainActivity)mActivity).startActivityWithoutOverride(intent);
            }
        });
        SlidePanelHelper.attach(mActivity);
    }


    /**
     * 拼接URL
     */
    private String newsUrl;

    @Override
    public void initDatas() {

        String userId;
        if (UserHelper.getInstance(mActivity).getProfile() != null) {
            userId = UserHelper.getInstance(mActivity).getProfile().getId();
        } else {
            userId = SPUtils.getCacheUserId(mActivity);
        }
        if (TextUtils.isEmpty(userId)) {
            userId = "";
        }

        newsUrl = NetConstants.generateNewsWebViewUrl(userId);

        webView.loadUrl(newsUrl);


        /**
         * 在fragment里面 webView监听返回键事件
         */
        webView.setFocusable(true);
        webView.setFocusableInTouchMode(true);
        webView.requestFocus();
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 1) {
                            mActivity.finish();
                            return true;
                        } else{
                            Toast.makeText(mActivity, "再按一次退出", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

}