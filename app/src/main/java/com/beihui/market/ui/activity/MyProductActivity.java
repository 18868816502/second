package com.beihui.market.ui.activity;


import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.MyProduct;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerMyProductComponent;
import com.beihui.market.injection.module.MyProductModule;
import com.beihui.market.ui.adapter.MyProductRVAdapter;
import com.beihui.market.ui.contract.MyProductContract;
import com.beihui.market.ui.presenter.MyProductPresenter;
import com.beihui.market.view.StateLayout;
import com.beihui.market.view.stateprovider.MyProductStateProvider;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class MyProductActivity extends BaseComponentActivity implements MyProductContract.View {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.state_layout)
    StateLayout stateLayout;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private TextView successCountView;

    @Inject
    MyProductPresenter presenter;

    private MyProductRVAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_product;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        ImmersionBar.with(this).titleBar(toolbar).init();
        stateLayout.setStateViewProvider(new MyProductStateProvider(this));
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refreshMyProduct();
            }
        });

        adapter = new MyProductRVAdapter();
        View view = LayoutInflater.from(this)
                .inflate(R.layout.layout_my_product_header, null);
        successCountView = (TextView) view.findViewById(R.id.success_count);
        adapter.setHeaderView(view);

        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                presenter.loadMoreMyProduct();
            }
        }, recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SlidePanelHelper.attach(this);

    }

    @Override
    public void initDatas() {
        presenter.refreshMyProduct();
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerMyProductComponent.builder()
                .appComponent(appComponent)
                .myProductModule(new MyProductModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(MyProductContract.Presenter presenter) {
        //
    }

    @Override
    public void showSuccessCount(int count) {
        stateLayout.switchState(StateLayout.STATE_CONTENT);
        successCountView.setText("" + count);
    }

    @Override
    public void showMyProduct(List<MyProduct.Row> list, boolean canLoadMore) {
        stateLayout.switchState(StateLayout.STATE_CONTENT);
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        if (adapter.isLoading()) {
            adapter.loadMoreComplete();
        }
        adapter.setEnableLoadMore(canLoadMore);
        adapter.notifyMyProductChanged(list);
    }

    @Override
    public void showMyProductEmpty() {
        stateLayout.switchState(StateLayout.STATE_EMPTY);
    }

    @Override
    public void showErrorMsg(String msg) {
        super.showErrorMsg(msg);
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        if (adapter.isLoading()) {
            adapter.loadMoreComplete();
        }
        adapter.setEnableLoadMore(false);
    }
}
