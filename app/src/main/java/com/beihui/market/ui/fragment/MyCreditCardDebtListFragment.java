package com.beihui.market.ui.fragment;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.entity.LoanBill;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerMyLoanBillComponent;
import com.beihui.market.injection.module.MyLoanBillModule;
import com.beihui.market.ui.activity.CreditCardDebtDetailActivity;
import com.beihui.market.ui.adapter.MyLoanBillDebtAdapter;
import com.beihui.market.ui.contract.MyLoanBillContract;
import com.beihui.market.ui.presenter.MyLoanBillPresenter;
import com.beihui.market.ui.rvdecoration.CommVerItemDeco;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;

public class MyCreditCardDebtListFragment extends BaseComponentFragment implements MyLoanBillContract.View {

    private final int billType = 2;

    @BindView(R.id.debt_num)
    TextView tvDebtNum;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Inject
    MyLoanBillPresenter presenter;

    private MyLoanBillDebtAdapter adapter;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_my_credit_card_list;
    }

    @Override
    public void configViews() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyLoanBillDebtAdapter(R.layout.rv_item_my_credit_card_debt, billType);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                presenter.fetchLoanBill(billType);
            }
        }, recyclerView);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.content_container) {
                    presenter.clickLoanBill(position);
                } else if (view.getId() == R.id.hide_show) {
                    presenter.clickShowHideDebt(position);
                }
            }
        });
        recyclerView.setAdapter(adapter);
        float density = getResources().getDisplayMetrics().density;
        recyclerView.addItemDecoration(new CommVerItemDeco((int) (density * 0.5), (int) (15 * density), (int) (15 * density)));
    }

    @Override
    public void initDatas() {
        presenter.fetchLoanBill(billType);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerMyLoanBillComponent.builder()
                .appComponent(appComponent)
                .myLoanBillModule(new MyLoanBillModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(MyLoanBillContract.Presenter presenter) {
        //
    }

    @Override
    public void showLoanBillCount(int count) {
        tvDebtNum.setText(String.format(Locale.CHINA, "您总共有%d个信用卡项目", count));
    }


    @Override
    public void showLoanBill(List<LoanBill.Row> list, boolean canLoadMore) {
        if (adapter.isLoading()) {
            adapter.loadMoreComplete();
        }
        adapter.setEnableLoadMore(canLoadMore);
        adapter.notifyLoanBillChanged(list);
    }

    @Override
    public void navigateLoanDebtDetail(LoanBill.Row bill) {
        //
    }

    @Override
    public void navigateBillDebtDetail(LoanBill.Row bill) {
        CreditCardDebtDetailActivity.launchActivity(getContext(), bill.getRecordId(), bill.getCardSource() == 3, bill.getBankName(), bill.getCardNums(), bill.getLogo());
    }
}
