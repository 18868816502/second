package com.beihui.market.ui.fragment;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.entity.LoanBill;
import com.beihui.market.event.MyLoanDebtListFragmentEvent;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerMyLoanBillComponent;
import com.beihui.market.injection.module.MyLoanBillModule;
import com.beihui.market.ui.activity.LoanDebtDetailActivity;
import com.beihui.market.ui.activity.MyDebtActivity;
import com.beihui.market.ui.adapter.MyLoanBillDebtAdapter;
import com.beihui.market.ui.contract.MyLoanBillContract;
import com.beihui.market.ui.presenter.MyLoanBillPresenter;
import com.beihui.market.ui.rvdecoration.CommVerItemDeco;
import com.beihui.market.util.FastClickUtils;
import com.beihui.market.view.StateLayout;
import com.beihui.market.view.stateprovider.DebtStateProvider;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @author xhb
 * 我的模块 我的账单 网贷列表
 */
public class MyLoanDebtListFragment extends BaseComponentFragment implements MyLoanBillContract.View {

    private final int billType = 1;

    @BindView(R.id.state_layout)
    StateLayout stateLayout;
//    @BindView(R.id.debt_num)
//    TextView tvDebtNum;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.ll_fg_loan_debt_root)
    LinearLayout mRoot;

    @Inject
    MyLoanBillPresenter presenter;

    private MyLoanBillDebtAdapter adapter;

    public int mPosition = 0;


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMainEvent(MyLoanDebtListFragmentEvent event){
        if (event.type != 1) {
            return;
        }
        if (((MyLoanBillPresenter)presenter).loanCurPage > 1 ) {
            ((MyLoanBillPresenter) presenter).loanCurPage = 1;
            int size = ((MyLoanBillPresenter) presenter).loanBillList.size();
            if (size > 0) {
                ((MyLoanBillPresenter) presenter).loanBillList.clear();
            }
            presenter.fetchLoanBill(billType);
        }
    }

    @Override
    public void onDestroyView() {
        presenter.onDestroy();
        super.onDestroyView();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1 && resultCode == Activity.RESULT_OK & data != null) {
//            presenter.debtDeleted(data.getStringExtra("deleteDebtId"));
//            }
    }


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_my_loan_debt_list;
    }

    @Override
    public void configViews() {
        mPosition = ((MyDebtActivity)getActivity()).mPosition;

        stateLayout.setStateViewProvider(new DebtStateProvider());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyLoanBillDebtAdapter(R.layout.rv_item_loan_debt, billType);
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

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

    }

    @Override
    public void initDatas() {
        ((MyLoanBillPresenter) presenter).loanCurPage = 1;
        int size = ((MyLoanBillPresenter) presenter).loanBillList.size();
        if (size > 0) {
            ((MyLoanBillPresenter) presenter).loanBillList.clear();
        }
        presenter.fetchLoanBill(billType);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
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
//        tvDebtNum.setText(String.format(Locale.CHINA, "您总共有%d个网贷项目", count));
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

    /**
     * 跳转到网贷账单详情
     * @param bill 账单
     */
    @Override
    public void navigateLoanDebtDetail(LoanBill.Row bill) {
        if (FastClickUtils.isFastClick()) {
            return;
        }
        Intent intent = new Intent(getContext(), LoanDebtDetailActivity.class);
        intent.putExtra("debt_id", bill.getRecordId());
        intent.putExtra("bill_id", bill.getBillId());
        startActivityForResult(intent, 1);
    }

    @Override
    public void navigateBillDebtDetail(LoanBill.Row bill) {
        //
    }

    @Override
    public void navigateFastDebtDetail(LoanBill.Row loanBill) {
//        Intent intent = new Intent(getContext(), FastDebtDetailActivity.class);
//        intent.putExtra("debt_id", loanBill.getRecordId());
//        intent.putExtra("bill_id", loanBill.getBillId());
//        startActivityForResult(intent, 1);
    }
}
