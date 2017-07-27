package com.beihui.market.ui.activity;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.SysMsg;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerSysMsgComponent;
import com.beihui.market.injection.module.SysMsgModule;
import com.beihui.market.ui.adapter.SysMsgAdapter;
import com.beihui.market.ui.contract.SysMsgContract;
import com.beihui.market.ui.presenter.SysMsgPresenter;
import com.beihui.market.ui.rvdecoration.NewsItemDeco;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class SysMsgActivity extends BaseComponentActivity implements SysMsgContract.View {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private SysMsgAdapter adapter;

    @Inject
    SysMsgPresenter presenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sys_msg;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        adapter = new SysMsgAdapter();
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(SysMsgActivity.this, ComWebViewActivity.class);
                startActivity(intent);
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                presenter.loadMore();
            }
        }, recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        float density = getResources().getDisplayMetrics().density;
        int padding = (int) (density * 7);
        recyclerView.addItemDecoration(new NewsItemDeco((int) (density * 0.5), padding, padding));
    }

    @Override
    public void initDatas() {
        presenter.onStart();
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerSysMsgComponent.builder()
                .appComponent(appComponent)
                .sysMsgModule(new SysMsgModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(SysMsgContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showSysMsg(List<SysMsg.Row> sysMsg) {
        adapter.notifySysMsgChanged(sysMsg);
    }

    @Override
    public void showNoSysMsg() {

    }

    @Override
    public void showNoMoreSysMsg() {
        adapter.loadMoreEnd(true);
    }

    @Override
    public void showErrorMsg(String msg) {
        super.showErrorMsg(msg);
        if (adapter != null && adapter.isLoading()) {
            adapter.loadMoreEnd(true);
        }
    }
}
