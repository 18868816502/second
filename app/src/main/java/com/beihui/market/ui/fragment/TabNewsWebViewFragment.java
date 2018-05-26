package com.beihui.market.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
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
import com.beihui.market.base.BaseTabFragment;
import com.beihui.market.event.TabNewsWebViewFragmentTitleEvent;
import com.beihui.market.event.TabNewsWebViewFragmentUrlEvent;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.activity.MainActivity;
import com.beihui.market.ui.activity.UserAuthorizationActivity;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.view.BusinessWebView;

import org.apache.commons.codec.Encoder;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URLDecoder;
import java.net.URLEncoder;

import butterknife.BindView;

/**
 * @date 20180419
 * @version 2.1.1
 * @author xhb
 * 发现 模块 使用WebView页面 (非资讯详情页)
 */
public class TabNewsWebViewFragment extends BaseTabFragment{

    @BindView(R.id.tl_news_header_tool_bar)
    Toolbar toolbar;
    @BindView(R.id.iv_tab_fg_news_web_back)
    ImageView comeBack;
    @BindView(R.id.iv_tab_fg_news_web_title)
    TextView newsTitleName;
    @BindView(R.id.bwv_news_web_view)
    BusinessWebView webView;
//    @BindView(R.id.progress_bar)
//    ProgressBar progressBar;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;
//    @BindView(R.id.scroll_view_container)
//    ScrollView scrollView;

    /**
     * 拼接URL
     */
    public static String newsUrl = null;

    //依赖的activity
    public Activity mActivity;

    /**
     * 标题
     */
    public String mTitleName;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEvent(TabNewsWebViewFragmentTitleEvent event) {
        if (!TextUtils.isEmpty(event.title) && newsTitleName != null) {
            newsTitleName.setText(event.title);
            mTitleName = event.title;
        }
    }

    /**
     * 判断审核的状态
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUrlEvent(TabNewsWebViewFragmentUrlEvent event) {
        load();
    }


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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (TextUtils.isEmpty(mTitleName)) {
            mTitleName = getActivity().getResources().getString(R.string.tab_news);
        }
    }

    @Override
    public void onDestroy() {
        if (webView != null) {
            webView.getSettings().setJavaScriptEnabled(false);
            webView.destroy();
        }
        super.onDestroy();

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_tab_news_web_view;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void configViews() {
        comeBack.setVisibility(View.GONE);
        SlidePanelHelper.attach(mActivity);
    }



    @Override
    public void initDatas() {}

    @Override
    public void onResume() {
        super.onResume();

        load();

        /**
         * 在fragment里面 webView监听返回键事件
         */
        webView.setFocusable(true);
        webView.setFocusableInTouchMode(true);
        webView.requestFocus();
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
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


        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress > 0) {

                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (webView != null && newsUrl != null) {
                    webView.loadUrl(newsUrl);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayout.setColorScheme(R.color.colorPrimary);
        swipeRefreshLayout.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(SwipeRefreshLayout parent, @Nullable View child) {
                return !"0".equals(mScrollY);
            }
        });

        /**
         * 客户端监听器
         */
        webView.setWebViewClient(new WebViewClient() {

            // 页面开始加载
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.e("xhb", "url--->   " +url);
                Log.e("xhb", "newsUrl--->   " +newsUrl);

                if (url.equals(newsUrl)) {
                    swipeRefreshLayout.setEnabled(true);
                    comeBack.setVisibility(View.GONE);
                    newsTitleName.setText(mTitleName);
                } else {
                    swipeRefreshLayout.setEnabled(false);
                    String mTitleName = "";
                    if (url.contains("title")) {
                        int index = url.indexOf("?");
                        String temp = url.substring(index + 1);
                        if (temp.contains("&")) {
                            String[] keyValue = temp.split("&");
                            for (String str : keyValue) {
                                if (str.contains("title")) {
                                    mTitleName = str.replace("title=", "");
                                    break;
                                }
                            }
                        } else {
                            mTitleName = temp.replace("title=", "");
                        }
                        newsTitleName.setText(URLDecoder.decode(mTitleName));
                    }
                    comeBack.setVisibility(View.VISIBLE);
                }
            }

            // 页面加载完成
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            // WebView加载的所有资源url
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
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

        comeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                }
            }
        });
        webView.addJavascriptInterface(new mobileJsMethod(), "android");
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
            mScrollY = scrollY;
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