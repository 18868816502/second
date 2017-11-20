package com.beihui.market.ui.fragment;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.entity.LoanProduct;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerPagePersonalProductComponent;
import com.beihui.market.injection.module.PagePersonalProductModule;
import com.beihui.market.ui.activity.LoanDetailActivity;
import com.beihui.market.ui.activity.UserAuthorizationActivity;
import com.beihui.market.ui.adapter.LoanRVAdapter;
import com.beihui.market.ui.contract.PagePersonalProductContract;
import com.beihui.market.ui.presenter.PagePersonalProductPresenter;
import com.beihui.market.ui.rvdecoration.LoanItemDeco;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class PagePersonalProductFragment extends BaseComponentFragment implements PagePersonalProductContract.View {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Inject
    PagePersonalProductPresenter presenter;

    private LoanRVAdapter adapter;

    private String groupId;

    @Override
    public int getLayoutResId() {
        return R.layout.pager_item_personal_product;
    }

    @Override
    public void configViews() {
        adapter = new LoanRVAdapter();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                presenter.clickProduct(position);
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                presenter.loadMoreGroupProduct();
            }
        }, recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new LoanItemDeco());
    }

    @Override
    public void initDatas() {
        presenter.loadGroupProduct();
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        if (groupId == null) {
            groupId = getArguments().getString("groupId");
        }
        DaggerPagePersonalProductComponent.builder()
                .appComponent(appComponent)
                .pagePersonalProductModule(new PagePersonalProductModule(this))
                .groupId(groupId)
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(PagePersonalProductContract.Presenter presenter) {
        //
    }

    @Override
    public void showGroupProducts(List<LoanProduct.Row> products, boolean canLoadMore) {
        if (isAdded()) {
            if (adapter != null) {
                if (adapter.isLoading()) {
                    if (canLoadMore) {
                        adapter.loadMoreComplete();
                    } else {
                        adapter.loadMoreEnd(true);
                    }
                }
                adapter.notifyLoanProductChanged(products);
            }
        }
    }

    @Override
    public void navigateLogin() {
        UserAuthorizationActivity.launch(getActivity(), null);
    }

    @Override
    public void navigateProductDetail(LoanProduct.Row loan) {
        Intent intent = new Intent(getContext(), LoanDetailActivity.class);
        intent.putExtra("loan", loan);
        startActivity(intent);
    }
}
