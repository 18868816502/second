package com.beihui.market.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.LoanProduct;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerChoiceProductComponent;
import com.beihui.market.injection.module.ChoiceProductModule;
import com.beihui.market.ui.adapter.HotChoiceRVAdapter;
import com.beihui.market.ui.contract.ChoiceProductContract;
import com.beihui.market.ui.presenter.ChoiceProductPresenter;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class ChoiceProductActivity extends BaseComponentActivity implements ChoiceProductContract.View {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView titleView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Inject
    ChoiceProductPresenter presenter;

    private HotChoiceRVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //umeng统计
        Statistic.onEvent(Events.RESUME_CHOICE_PRODUCT);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_choice_product;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);

        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refreshChoiceProduct();
            }
        });

        adapter = new HotChoiceRVAdapter(R.layout.list_item_hot_product);
        final View view = LayoutInflater.from(this)
                .inflate(R.layout.layout_choice_product_head, null);
        adapter.setHeaderView(view);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                presenter.loadMoreChoiceProduct();
            }
        }, recyclerView);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(ChoiceProductActivity.this, LoanDetailActivity.class);
                intent.putExtra("loan", (LoanProduct.Row) adapter.getItem(position));
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int scrollY;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollY += dy;
                titleView.setVisibility(scrollY >= view.getMeasuredHeight() - 10 ? View.VISIBLE : View.GONE);
            }
        });

        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        presenter.refreshChoiceProduct();
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerChoiceProductComponent.builder()
                .appComponent(appComponent)
                .choiceProductModule(new ChoiceProductModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(ChoiceProductContract.Presenter presenter) {
        //
    }

    @Override
    public void showChoiceProduct(List<LoanProduct.Row> list, boolean canLoanMore) {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        if (adapter.isLoading()) {
            adapter.loadMoreComplete();
        }
        adapter.setEnableLoadMore(canLoanMore);
        adapter.notifyHotProductChanged(list);
    }
}
