package com.beihui.market.ui.activity;


import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.LoanProduct;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerChoiceProductComponent;
import com.beihui.market.injection.module.ChoiceProductModule;
import com.beihui.market.ui.adapter.LoanRVAdapter;
import com.beihui.market.ui.contract.ChoiceProductContract;
import com.beihui.market.ui.presenter.ChoiceProductPresenter;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class ChoiceProductActivity extends BaseComponentActivity implements ChoiceProductContract.View {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Inject
    ChoiceProductPresenter presenter;

    private LoanRVAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_choice_product;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refreshChoiceProduct();
            }
        });

        adapter = new LoanRVAdapter();
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                presenter.loadMoreChoiceProduct();
            }
        }, recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
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
        adapter.notifyLoanProductChanged(list);
    }
}
