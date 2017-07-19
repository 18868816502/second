package com.beihui.market.ui.activity;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.injection.component.AppComponent;

import butterknife.BindView;

public class InvitationActivity extends BaseComponentActivity {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.invitation_code)
    TextView invitationCodeTv;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.invite)
    Button inviteBtn;

    @Override
    public int getLayoutId() {
        return R.layout.activity_invitation;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }
}
