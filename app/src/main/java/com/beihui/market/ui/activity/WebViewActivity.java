package com.beihui.market.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.App;
import com.beihui.market.BuildConfig;
import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.util.viewutils.ToastUtils;
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
    @BindView(R.id.iv_tab_fg_news_web_back)
    ImageView mReturn;
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
        } else if (!TextUtils.isEmpty(getIntent().getStringExtra("webViewTitleName"))) {
            titleName.setText(getIntent().getStringExtra("webViewTitleName"));
        }
        SlidePanelHelper.attach(this);
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
        //解决图片不显示
        webSettings.setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

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
            if (webViewUrl.contains("?")) {
                webView.loadUrl(webViewUrl + "&isApp=1&userId=" + userId + "&packageId=" + channelId + "&version=" + versionName);
            } else {
                webView.loadUrl(webViewUrl + "?isApp=1&userId=" + userId + "&packageId=" + channelId + "&version=" + versionName);
            }
        }

        mReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    /**
     * 调用js
     */
    class mobileJsMethod {
        /**
         * 跳转到登陆页面
         */
        @JavascriptInterface
        public void authorize(String nextUrl) {
            UserAuthorizationActivity.launch(WebViewActivity.this, null);
        }

        /**
         * 获取HTML滑动的Y轴的值
         */
        @JavascriptInterface
        public void getFindHtmlScrollY(String scrollY) {
            mScrollY = scrollY;
        }

        /**
         * 唤醒微信
         */
        @JavascriptInterface
        public void openWeChat() {
            try {
                Intent intent = new Intent();
                ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(cmp);
                startActivityForResult(intent, 0);

            } catch (Exception e) {
                //若无法正常跳转，在此进行错误处理
                ToastUtils.showShort(WebViewActivity.this, "无法跳转到微信，请检查您是否安装了微信！", null);
            }
        }

        @JavascriptInterface
        public void skipToHome() {
            Intent intent = new Intent(WebViewActivity.this, MainActivity.class);
            intent.putExtra("home", true);
            startActivity(intent);
        }
    }

    /**
     * HTML Y轴坐标
     */
    public String mScrollY = "0";
}
