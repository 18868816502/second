package com.beihui.market.ui.activity;


import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.gyf.barlibrary.ImmersionBar;
import com.just.agentweb.AgentWeb;

import butterknife.BindView;

/**
 * Copyright: zhujia (C)2018
 * FileName: H5Activity
 * Author: jiang
 * Create on: 2018/7/27 16:23
 * Description: 活动展示页
 */
public class H5Activity extends BaseComponentActivity {

    @BindView(R.id.tool_bar_web)
    Toolbar toolbar;

    @BindView(R.id.title_web)
    TextView titleTv;

    private String webViewUrl;
    private String title;
    @BindView(R.id.web_view_common)
    RelativeLayout relativeLayout;

    @Override
    public int getLayoutId() {
        return R.layout.activity_h5_layout;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();

        SlidePanelHelper.attach(this);
        webViewUrl = getIntent().getStringExtra("webViewUrl");
        title = getIntent().getStringExtra("title");


    }

    @Override
    public void initDatas() {
        titleTv.setText(title);
        AgentWeb.with(this)
                .setAgentWebParent(relativeLayout, new RelativeLayout.LayoutParams(-1, -1)).
                useDefaultIndicator(getResources().getColor(R.color.red), 1)
                .createAgentWeb()//
                .ready()
                .go(webViewUrl);

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }
}
