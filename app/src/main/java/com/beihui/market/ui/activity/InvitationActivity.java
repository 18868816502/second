package com.beihui.market.ui.activity;


import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.gyf.barlibrary.ImmersionBar;
import com.just.agentweb.AgentWeb;


import butterknife.BindView;

/**
 * @author xhb
 * 邀请好友
 */
public class InvitationActivity extends BaseComponentActivity {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.web_view_layout)
    LinearLayout webView;

    private AgentWeb agentWeb;

    @Override
    public int getLayoutId() {
        return R.layout.activity_invitation;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        setupToolbar(toolbar);
        SlidePanelHelper.attach(this);

    }

    @Override
    public void initDatas() {
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(webView, new LinearLayout.LayoutParams(-1, -1)).
                        useDefaultIndicator(getResources().getColor(R.color.red), 2)
                .createAgentWeb()//
                .ready()
                .go("http://www.baidu.com");


    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

}
