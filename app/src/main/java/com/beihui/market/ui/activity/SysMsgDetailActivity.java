package com.beihui.market.ui.activity;


import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.SysMsg;
import com.beihui.market.entity.SysMsgDetail;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerSysMsgDetailComponent;
import com.beihui.market.injection.module.SysMsgDetailModule;
import com.beihui.market.ui.contract.SysMsgDetailContract;
import com.beihui.market.ui.presenter.SysMsgDetailPresenter;

import javax.inject.Inject;

import butterknife.BindView;

public class SysMsgDetailActivity extends BaseComponentActivity implements SysMsgDetailContract.View {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView titleTv;
    @BindView(R.id.content)
    TextView contentTv;

    @Inject
    SysMsgDetailPresenter presenter;

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        presenter = null;
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sys_msg_detail;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);

    }

    @Override
    public void initDatas() {
        SysMsg.Row sysMsg = getIntent().getParcelableExtra("sysMsg");
        if (sysMsg != null) {
            if (sysMsg.getId() != null) {
                presenter.queryMsgDetail(sysMsg.getId());
            }
            if (sysMsg.getTitle() != null) {
                titleTv.setText(sysMsg.getTitle());
            }
        }
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerSysMsgDetailComponent.builder()
                .appComponent(appComponent)
                .sysMsgDetailModule(new SysMsgDetailModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(SysMsgDetailContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showSysMsgDetail(SysMsgDetail detail) {
        if (detail != null && detail.getContent() != null) {
            contentTv.setText(detail.getContent());
        }
    }
}
