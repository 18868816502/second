package com.beihui.market.ui.fragment;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.entity.LoanProduct;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerProductCollectionComponent;
import com.beihui.market.injection.module.ProductCollectionModule;
import com.beihui.market.ui.activity.LoanDetailActivity;
import com.beihui.market.ui.adapter.CollectionLoanRVAdapter;
import com.beihui.market.ui.contract.ProductCollectionContract;
import com.beihui.market.ui.dialog.CommNoneAndroidDialog;
import com.beihui.market.ui.presenter.ProductCollectionPresenter;
import com.beihui.market.util.viewutils.ToastUtils;
import com.beihui.market.view.StateLayout;
import com.beihui.market.view.stateprovider.CollectionStateViewProvider;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.daimajia.swipe.SwipeLayout;

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

    private CollectionLoanRVAdapter adapter;

    @Override
    public int getLayoutResId() {
        return R.layout.pager_item_collection_product;
    }

    @Override
    public void configViews() {
        stateLayout.setStateViewProvider(new CollectionStateViewProvider());
        adapter = new CollectionLoanRVAdapter();
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                presenter.loadCollection();
            }
        }, recyclerView);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                if (view.getId() == R.id.delete) {
                    ((SwipeLayout) view.getParent()).close(true);
                    new CommNoneAndroidDialog().withMessage("确认删除收藏")
                            .withNegativeBtn("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    presenter.deleteCollection(position);
                                }
                            })
                            .withPositiveBtn("取消", null)
                            .show(getChildFragmentManager(), "ConfirmDelete");
                } else if (view.getId() == R.id.base_container) {
                    presenter.clickCollection(position);
                }
            }
        });
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
    public void showProductCollection(List<LoanProduct.Row> list, boolean canLoadMore) {
        if (isAdded()) {
            stateLayout.switchState(StateLayout.STATE_CONTENT);
            if (adapter != null) {
                if (adapter.isLoading()) {
                    if (canLoadMore) {
                        adapter.loadMoreComplete();
                    } else {
                        adapter.loadMoreEnd(true);
                    }
                }
                adapter.notifyLoanProductChanged(list);
            }
        }
    }

    @Override
    public void showDeleteCollectionSuccess(String msg) {
        if (msg != null) {
            ToastUtils.showShort(getContext(), msg, null);
        }
    }

    @Override
    public void showNoCollection() {
        if (isAdded()) {
            stateLayout.switchState(StateLayout.STATE_EMPTY);
        }
    }

    @Override
    public void navigateLoanDetail(LoanProduct.Row loan) {
        Intent toDetail = new Intent(getContext(), LoanDetailActivity.class);
        toDetail.putExtra("loan", loan);
        startActivity(toDetail);
    }
}
