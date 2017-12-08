package com.beihui.market.ui.fragment;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseTabFragment;
import com.beihui.market.entity.News;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerNewsComponent;
import com.beihui.market.injection.module.NewsModule;
import com.beihui.market.ui.activity.NewsDetailActivity;
import com.beihui.market.ui.adapter.NewsRVAdapter;
import com.beihui.market.ui.adapter.multipleentity.MultipleNewsItem;
import com.beihui.market.ui.contract.TabNewsContract;
import com.beihui.market.ui.presenter.TabNewsPresenter;
import com.beihui.market.ui.rvdecoration.NewsItemDeco;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.view.StateLayout;
import com.beihui.market.view.stateprovider.NewsStateViewProvider;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class TabNewsFragment extends BaseTabFragment implements TabNewsContract.View {

    @BindView(R.id.state_layout)
    StateLayout stateLayout;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.refresh_hint)
    TextView refreshHintView;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private NewsRVAdapter adapter;

    @Inject
    TabNewsPresenter presenter;

    private ValueAnimator inAnimator;
    private ValueAnimator outAnimator;

    public static TabNewsFragment newInstance() {
        return new TabNewsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //pv，uv统计
        DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_CLICK_TAB_NEWS);

        //umeng统计
        Statistic.onEvent(Events.ENTER_NEWS_PAGE);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        presenter.onDestroy();
        if (inAnimator != null) {
            inAnimator.cancel();
            inAnimator = null;
        }
        if (outAnimator != null) {
            outAnimator.cancel();
            outAnimator = null;
        }
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

                News.Row news = ((MultipleNewsItem) adapter.getData().get(position)).getNews();
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra("news", news);
                startActivity(intent);

                if (news != null) {
                    //Umeng统计
                    Statistic.onEvent(Events.ENTER_NEWS_DETAIL, news.getId());
                }
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

        refreshHintView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (refreshHintView != null) {
                    refreshHintView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    refreshHintView.setTranslationY(refreshHintView.getMeasuredHeight());
                }
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
    public void showRefreshHint(String refreshHint) {
//        refreshHintView.setText(refreshHint);
//
//        final int hintHeight = (int) (getResources().getDisplayMetrics().density * 35);
//        inAnimator = ValueAnimator.ofFloat(0, 1);
//        inAnimator.setDuration(100);
//        inAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                ViewGroup.LayoutParams lp = refreshHintView.getLayoutParams();
//                lp.height = (int) (hintHeight * (float) animation.getAnimatedValue());
//                refreshHintView.setLayoutParams(lp);
//                refreshHintView.requestLayout();
//            }
//        });
//        inAnimator.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                inAnimator = null;
//            }
//        });
//        inAnimator.start();
//
//        outAnimator = ValueAnimator.ofFloat(1, 0);
//        outAnimator.setDuration(200);
//        outAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                ViewGroup.LayoutParams lp = refreshHintView.getLayoutParams();
//                lp.height = (int) (hintHeight * (float) animation.getAnimatedValue());
//                refreshHintView.setLayoutParams(lp);
//                refreshHintView.requestLayout();
//            }
//        });
//        outAnimator.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                outAnimator = null;
//            }
//        });
//        outAnimator.setStartDelay(1100);
//        outAnimator.start();
    }

    @Override
    public void showNews(List<News.Row> news) {
        if (isAdded()) {
            stateLayout.switchState(StateLayout.STATE_CONTENT);
            recyclerView.setBackgroundColor(Color.WHITE);
            if (refreshLayout.isRefreshing()) {
                refreshLayout.setRefreshing(false);
            }
            if (adapter.isLoading()) {
                adapter.loadMoreComplete();
            }
            adapter.notifyNewsDataSetChanged(news);
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
