package com.beihui.market.ui.activity;

import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;

import com.beihui.market.R;
import com.beihui.market.api.NetConstants;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.gyf.barlibrary.ImmersionBar;
import com.just.agentweb.AgentWeb;

import butterknife.BindView;

public class AboutKaolaActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.web_about_kaola)
    RelativeLayout relativeLayout;


    @Override
    public int getLayoutId() {
        return R.layout.activity_about_kaola;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();

        SlidePanelHelper.attach(this);

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

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }
}
