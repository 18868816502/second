package com.beihui.market.tang.activity;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beihui.market.App;
import com.beihui.market.BuildConfig;
import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.gyf.barlibrary.ImmersionBar;
import com.just.agentweb.AgentWeb;

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
        }
        titleTv.setText(title);

        AgentWeb.with(this)
                .setAgentWebParent(relativeLayout, new RelativeLayout.LayoutParams(-1, -1)).useDefaultIndicator(ContextCompat.getColor(this, R.color.white), 1)
                .createAgentWeb()
                .ready()
                .go(webViewUrl);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }
}