package com.beihui.market.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beihui.market.R;
import com.beihui.market.base.BaseTabFragment;
import com.beihui.market.entity.News;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerNewsComponent;
import com.beihui.market.injection.module.NewsModule;
import com.beihui.market.ui.activity.NewsDetailActivity;
import com.beihui.market.ui.adapter.NewsRVAdapter;
import com.beihui.market.ui.contract.TabNewsContract;
import com.beihui.market.ui.presenter.TabNewsPresenter;
import com.beihui.market.ui.rvdecoration.NewsItemDeco;
import com.beihui.market.view.stateprovider.NewsStateViewProvider;
import com.beihui.market.view.StateLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class TabNewsFragment extends BaseTabFragment implements TabNewsContract.View {

    @BindView(R.id.state_layout)
    StateLayout stateLayout;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private NewsRVAdapter adapter;

    @Inject
    TabNewsPresenter presenter;

    public static TabNewsFragment newInstance() {
        return new TabNewsFragment();
    }

    @Override
    public void onDestroyView() {
        presenter.onDestroy();
        super.onDestroyView();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_tab_news;
    }

    @Override
    public void configViews() {
        stateLayout.setStateViewProvider(new NewsStateViewProvider(getContext(),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.refresh();
                    }
                }));

        adapter = new NewsRVAdapter();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra("news", (News.Row) adapter.getData().get(position));
                startActivity(intent);
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                presenter.loadMore();
            }
        }, recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        float density = getContext().getResources().getDisplayMetrics().density;
        int padding = (int) (density * 8);
        recyclerView.addItemDecoration(new NewsItemDeco((int) (density * 0.5), padding, padding));

        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refresh();
            }
        });
    }

    @Override
    public void initDatas() {
        refreshLayout.setRefreshing(true);
        presenter.onStart();
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerNewsComponent.builder()
                .appComponent(appComponent)
                .newsModule(new NewsModule(this))
                .build()
                .inject(this);
    }


    @Override
    public void setPresenter(TabNewsContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showNews(List<News.Row> news) {
        if (!isAdded())
            return;
        stateLayout.switchState(StateLayout.STATE_CONTENT);
        adapter.notifyNewsSetChanged(news);
        if (adapter.isLoading()) {
            adapter.loadMoreComplete();
        }
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showErrorMsg(String msg) {
        if (!isAdded())
            return;
        super.showErrorMsg(msg);
        if (adapter.isLoading()) {
            adapter.loadMoreComplete();
        }
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showNoNews() {
        if (!isAdded())
            return;
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        stateLayout.switchState(StateLayout.STATE_EMPTY);
    }

    @Override
    public void showNetError() {
        if (!isAdded())
            return;
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        stateLayout.switchState(StateLayout.STATE_NET_ERROR);
    }

    @Override
    public void showNoMoreNews() {
        if (!isAdded())
            return;
        adapter.loadMoreEnd(true);
    }
}
