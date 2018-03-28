package com.beihui.market.ui.activity;


import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.DebtChannel;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerDebtSourceComponent;
import com.beihui.market.injection.module.DebtSourceModule;
import com.beihui.market.ui.adapter.DebtSourceChannelAdapter;
import com.beihui.market.ui.contract.DebtSourceContract;
import com.beihui.market.ui.presenter.DebtSourcePresenter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class DebtSourceActivity extends BaseComponentActivity implements DebtSourceContract.View {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.loan_debt_block)
    LinearLayout llLoanDebtBlock;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private DebtSourceChannelAdapter adapter;

    @Inject
    DebtSourcePresenter presenter;


    private boolean onlyCreditCard;

    @Override
    public int getLayoutId() {
        return R.layout.activity_debt_source;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
        setupToolbarBackNavigation(toolbar, R.mipmap.left_arrow_black);

        adapter = new DebtSourceChannelAdapter();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                presenter.clickSourceChannel(position);
            }
        });

        onlyCreditCard = getIntent().getBooleanExtra("only_credit_card", false);
        if (onlyCreditCard) {
            llLoanDebtBlock.setVisibility(View.GONE);
        }
    }

    @Override
    public void initDatas() {
        if (!onlyCreditCard) {
            presenter.fetchSourceChannel();
        }
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerDebtSourceComponent.builder()
                .appComponent(appComponent)
                .debtSourceModule(new DebtSourceModule(this))
                .build()
                .inject(this);
    }

    @OnClick({R.id.fetch_debt_with_mail, R.id.fetch_debt_with_visa, R.id.add_debt_by_hand, R.id.more_debt_source_channel})
    void onItemClicked(View view) {
        switch (view.getId()) {
            case R.id.fetch_debt_with_mail:
                presenter.clickFetchDebtWithMail();
                break;
            case R.id.fetch_debt_with_visa:
                presenter.clickFetchDebtWithVisa();
                break;
            case R.id.add_debt_by_hand:
                presenter.clickAddDebtByHand();
                break;
            case R.id.more_debt_source_channel:
                presenter.clickMoreSourceChannel();
                break;
        }
    }

    @Override
    public void setPresenter(DebtSourceContract.Presenter presenter) {
        //
    }

    @Override
    public void showSourceChannel(List<DebtChannel> list) {
        adapter.notifyDebtChannelChanged(list);
    }

    @Override
    public void navigateDebtMail() {
        startActivity(new Intent(this, UsedEmailActivity.class));
    }

    @Override
    public void navigateDebtVisa() {
        startActivity(new Intent(this, EBankActivity.class));
    }

    @Override
    public void navigateDebtHand() {
        startActivity(new Intent(this, CreditCardDebtNewActivity.class));
    }

    @Override
    public void navigateMoreSourceChannel() {
        startActivity(new Intent(this, DebtChannelActivity.class));
    }

    @Override
    public void navigateDebtNew(DebtChannel channel) {
        Intent intent = new Intent(this, DebtNewActivity.class);
        intent.putExtra("debt_channel", channel);
        startActivity(intent);
    }
}
