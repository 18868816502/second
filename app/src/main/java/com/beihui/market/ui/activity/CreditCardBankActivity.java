package com.beihui.market.ui.activity;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.CreditCardBank;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerCreditCardBankComponent;
import com.beihui.market.injection.module.CreditCardBankListModule;
import com.beihui.market.ui.adapter.CreditCardBankAdapter;
import com.beihui.market.ui.contract.CreditCardBankContract;
import com.beihui.market.ui.presenter.CreditCardBankListPresenter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class CreditCardBankActivity extends BaseComponentActivity implements CreditCardBankContract.View {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private CreditCardBankAdapter adapter;

    @Inject
    CreditCardBankListPresenter presenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_credit_card_bank_list;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
        setupToolbarBackNavigation(toolbar, R.mipmap.left_arrow_black);

        adapter = new CreditCardBankAdapter();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                presenter.clickBank(position);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void initDatas() {
        presenter.fetchCreditCardBankList();
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerCreditCardBankComponent.builder()
                .appComponent(appComponent)
                .creditCardBankListModule(new CreditCardBankListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(CreditCardBankContract.Presenter presenter) {
        //
    }

    @Override
    public void showCreditCardBankList(List<CreditCardBank> list) {
        adapter.notifyCreditCardBankChanged(list);
    }

    @Override
    public void navigateCreditCardDebtNew(CreditCardBank bank) {
        Intent intent = new Intent();
        intent.putExtra("bank", bank);
        setResult(RESULT_OK, intent);
        finish();
    }
}
