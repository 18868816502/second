package com.beiwo.klyjaz.base;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.gyf.barlibrary.ImmersionBar;
import com.just.agentweb.AgentWeb;

import butterknife.BindView;

/**
 * Copyright: dondo (C)2018
 * FileName: BaseH5Activity
 * Author: jiang
 * Create on: 2018/8/20 14:34
 * Description:
 */
public abstract class BaseH5Activity extends BaseComponentActivity {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.base_h5_relative)
    RelativeLayout relativeLayout;
    @BindView(R.id.tool_bar_title)
    public TextView titleTv;
    @BindView(R.id.toolbar_sub_title)
    public TextView subTilteTv;

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        initTitle();
        ImmersionBar.with(this).titleBar(toolbar).init();
    }

    @Override
    public int getLayoutId() {
        return R.layout.base_h5_layout;
    }

    @Override
    public void initDatas() {
        AgentWeb.with(this).setAgentWebParent(relativeLayout, new RelativeLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(ContextCompat.getColor(this, R.color.red), 1)
                .createAgentWeb().ready().go(loadUrl());
    }

    public abstract String loadUrl();

    public abstract void initTitle();
}