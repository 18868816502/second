package com.beihui.market.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.widget.RelativeLayout;

import com.beihui.market.R;
import com.beihui.market.api.NetConstants;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.dialog.ShareDialog;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.LogUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.just.agentweb.AgentWeb;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

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
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

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

            UMWeb umWeb = new UMWeb(NetConstants.generateInvitationUrl(UserHelper.getInstance(context).getProfile().getId()));
            umWeb.setTitle("告诉你一个手机借款神器");
            umWeb.setDescription("急用钱？秒到账！超给力新口子，下款快，额度高，注册极简.");
            UMImage image = new UMImage(context, R.drawable.ic_launcher_kaola);
            umWeb.setThumb(image);
            new ShareDialog()
                    .setUmWeb(umWeb)
                    .show(getSupportFragmentManager(), ShareDialog.class.getSimpleName());
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
