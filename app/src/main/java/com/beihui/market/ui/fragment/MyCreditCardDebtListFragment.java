package com.beihui.market.ui.fragment;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
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
import com.beihui.market.view.StateLayout;
import com.beihui.market.view.stateprovider.DebtStateProvider;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;

public class MyCreditCardDebtListFragment extends BaseComponentFragment implements MyLoanBillContract.View {

    private final int billType = 2;

    @BindView(R.id.state_layout)
    StateLayout stateLayout;
//    @BindView(R.id.debt_num)
//    TextView tvDebtNum;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.ll_fg_credit_card_root)
    LinearLayout mRoot;

    @Inject
    MyLoanBillPresenter presenter;

    private MyLoanBillDebtAdapter adapter;

    @Override
    public void onDestroyView() {
        presenter.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1 && data != null) {
            presenter.debtDeleted(data.getStringExtra("deleted_id"));
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_my_credit_card_list;
    }

    @Override
    public void configViews() {
        stateLayout.setStateViewProvider(new DebtStateProvider());
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
                ((SwipeMenuLayout) adapter.getViewByPosition(position, R.id.swipe_menu_layout)).quickClose();
                if (view.getId() == R.id.content_container) {
                    presenter.clickLoanBill(position);
                }

//                else if (view.getId() == R.id.hide_show) {
//                    presenter.clickShowHideDebt(position);
//                }
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
//        tvDebtNum.setText(String.format(Locale.CHINA, "您总共有%d个信用卡项目", count));
    }


    @Override
    public void showLoanBill(List<LoanBill.Row> list, boolean canLoadMore) {
        if (adapter.isLoading()) {
            adapter.loadMoreComplete();
        }
        if (list.size() > 0) {
            mRoot.setVisibility(View.VISIBLE);
            adapter.setEnableLoadMore(canLoadMore);
            adapter.notifyLoanBillChanged(list);

            stateLayout.switchState(list.size() > 0 ? StateLayout.STATE_CONTENT : StateLayout.STATE_EMPTY);
        } else {
            mRoot.setVisibility(View.GONE);
        }

    }

    @Override
    public void navigateLoanDebtDetail(LoanBill.Row bill) {
        //
    }

    @Override
    public void navigateBillDebtDetail(LoanBill.Row bill) {
        Intent intent = new Intent(getContext(), CreditCardDebtDetailActivity.class);
        CreditCardDebtDetailActivity.putExtra(intent, bill.getRecordId(), bill.getBillId(), bill.getCardSource() == 3, bill.getBankName(), bill.getCardNums(), bill.getLogo());
        startActivityForResult(intent, 1);
    }

    @Override
    public void navigateFastDebtDetail(LoanBill.Row loanBill) {
        //
    }
}
