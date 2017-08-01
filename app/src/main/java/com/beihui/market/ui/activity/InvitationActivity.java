package com.beihui.market.ui.activity;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.Invitation;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerInvitationComponent;
import com.beihui.market.injection.module.InvitationModule;
import com.beihui.market.ui.adapter.InvitationAdapter;
import com.beihui.market.ui.contract.InvitationContract;
import com.beihui.market.ui.presenter.InvitationPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class InvitationActivity extends BaseComponentActivity implements InvitationContract.View {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.invitation_code)
    TextView invitationCodeTv;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.invite)
    Button inviteBtn;

    private InvitationAdapter adapter;

    @Inject
    InvitationPresenter presenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_invitation;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        adapter = new InvitationAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
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
        invitationCodeTv.setText(code);
    }

    @Override
    public void showInvitations(List<Invitation.Row> list) {
        adapter.notifyInvitationChanged(list);
    }
}
