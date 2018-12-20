package com.beiwo.qnejqaz.ui.activity;

import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.api.NetConstants;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.helper.SlidePanelHelper;
import com.gyf.barlibrary.ImmersionBar;
import com.just.agentweb.AgentWeb;

import butterknife.BindView;

public class AboutKaolaActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.web_about_kaola)
    RelativeLayout relativeLayout;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @Override
    public int getLayoutId() {
        return R.layout.activity_about_kaola;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);
        tv_title.setText(String.format(getString(R.string.about_app), getString(R.string.app_name)));
    }

    @Override
    public void initDatas() {
        AgentWeb.with(this)
                .setAgentWebParent(relativeLayout, new RelativeLayout.LayoutParams(-1, -1)).
                useDefaultIndicator(getResources().getColor(R.color.red), 1)
                .createAgentWeb()//
                .ready()
                .go(NetConstants.H5_ABOUT_US);
    }
}