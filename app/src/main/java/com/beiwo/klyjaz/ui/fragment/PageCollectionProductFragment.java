package com.beiwo.klyjaz.ui.fragment;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.base.BaseComponentFragment;
import com.beiwo.klyjaz.entity.LoanProduct;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.injection.component.DaggerProductCollectionComponent;
import com.beiwo.klyjaz.injection.module.ProductCollectionModule;
import com.beiwo.klyjaz.ui.activity.LoanDetailActivity;
import com.beiwo.klyjaz.ui.adapter.CollectionLoanRVAdapter;
import com.beiwo.klyjaz.ui.contract.ProductCollectionContract;
import com.beiwo.klyjaz.ui.dialog.CommNoneAndroidDialog;
import com.beiwo.klyjaz.ui.presenter.ProductCollectionPresenter;
import com.beiwo.klyjaz.umeng.Events;
import com.beiwo.klyjaz.umeng.Statistic;
import com.beiwo.klyjaz.util.WeakRefToastUtil;
import com.beiwo.klyjaz.view.StateLayout;
import com.beiwo.klyjaz.view.stateprovider.CollectionStateViewProvider;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

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
    public void onDestroyView() {
        presenter.onDestroy();
        super.onDestroyView();
    }

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
                presenter.loadMoreCollection();
            }
        }, recyclerView);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                if (view.getId() == R.id.delete) {
                    ((SwipeMenuLayout) view.getParent()).smoothClose();
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
                    //umeng统计
                    Statistic.onEvent(Events.CLICK_MY_COLLECTION_ITEM);

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
            WeakRefToastUtil.showShort(getContext(), msg, null);
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
