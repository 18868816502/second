package com.beiwo.klyjaz.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.klyjaz.App;
import com.beiwo.klyjaz.BuildConfig;
import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.entity.UserProfileAbstract;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.tang.DlgUtil;
import com.beiwo.klyjaz.util.ToastUtil;
import com.beiwo.klyjaz.util.WeakRefToastUtil;
import com.beiwo.klyjaz.view.BusinessWebView;
import com.gyf.barlibrary.ImmersionBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;

import butterknife.BindView;


public class WebViewActivity extends BaseComponentActivity {

    @BindView(R.id.tl_news_header_tool_bar)
    Toolbar toolbar;
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

    private ValueCallback<Uri[]> uploadMessageAboveL;
    private final static int FILE_CHOOSER_RESULT_CODE = 10000;

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
        //h5打开空白
        webSettings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                uploadMessageAboveL = filePathCallback;
                openImageChooserActivity();
                return true;
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

        webView.addJavascriptInterface(new mobileJsMethod(), "android");

        String userId = null;
        if (UserHelper.getInstance(this).getProfile() != null) {
            userId = UserHelper.getInstance(this).getProfile().getId();
        }

        if (TextUtils.isEmpty(userId)) {
            userId = "";
        }
        //生成发现页链接
        String versionName = BuildConfig.VERSION_NAME;
        if (!TextUtils.isEmpty(webViewUrl)) {
            if (webViewUrl.contains("?")) {
                webView.loadUrl(webViewUrl + "&isApp=1&userId=" + userId + "&packageId=" + App.sChannelId + "&version=" + versionName);
            } else {
                webView.loadUrl(webViewUrl + "?isApp=1&userId=" + userId + "&packageId=" + App.sChannelId + "&version=" + versionName);
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

    private void openImageChooserActivity() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == uploadMessageAboveL) return;
            onActivityResultAboveL(requestCode, resultCode, data);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != FILE_CHOOSER_RESULT_CODE || uploadMessageAboveL == null) return;
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null) results = new Uri[]{Uri.parse(dataString)};
            }
        }
        uploadMessageAboveL.onReceiveValue(results);
        uploadMessageAboveL = null;
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

    /*调用js*/
    class mobileJsMethod {
        /*跳转到登陆页面*/
        @JavascriptInterface
        public void authorize() {
            //UserAuthorizationActivity.launch(WebViewActivity.this, null);
            DlgUtil.loginDlg(WebViewActivity.this, new DlgUtil.OnLoginSuccessListener() {
                @Override
                public void success(UserProfileAbstract data) {
                    if (data != null)
                        webView.loadUrl("javascript:sendLoginUserInfo('" + data.toString() + "')");
                }
            });
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
                WeakRefToastUtil.showShort(WebViewActivity.this, "无法跳转到微信，请检查您是否安装了微信！", null);
            }
        }

        @JavascriptInterface
        public void skipToHome() {
            Intent intent = new Intent(WebViewActivity.this, App.audit == 2 ? MainActivity.class : VestMainActivity.class);
            intent.putExtra("home", true);
            startActivity(intent);
        }

        @JavascriptInterface
        public void ConversePicture(final String DownloadImageURL) {
            try {
                byte[] buffer = Base64.decode(DownloadImageURL.split(",")[1], Base64.DEFAULT);
                for (int i = 0; i < buffer.length; i++) {
                    if (buffer[i] < 0) buffer[i] += 256;
                }
                File f = new File(Environment.getExternalStorageDirectory(), "invite_friend.png");
                FileOutputStream out = new FileOutputStream(f);
                out.write(buffer);
                out.flush();
                out.close();
                //广播通知相册轮询图片
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(f));
                sendBroadcast(intent);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.toast("保存成功");
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.toast("保存失败");
                    }
                });
            }
        }
    }

    /*HTML Y轴坐标*/
    private String mScrollY = "0";
}