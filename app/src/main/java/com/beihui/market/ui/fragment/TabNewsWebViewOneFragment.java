package com.beihui.market.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beihui.market.App;
import com.beihui.market.BuildConfig;
import com.beihui.market.R;
import com.beihui.market.api.NetConstants;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.base.BaseTabFragment;
import com.beihui.market.event.TabNewsWebViewFragmentTitleEvent;
import com.beihui.market.event.TabNewsWebViewFragmentUrlEvent;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.activity.MainActivity;
import com.beihui.market.ui.activity.TabMineActivity;
import com.beihui.market.ui.activity.UserAuthorizationActivity;
import com.beihui.market.ui.activity.WebViewActivity;
import com.beihui.market.ui.busevents.UserLoginEvent;
import com.beihui.market.ui.busevents.UserLogoutEvent;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.view.BusinessWebView;
import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @date 20180419
 * @version 2.1.1
 * @author xhb
 * 发现 模块 使用WebView页面 (非资讯详情页)
 */
public class TabNewsWebViewOneFragment extends BaseTabFragment{

    @BindView(R.id.bwv_news_web_view)
    BusinessWebView webView;
    @BindView(R.id.swipe_container_one)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.nsv_scroll_view)
    NestedScrollView scrollView;

    /**
     * 拼接URL
     */
    public static String newsUrl = null;

    //依赖的activity
    public FragmentActivity mActivity;

    @Subscribe
    public void onLogin(UserLoginEvent event) {
        load();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEvent(UserLogoutEvent event){
        load();
    }

    public static TabNewsWebViewOneFragment newInstance() {
        return new TabNewsWebViewOneFragment();
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    @Override
    public void onDestroy() {
        if (webView != null) {
            webView.getSettings().setJavaScriptEnabled(false);
            webView.destroy();
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_tab_news_web_view_one;
    }

    @Override
    public void configViews() {

    }

    @Override
    public void initDatas() {}

    @Override
    public void onStart() {
        super.onStart();

        load();

        initWebView();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (webView != null && newsUrl != null) {
                    webView.loadUrl(newsUrl);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayout.setColorScheme(R.color.refresh_one);
//        swipeRefreshLayout.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
//            @Override
//            public boolean canChildScrollUp(SwipeRefreshLayout parent, @Nullable View child) {
//                Log.e("canChildScrollUp", "ScrollY-----> " + mScrollY);
//                return !"0".equals(mScrollY);
//            }
//        });


        if (scrollView != null) {
            scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setEnabled(scrollView.getScrollY() == 0);
                    }
                    mScrollY = scrollView.getScrollY()+"";
                }
            });
        }

            webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                ((MainActivity)mActivity).startActivityWithoutOverride(intent);
            }
        });

        webView.addJavascriptInterface(new mobileJsMethod(), "android");
    }

    @Override
    public void onResume() {
        super.onResume();
        if ("0".equals(mScrollY)) {
            swipeRefreshLayout.setEnabled(true);
        }
    }

    private void initWebView() {
        /**
         * 在fragment里面 webView监听返回键事件
         */
        webView.setFocusable(true);
        webView.setFocusableInTouchMode(true);
        webView.requestFocus();
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setSavePassword(false);

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);

        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        /**
         * 客户端监听器
         */
        webView.setWebViewClient(new WebViewClient() {
            // url拦截
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.equals(newsUrl)) {
                    swipeRefreshLayout.setEnabled(true);
                    return super.shouldOverrideUrlLoading(view, url);
                } else {
                    swipeRefreshLayout.setEnabled(false);
                    Intent intent = new Intent(mActivity, WebViewActivity.class);
                    intent.putExtra("webViewUrl", URLDecoder.decode(url));
                    mActivity.startActivity(intent);
                    // 相应完成返回true
                    return true;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                int touchSlop = ViewConfiguration.get(webView.getContext()).getScaledTouchSlop();
                StringBuilder jsSb = new StringBuilder("javascript:initTouchSlop('").append(touchSlop).append("')");
                webView.loadUrl(jsSb.toString());

            }
        });
        webView.clearCache(true);
        webView.clearHistory();
        webView.setHapticFeedbackEnabled(false);
    }


    /**
     * webView 加载Url
     */
    private void load() {
        String userId = null;
        if (UserHelper.getInstance(mActivity).getProfile() != null) {
            userId = UserHelper.getInstance(mActivity).getProfile().getId();
        }

        if (TextUtils.isEmpty(userId)) {
            userId = "";
        }

        //生成发现页链接
        String channelId = "unknown";
        String versionName = BuildConfig.VERSION_NAME;
        try {
            channelId = App.getInstance().getPackageManager()
                    .getApplicationInfo(App.getInstance().getPackageName(), PackageManager.GET_META_DATA).metaData.getString("CHANNEL_ID");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        newsUrl = NetConstants.generateNewsWebViewUrl(userId, channelId, versionName);

        Log.e("newsUrl", "newsUrl--->     " + newsUrl);
        webView.loadUrl(newsUrl);
    }


    /**
     * 调用js
     */
    class mobileJsMethod{
        /**
         * 跳转到登陆页面
         */
        @JavascriptInterface
        public void authorize(String nextUrl){
            UserAuthorizationActivity.launch(getActivity(), null);
        }

        /**
         * 获取HTML滑动的Y轴的值
         */
        @JavascriptInterface
        public void getFindHtmlScrollY(String scrollY){
//            mScrollY = scrollY;
            Log.e("ScrollY", "ScrollY-----> " + mScrollY);
        }

        /**
         * Banner滑动
         */
        @JavascriptInterface
        public void getFindHtmlBannerStatus(boolean isFly){
            if (webView == null) {
                return;
            }
            webView.requestDisallowInterceptTouchEvent(isFly);
        }
    }

    /**
     * HTML Y轴坐标
     */
    public String mScrollY = "0";


    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

}