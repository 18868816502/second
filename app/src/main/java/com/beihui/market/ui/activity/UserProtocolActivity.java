package com.beihui.market.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.beihui.market.R;
import com.beihui.market.api.NetConstants;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.dialog.ShareDialog;
import com.beihui.market.util.CommonUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import butterknife.BindView;

/**
 * 用户协议的页面
 */
public class UserProtocolActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.web_view)
    WebView webView;


    @Override
    protected void onDestroy() {
        webView.getSettings().setJavaScriptEnabled(false);
        webView.destroy();
        webView = null;
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_protocol;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void configViews() {
        setupToolbar(mToolBar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
//        int statusHeight = CommonUtils.getStatusBarHeight(this);
//        //设置toolbar的高度为状态栏相同高度
//        mToolBar.setPadding(mToolBar.getPaddingLeft(), statusHeight, mToolBar.getPaddingRight(), 0);
//        ViewGroup.LayoutParams lp = mToolBar.getLayoutParams();
//        lp.height = statusHeight;
//        mToolBar.setLayoutParams(lp);


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        webView.loadUrl(NetConstants.H5_USER_REGISTRATION_PROTOCOL);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }


}
