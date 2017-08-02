package com.beihui.market.ui.activity;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.Notice;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerAnnounceComponent;
import com.beihui.market.injection.module.AnnounceModule;
import com.beihui.market.ui.adapter.AnnouncementAdapter;
import com.beihui.market.ui.contract.NoticeContract;
import com.beihui.market.ui.presenter.NoticePresenter;
import com.beihui.market.ui.rvdecoration.NewsItemDeco;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class NoticeActivity extends BaseComponentActivity implements NoticeContract.View {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private AnnouncementAdapter adapter;

    @Inject
    NoticePresenter presenter;


    @Override
    public int getLayoutId() {
        return R.layout.activity_announcement;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        adapter = new AnnouncementAdapter();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(NoticeActivity.this, ComWebViewActivity.class);
                startActivity(intent);
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                presenter.loadMore();
            }
        }, recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
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
        DaggerAnnounceComponent.builder()
                .appComponent(appComponent)
                .announceModule(new AnnounceModule(this))
                .build().inject(this);
    }

    @Override
    public void setPresenter(NoticeContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showAnnounce(List<Notice.Row> announceList) {
        if (recyclerView.getVisibility() == View.GONE) {
            recyclerView.setVisibility(View.VISIBLE);
        }
        adapter.notifyAnnounceDataChanged(announceList);
        if (adapter.isLoading()) {
            adapter.loadMoreComplete();
        }
    }

    @Override
    public void showNoAnnounce() {
    }

    @Override
    public void showNoMoreAnnounce() {
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