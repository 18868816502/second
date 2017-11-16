package com.beihui.market.ui.fragment;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.entity.LoanProduct;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerProductCollectionComponent;
import com.beihui.market.injection.module.ProductCollectionModule;
import com.beihui.market.ui.adapter.LoanRVAdapter;
import com.beihui.market.ui.contract.ProductCollectionContract;
import com.beihui.market.ui.presenter.ProductCollectionPresenter;
import com.beihui.market.util.viewutils.ToastUtils;
import com.beihui.market.view.StateLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class PageCollectionProductFragment extends BaseComponentFragment implements ProductCollectionContract.View {

    @BindView(R.id.state_layout)
    StateLayout stateLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Inject
    ProductCollectionPresenter presenter;

    private LoanRVAdapter adapter;

    @Override
    public int getLayoutResId() {
        return R.layout.pager_item_collection_product;
    }

    @Override
    public void configViews() {
        adapter = new LoanRVAdapter();
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                presenter.loadCollection();
            }
        }, recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void initDatas() {
        presenter.loadCollection();
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerProductCollectionComponent.builder()
                .appComponent(appComponent)
                .productCollectionModule(new ProductCollectionModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(ProductCollectionContract.Presenter presenter) {
        //
    }

    @Override
    public void showProductCollection(List<LoanProduct.Row> list) {
        if (isAdded()) {
            if (adapter != null) {
                adapter.notifyLoanProductChanged(list);

                if (adapter.isLoading()) {
                    adapter.loadMoreComplete();
                }
            }
        }
    }

    @Override
    public void showDeleteCollectionSuccess(String msg) {
        if (msg != null) {
            ToastUtils.showShort(getContext(), msg, null);
        }
    }
}
