package com.beihui.market.ui.fragment;

import android.content.Intent;
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
import com.beihui.market.ui.contract.NewsContract;
import com.beihui.market.ui.presenter.NewsPresenter;
import com.beihui.market.ui.rvdecoration.NewsItemDeco;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class TabNewsFragment extends BaseTabFragment implements NewsContract.View {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private NewsRVAdapter adapter;

    @Inject
    NewsPresenter presenter;

    public static TabNewsFragment newInstance() {
        return new TabNewsFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_tab_news;
    }

    @Override
    public void configViews() {
        adapter = new NewsRVAdapter();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
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
    }

    @Override
    public void initDatas() {
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
    public void setPresenter(NewsContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showNews(List<News.Row> news) {
        adapter.notifyNewsSetChanged(news);
        if (adapter.isLoading()) {
            adapter.loadMoreComplete();
        }
    }

    @Override
    public void showNoNews() {

    }

    @Override
    public void showNoMoreNews() {
        adapter.loadMoreEnd(true);
    }
}
