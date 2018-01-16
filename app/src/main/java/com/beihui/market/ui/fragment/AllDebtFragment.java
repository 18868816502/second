package com.beihui.market.ui.fragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.entity.Debt;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerAllDebtComponent;
import com.beihui.market.injection.module.AllDebtModule;
import com.beihui.market.ui.activity.AllDebtActivity;
import com.beihui.market.ui.adapter.AllDebtRVAdapter;
import com.beihui.market.ui.contract.AllDebtContract;
import com.beihui.market.ui.presenter.AllDebtPresenter;
import com.beihui.market.ui.rvdecoration.AllDebtItemDeco;
import com.beihui.market.ui.rvdecoration.AllDebtStickyHeaderItemDeco;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static com.beihui.market.util.CommonUtils.keep2digits;

public class AllDebtFragment extends BaseComponentFragment implements AllDebtContract.View {

    public static final int DEBT_STATUS_IN = 1;
    public static final int DEBT_STATUS_OFF = 2;
    public static final int DEBT_STATUS_ALL = 3;


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

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
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_all_debt;
    }

    @Override
    public void configViews() {
        adapter = new AllDebtRVAdapter(getArguments().getInt("debt_status") == AllDebtContract.Presenter.STATUS_ALL);
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
        presenter.loadDebts();
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
                ((AllDebtActivity) getActivity()).updateBottomInfo(count + "", keep2digits(debtAmount), keep2digits(capitalAmount), keep2digits(interestAmount));
            }
        }
    }

    @Override
    public void showDebts(List<Debt> list) {
        if (isAdded()) {
            adapter.notifyDebtChanged(list);
        }
    }

    @Override
    public void navigateDebtDetail(Debt debt) {
    }
}