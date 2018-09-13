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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.NetConstants;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.dialog.ShareDialog;
import com.beihui.market.util.CommonUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.just.agentweb.AgentWeb;
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
    RelativeLayout webView;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private int type = 0;
    private String url = "";

    @Override
    protected void onDestroy() {
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
        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        try {
            type = getIntent().getIntExtra("type", 0);
        } catch (Exception e) {
        }
        if (type == 1) {//隐私申明
            tvTitle.setText("隐私申明");
            url = NetConstants.H5_USER_SECRET_PROTOCOL;
        } else if (type == 2) {//授权协议
            tvTitle.setText("授权协议");
            url = NetConstants.H5_USER_AUTH_PROTOCOL;
        } else if (type == 3) {//用户协议
            tvTitle.setText("用户协议");
            url = NetConstants.H5_USER_PROTOCOL;
        } else {//用户协议
            tvTitle.setText("注册协议");
            url = NetConstants.H5_USER_REGISTRATION_PROTOCOL;
        }
        AgentWeb.with(this)
                .setAgentWebParent(webView, new RelativeLayout.LayoutParams(-1, -1)).
                useDefaultIndicator(getResources().getColor(R.color.red), 1)
                .createAgentWeb()//
                .ready()
                .go(url);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }
}