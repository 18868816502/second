package com.beihui.market.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseTabFragment;
import com.beihui.market.entity.LoanProduct;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerTabLoanComponent;
import com.beihui.market.injection.module.TabLoanModule;
import com.beihui.market.ui.activity.LoanDetailActivity;
import com.beihui.market.ui.adapter.LoanRVAdapter;
import com.beihui.market.ui.contract.TabLoanContract;
import com.beihui.market.ui.dialog.MoneyFilterPopup;
import com.beihui.market.ui.dialog.ProFilterPopup;
import com.beihui.market.ui.dialog.TimeFilterPopup;
import com.beihui.market.ui.presenter.TabLoanPresenter;
import com.beihui.market.ui.rvdecoration.LoanItemDeco;
import com.beihui.market.util.viewutils.ToastUtils;
import com.beihui.market.view.CommStateViewProvider;
import com.beihui.market.view.StateLayout;
import com.beihui.market.view.drawable.BlurDrawable;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class TabLoanFragment extends BaseTabFragment implements TabLoanContract.View, MoneyFilterPopup.onBrMoneyListener,
        TimeFilterPopup.onBrTimeListener, ProFilterPopup.onBrZhiyeListener {

    @BindView(R.id.filter_container)
    LinearLayout filterContainer;
    @BindView(R.id.money_filter_text)
    TextView moneyFilterText;
    @BindView(R.id.money_filter_image)
    ImageView moneyFilterImage;
    @BindView(R.id.money_filter_content)
    TextView moneyFilterContent;
    @BindView(R.id.time_filter_text)
    TextView timeFilterText;
    @BindView(R.id.time_filter_image)
    ImageView timeFilterImage;
    @BindView(R.id.time_filter_content)
    TextView timeFilterContent;
    @BindView(R.id.pro_filter_text)
    TextView proFilterText;
    @BindView(R.id.pro_filter_image)
    ImageView proFilterImage;
    @BindView(R.id.pro_filter_content)
    TextView proFilterContent;

    @BindView(R.id.state_layout)
    StateLayout stateLayout;
    @BindView(R.id.loan_container)
    FrameLayout loanContainer;
    @BindView(R.id.blur_view)
    View blurView;
    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @Inject
    TabLoanPresenter presenter;

    private int pendingAmount = -1;

    private LoanRVAdapter loanRVAdapter;

    public static TabLoanFragment newInstance() {
        return new TabLoanFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_tab_loan;
    }

    @Override
    public void configViews() {
        stateLayout.setStateViewProvider(new CommStateViewProvider(getContext(),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.refresh();
                    }
                }));
        blurView.setBackgroundDrawable(new BlurDrawable(getContext(), loanContainer));
        loanRVAdapter = new LoanRVAdapter();
        loanRVAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), LoanDetailActivity.class);
                intent.putExtra("loan", (LoanProduct.Row) adapter.getItem(position));
                startActivity(intent);
            }
        });
        loanRVAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                presenter.loadMore();
            }
        }, recycleView);

        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleView.setAdapter(loanRVAdapter);
        recycleView.addItemDecoration(new LoanItemDeco());

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refresh();
            }
        });
    }

    @Override
    public void initDatas() {
        presenter.onStart();
        if (pendingAmount != -1) {
            presenter.filterAmount(pendingAmount);
            pendingAmount = -1;
        }
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerTabLoanComponent.builder()
                .appComponent(appComponent)
                .tabLoanModule(new TabLoanModule(this))
                .build()
                .inject(this);
    }


    @Override
    public void onMoneyItemClick(int money) {
        presenter.filterAmount(money);
    }

    @Override
    public void onTimeItemClick(int selectTimeIndex) {
        presenter.filterDueTime(selectTimeIndex);
    }

    @Override
    public void onZhiyeItemClick(int selectIndex) {
        presenter.filterPro(selectIndex);
    }


    @OnClick({R.id.money_filter, R.id.time_filter, R.id.pro_filter})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.money_filter:
                MoneyFilterPopup moneyFilterPopup = new MoneyFilterPopup(getActivity(), presenter.getFilterAmount(), blurView, moneyFilterText, moneyFilterImage);
                moneyFilterPopup.setShareItemListener(this);
                moneyFilterPopup.showAsDropDown(filterContainer);
                break;
            case R.id.time_filter:
                TimeFilterPopup timeFilterPopup = new TimeFilterPopup(getActivity(), presenter.getFilterDueTimeSelected(),
                        blurView, timeFilterText, timeFilterImage, presenter.getFilterDueTime());
                timeFilterPopup.setShareItemListener(this);
                timeFilterPopup.showAsDropDown(filterContainer);
                break;
            case R.id.pro_filter:
                ProFilterPopup proFilterPopup = new ProFilterPopup(getActivity(), blurView, proFilterText, proFilterImage);
                proFilterPopup.setShareItemListener(this);
                proFilterPopup.showAsDropDown(filterContainer);
                break;
        }
    }

    public void setQueryMoney(int queryMoney) {
        this.pendingAmount = queryMoney;
    }

    @Override
    public void setPresenter(TabLoanContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showFilters(String amount, String dueTime, String pro) {
        Log.e("e", "amount " + amount + " dueTime " + dueTime + " pro " + pro);
        moneyFilterContent.setText(amount);
        timeFilterContent.setText(dueTime);
        proFilterContent.setText(pro);
    }

    @Override
    public void showNetError() {
        stateLayout.switchState(StateLayout.STATE_NET_ERROR);
    }

    @Override
    public void showNoLoanProduct() {
        stateLayout.switchState(StateLayout.STATE_EMPTY);
    }

    @Override
    public void showLoanProduct(List<LoanProduct.Row> list, boolean enableLoadMore) {
        if (loanRVAdapter.isLoading()) {
            loanRVAdapter.loadMoreComplete();
        }
        loanRVAdapter.setEnableLoadMore(enableLoadMore);
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        stateLayout.switchState(StateLayout.STATE_CONTENT);
        loanRVAdapter.notifyLoanProductChanged(list);
    }

    @Override
    public void showNoMoreLoanProduct() {
        loanRVAdapter.setEnableLoadMore(false);
        loanRVAdapter.loadMoreComplete();
    }

    @Override
    public void showErrorMsg(String msg) {
        super.showErrorMsg(msg);
        if (loanRVAdapter.isLoading()) {
            loanRVAdapter.loadMoreComplete();
        }
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }
}
