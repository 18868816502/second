package com.beiwo.klyjaz.tang.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.ui.activity.WebViewActivity;
import com.gyf.barlibrary.ImmersionBar;
import com.just.agentweb.AgentWeb;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import butterknife.BindView;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/9/7
 */

public class CreditResultActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar_web)
    Toolbar toolbar;
    @BindView(R.id.title_web)
    TextView titleTv;
    @BindView(R.id.web_view_common)
    RelativeLayout relativeLayout;

    private String webViewUrl = "";
    private String title = "";

    private AgentWeb mAgentWeb;
    private WebView mWebView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_h5_layout;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        ImmersionBar.with(this).statusBarDarkFont(false).init();
        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        webViewUrl = getIntent().getStringExtra("webViewUrl");
        title = getIntent().getStringExtra("title");
        if (webViewUrl.contains("page/activity-credit-query.html")) {
            toolbar.setBackgroundResource(R.color.refresh_one);
            setupToolbarBackNavigation(toolbar, R.drawable.back_white);
            titleTv.setTextColor(Color.WHITE);
        } else {
            toolbar.setBackgroundResource(R.color.transparent);
            setupToolbarBackNavigation(toolbar, R.drawable.back_white);
        }
        titleTv.setText(title);

        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(relativeLayout, new RelativeLayout.LayoutParams(-1, -1)).useDefaultIndicator(ContextCompat.getColor(this, R.color.white), 1)
                .createAgentWeb()
                .ready()
                .go(webViewUrl);
        mWebView = mAgentWeb.getWebCreator().getWebView();
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {// url拦截
                if (url.equals(webViewUrl)) {
                    return super.shouldOverrideUrlLoading(view, url);
                } else {
                    Intent intent = new Intent(view.getContext(), WebViewActivity.class);
                    try {
                        intent.putExtra("webViewUrl", URLDecoder.decode(url, "utf-8"));
                    } catch (UnsupportedEncodingException e) {
                    }
                    startActivity(intent);
                    // 相应完成返回true
                    return true;
                }
            }
        });
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }
}