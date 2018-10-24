package com.beiwo.klyjaz.ui.activity;


import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.NetConstants;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.entity.Invitation;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.injection.component.DaggerInvitationComponent;
import com.beiwo.klyjaz.injection.module.InvitationModule;
import com.beiwo.klyjaz.ui.contract.InvitationContract;
import com.beiwo.klyjaz.ui.dialog.ShareDialog;
import com.beiwo.klyjaz.ui.presenter.InvitationPresenter;
import com.beiwo.klyjaz.umeng.Events;
import com.beiwo.klyjaz.umeng.Statistic;
import com.beiwo.klyjaz.util.CommonUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @author xhb
 * 邀请好友
 */
public class InvitationActivity extends BaseComponentActivity implements InvitationContract.View {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;

    @BindView(R.id.ll_root)
    LinearLayout mRoot;
    @BindView(R.id.invite)
    TextView inviteBtn;

    @BindView(R.id.ac_invitation_sv_root)
    ScrollView mScrollView;

    @Inject
    InvitationPresenter presenter;

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        presenter = null;
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_invitation;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();

        inviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //umeng统计
                Statistic.onEvent(Events.INVITATION_INVITE);

                UMWeb umWeb = new UMWeb(NetConstants.invitationActivityUrl(UserHelper.getInstance(InvitationActivity.this).getProfile().getId()));
                umWeb.setTitle("告诉你一个手机借款神器");
                umWeb.setDescription("急用钱？秒到账！超给力新口子，下款快，额度高，注册极简.");
                UMImage image = new UMImage(InvitationActivity.this, R.mipmap.ic_launcher);
                umWeb.setThumb(image);
                new ShareDialog()
                        .setUmWeb(umWeb)
                        .show(getSupportFragmentManager(), ShareDialog.class.getSimpleName());
            }
        });

        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        presenter.onStart();
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerInvitationComponent.builder()
                .appComponent(appComponent)
                .invitationModule(new InvitationModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(InvitationContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showInvitationCode(String code) {
//        invitationCodeTv.setText(code);
    }

    @Override
    public void showInvitations(List<Invitation.Row> list) {
        if (list.size() > 0) {
            for (Invitation.Row row : list) {
                View view = View.inflate(this, R.layout.rv_item_invitation, null);
                TextView mPhone = (TextView) view.findViewById(R.id.phone);
                TextView mStatus = (TextView) view.findViewById(R.id.status);
                mPhone.setText(CommonUtils.changeTel(row.getPhone()));
                mStatus.setText(row.getStatus());
                mRoot.addView(view);
            }
        }
    }
}
