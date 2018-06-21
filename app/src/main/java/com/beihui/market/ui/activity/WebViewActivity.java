package com.beihui.market.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.beihui.market.App;
import com.beihui.market.BuildConfig;
import com.beihui.market.R;
import com.beihui.market.api.NetConstants;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.view.BusinessWebView;
import com.gyf.barlibrary.ImmersionBar;

import java.net.URLDecoder;

import butterknife.BindView;

/**
 * Created by admin on 2018/6/20.
 */

public class WebViewActivity extends BaseComponentActivity {

    @BindView(R.id.tl_news_header_tool_bar)
    android.support.v7.widget.Toolbar toolbar;
    @BindView(R.id.tv_web_view_title)
    TextView titleName;
    @BindView(R.id.bwv_news_web_view)
    BusinessWebView webView;

    private String webViewUrl;

    @Override
    public int getLayoutId() {
        return R.layout.x_ac_web_view;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
        setupToolbarBackNavigation(toolbar, R.mipmap.left_arrow_black);
        webViewUrl = getIntent().getStringExtra("webViewUrl");

        String mTitleName = "";
        if (webViewUrl.contains("title")) {
            int index = webViewUrl.indexOf("?");
            String temp = webViewUrl.substring(index + 1);
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
            titleName.setText(URLDecoder.decode(mTitleName));
        }
    }

    @Override
    public void initDatas() {

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

                        } else{
                            Toast.makeText(WebViewActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                return false;
            }
        });


        webView.setWebViewClient(new WebViewClient());

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivityWithoutOverride(intent);
            }
        });


        webView.addJavascriptInterface(new mobileJsMethod(), "android");



        String userId = null;
        if (UserHelper.getInstance(this).getProfile() != null) {
            userId = UserHelper.getInstance(this).getProfile().getId();
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
       if (!TextUtils.isEmpty(webViewUrl)) {
           webView.loadUrl(webViewUrl+"?isApp=1&userId=" + userId + "&packageId=" + channelId + "&version=" + versionName);
       }
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

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
            UserAuthorizationActivity.launch(WebViewActivity.this, null);
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
}
