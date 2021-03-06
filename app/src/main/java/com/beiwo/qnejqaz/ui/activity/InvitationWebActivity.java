package com.beiwo.qnejqaz.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.widget.RelativeLayout;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.api.NetConstants;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.helper.SlidePanelHelper;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.beiwo.qnejqaz.umeng.Events;
import com.beiwo.qnejqaz.umeng.Statistic;
import com.beiwo.qnejqaz.util.InputMethodUtil;
import com.beiwo.qnejqaz.util.LogUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.just.agentweb.AgentWeb;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;
import butterknife.OnClick;

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
    private AgentWeb agentWeb;
    @BindView(R.id.invitation_refresh)
    SmartRefreshLayout refreshLayout;


    @Override
    public int getLayoutId() {
        return R.layout.activity_invitation_web_layout;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        setupToolbarBackNavigation(toolbar, R.drawable.back_white);
        ImmersionBar.with(this).statusBarDarkFont(false).init();
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
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                agentWeb.getUrlLoader().reload();
                refreshLayout.finishRefresh();
            }
        });
    }

    @OnClick(R.id.daily_mission)
    void dailyMission() {
        startActivity(new Intent(context, DailyMissonActivity.class));
    }

    public class JsInterration {

        @JavascriptInterface
        public void invite() {
            //umeng统计
            Statistic.onEvent(Events.INVITATION_INVITE);

            /*UMWeb umWeb = new UMWeb(NetConstants.invitationActivityUrl(UserHelper.getInstance(context).getProfile().getId()));
            umWeb.setTitle("告诉你一个手机借款神器");
            umWeb.setDescription("急用钱？秒到账！超给力新口子，下款快，额度高，注册极简.");
            UMImage image = new UMImage(context, R.drawable.ic_launcher_kaola);
            umWeb.setThumb(image);
            new ShareDialog()
                    .setUmWeb(umWeb)
                    .show(getSupportFragmentManager(), ShareDialog.class.getSimpleName());*/
        }


        @JavascriptInterface
        public void contactInvite() {
            agentWeb.getJsAccessEntrace().callJs("contactInvite()", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    LogUtils.i("value------------->" + value);
                    Intent intent = new Intent(context, ContactsActivity.class);
                    intent.putExtra("contact", value);
                    context.startActivity(intent);

                }
            });

        }

        @JavascriptInterface
        public void guideInvite() {
            context.startActivity(new Intent(context, GuideInviteActivity.class));
        }
    }

    @Override
    protected void onResume() {
        InputMethodUtil.closeSoftKeyboard(this);
        super.onResume();
    }
}
