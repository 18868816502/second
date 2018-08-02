package com.beihui.market.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.beihui.market.BuildConfig;
import com.beihui.market.R;
import com.beihui.market.api.NetConstants;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.dialog.ShareDialog;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.LogUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.just.agentweb.AgentWeb;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import butterknife.BindView;

/**
 * Copyright: zhujia (C)2018
 * FileName: InvitationWebActivity
 * Author: jiang
 * Create on: 2018/7/30 14:34
 * Description:邀请好友界面
 */
public class InvitationWebActivity extends BaseComponentActivity {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.web_view_invitation)
    RelativeLayout relativeLayout;
    private Context context;
    @BindView(R.id.invitation_reload)
    SwipeRefreshLayout swipeRefreshLayout;
    private AgentWeb agentWeb;


    @Override
    public int getLayoutId() {
        return R.layout.activity_invitation_web_layout;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);
        context = this;
    }


    @Override
    public void initDatas() {
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(relativeLayout, new RelativeLayout.LayoutParams(-1, -1)).
                        useDefaultIndicator(getResources().getColor(R.color.red), 1)
                .createAgentWeb()//
                .ready()
                .go(NetConstants.invitationUrl(UserHelper.getInstance(context).getProfile().getId()));
        agentWeb.getJsInterfaceHolder().addJavaObject("android", new JsInterration());
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.c_ff5240));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                agentWeb.getUrlLoader().reload();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        LogUtils.i(UserHelper.getInstance(context).getProfile().getId());

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    public class JsInterration {

        @JavascriptInterface
        public String getUserId() {
            return UserHelper.getInstance(context).getProfile().getId();
        }

        @JavascriptInterface
        public String getPackageId() {
            return BuildConfig.APPLICATION_ID;
        }

        @JavascriptInterface
        public String getVersion() {
            return BuildConfig.VERSION_NAME;

        }

        @JavascriptInterface
        public void invite() {
            //umeng统计
            Statistic.onEvent(Events.INVITATION_INVITE);

            UMWeb umWeb = new UMWeb(NetConstants.generateInvitationUrl(UserHelper.getInstance(context).getProfile().getId()));
            umWeb.setTitle("告诉你一个手机借款神器");
            umWeb.setDescription("急用钱？秒到账！超给力新口子，下款快，额度高，注册极简.");
            UMImage image = new UMImage(context, R.mipmap.ic_launcher);
            umWeb.setThumb(image);
            new ShareDialog()
                    .setUmWeb(umWeb)
                    .show(getSupportFragmentManager(), ShareDialog.class.getSimpleName());
        }

    }

    @Override
    protected void onResume() {
        InputMethodUtil.closeSoftKeyboard(this);
        super.onResume();
    }
}
