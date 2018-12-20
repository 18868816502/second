package com.beiwo.qnejqaz.ui.activity;


import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.entity.Invitation;
import com.beiwo.qnejqaz.helper.SlidePanelHelper;
import com.beiwo.qnejqaz.ui.contract.InvitationContract;
import com.beiwo.qnejqaz.ui.presenter.InvitationPresenter;
import com.beiwo.qnejqaz.umeng.Events;
import com.beiwo.qnejqaz.umeng.Statistic;
import com.beiwo.qnejqaz.util.CommonUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.umeng.socialize.UMShareAPI;

import java.util.List;

import butterknife.BindView;

/**
 * @author xhb
 *         邀请好友
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
        presenter = new InvitationPresenter(this,this);
        inviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //umeng统计
                Statistic.onEvent(Events.INVITATION_INVITE);

                /*UMWeb umWeb = new UMWeb(NetConstants.invitationActivityUrl(UserHelper.getInstance(InvitationActivity.this).getProfile().getId()));
                umWeb.setTitle("告诉你一个手机借款神器");
                umWeb.setDescription("急用钱？秒到账！超给力新口子，下款快，额度高，注册极简.");
                UMImage image = new UMImage(InvitationActivity.this, R.mipmap.ic_launcher);
                umWeb.setThumb(image);
                new ShareDialog()
                        .setUmWeb(umWeb)
                        .show(getSupportFragmentManager(), ShareDialog.class.getSimpleName());*/
            }
        });

        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        presenter.onStart();
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
                TextView mPhone = view.findViewById(R.id.phone);
                TextView mStatus = view.findViewById(R.id.status);
                mPhone.setText(CommonUtils.changeTel(row.getPhone()));
                mStatus.setText(row.getStatus());
                mRoot.addView(view);
            }
        }
    }
}
