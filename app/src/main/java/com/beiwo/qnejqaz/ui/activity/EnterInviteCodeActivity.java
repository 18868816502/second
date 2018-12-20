package com.beiwo.qnejqaz.ui.activity;

import android.support.v7.widget.Toolbar;
import android.webkit.JavascriptInterface;
import android.widget.RelativeLayout;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.api.NetConstants;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.helper.SlidePanelHelper;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.gyf.barlibrary.ImmersionBar;
import com.just.agentweb.AgentWeb;

import butterknife.BindView;

public class EnterInviteCodeActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    AgentWeb agentWeb;
    @BindView(R.id.web_view_code)
    RelativeLayout relativeLayout;


    @Override
    public int getLayoutId() {
        return R.layout.activity_enter_invite_code_layout;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);

    }

    @Override
    public void initDatas() {
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(relativeLayout, new RelativeLayout.LayoutParams(-1, -1)).
                        useDefaultIndicator(getResources().getColor(R.color.red), 1)
                .createAgentWeb()
                .ready()
                .go(NetConstants.codeUrl(UserHelper.getInstance(this).getProfile().getId(), UserHelper.getInstance(this).getProfile().getAccount()));
        agentWeb.getJsInterfaceHolder().addJavaObject("android", new EnterInviteCodeActivity.JsInterration());

    }

    public class JsInterration {
        @JavascriptInterface
        public void confirmCode() {
            EnterInviteCodeActivity.this.setResult(100);
            EnterInviteCodeActivity.this.finish();
        }


    }
}
