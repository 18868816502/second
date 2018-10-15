package com.beiwo.klyjaz.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.base.BaseComponentFragment;
import com.beiwo.klyjaz.entity.AllDebt;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.injection.component.DaggerAllDebtComponent;
import com.beiwo.klyjaz.injection.module.AllDebtModule;
import com.beiwo.klyjaz.ui.activity.AllDebtActivity;
import com.beiwo.klyjaz.ui.activity.LoanDebtDetailActivity;
import com.beiwo.klyjaz.ui.adapter.AllDebtRVAdapter;
import com.beiwo.klyjaz.ui.contract.AllDebtContract;
import com.beiwo.klyjaz.ui.presenter.AllDebtPresenter;
import com.beiwo.klyjaz.ui.rvdecoration.AllDebtItemDeco;
import com.beiwo.klyjaz.ui.rvdecoration.AllDebtStickyHeaderItemDeco;
import com.beiwo.klyjaz.util.FastClickUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static com.beiwo.klyjaz.util.CommonUtils.keep2digits;
import static com.beiwo.klyjaz.util.CommonUtils.keep2digitsWithoutZero;

public class AllDebtFragment extends BaseComponentFragment implements AllDebtContract.View {

    public static final int DEBT_STATUS_IN = 1;
    public static final int DEBT_STATUS_OFF = 2;
    public static final int DEBT_STATUS_ALL = 3;


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.no_record)
    View noRecord;

    @Inject
    AllDebtPresenter presenter;

    private AllDebtRVAdapter adapter;

    private int count;
    private double debtAmount;
    private double capitalAmount;
    private double interestAmount;

    public static AllDebtFragment newInstance(int status) {
        Bundle args = new Bundle();
        args.putInt("debt_status", status);
        AllDebtFragment fragment = new AllDebtFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (getActivity() != null) {
                ((AllDebtActivity) getActivity()).updateBottomInfo(count + "", keep2digits(debtAmount), keep2digits(capitalAmount), keep2digits(interestAmount));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadDebts();
    }

    @Override
    public void onDestroyView() {
        presenter.onDestroy();
        super.onDestroyView();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_all_debt;
    }

    @Override
    public void configViews() {
        adapter = new AllDebtRVAdapter(getArguments().getInt("debt_status") == AllDebtContract.Presenter.STATUS_ALL);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                presenter.loadMoreDebts();
            }
        }, recyclerView);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                presenter.clickDebt(position);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new AllDebtItemDeco());
        recyclerView.addItemDecoration(new AllDebtStickyHeaderItemDeco(getContext()) {
            @SuppressWarnings("ConstantConditions")
            @Override
            public String getHeaderName(int pos) {
                return adapter.getItem(pos).getStartDate();
            }
        });
    }

    @Override
    public void initDatas() {
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerAllDebtComponent.builder()
                .appComponent(appComponent)
                .allDebtModule(new AllDebtModule(this))
                .status(getArguments().getInt("debt_status"))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(AllDebtContract.Presenter presenter) {
        //
    }

    @Override
    public void showDebtInfo(int count, double debtAmount, double capitalAmount, double interestAmount) {
        this.count = count;
        this.debtAmount = debtAmount;
        this.capitalAmount = capitalAmount;
        this.interestAmount = interestAmount;
        if (getUserVisibleHint()) {
            if (getActivity() != null) {
                ((AllDebtActivity) getActivity()).updateBottomInfo(count + "", keep2digitsWithoutZero(debtAmount), keep2digitsWithoutZero(capitalAmount), keep2digitsWithoutZero(interestAmount));
            }
        }
    }

    @Override
    public void showDebts(List<AllDebt.Row> list, boolean canLoadMore) {
        if (isAdded()) {
            if (adapter.isLoading()) {
                adapter.loadMoreComplete();
            }
            adapter.setEnableLoadMore(canLoadMore);

            adapter.notifyDebtChanged(list);

            boolean hasData = list != null && list.size() > 0;
            recyclerView.setVisibility(hasData ? View.VISIBLE : View.GONE);
            noRecord.setVisibility(hasData ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * TODO  页面没有分期账单ID
     */
    @Override
    public void navigateDebtDetail(AllDebt.Row debt) {
        if (FastClickUtils.isFastClick()) {
            return;
        }
        Intent intent = new Intent(getContext(), LoanDebtDetailActivity.class);
        intent.putExtra("debt_id", debt.getId());
        startActivityForResult(intent, 1);
    }

    @Override
    public void showErrorMsg(String msg) {
        super.showErrorMsg(msg);
        if (adapter.isLoading()) {
            adapter.loadMoreComplete();
        }
        adapter.setEnableLoadMore(false);
    }
}
