package com.beihui.market.ui.activity;


import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.DebtChannel;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.NutEmailLeadInListener;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerDebtSourceComponent;
import com.beihui.market.injection.module.DebtSourceModule;
import com.beihui.market.ui.contract.DebtSourceContract;
import com.beihui.market.ui.presenter.DebtSourcePresenter;
import com.gyf.barlibrary.ImmersionBar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xhb
 * 添加信用卡账单
 */
public class DebtSourceActivity extends BaseComponentActivity implements DebtSourceContract.View {

    @BindView(R.id.tl_credit_card_input_account_bar)
    Toolbar toolbar;

    @Inject
    DebtSourcePresenter presenter;


    private boolean onlyCreditCard;

    @Override
    public int getLayoutId() {
        return R.layout.xlayout_ac_credit_card_account_input;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
        setupToolbarBackNavigation(toolbar, R.mipmap.left_arrow_black);


        onlyCreditCard = getIntent().getBooleanExtra("only_credit_card", false);
        if (onlyCreditCard) {
        }
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerDebtSourceComponent.builder()
                .appComponent(appComponent)
                .debtSourceModule(new DebtSourceModule(this))
                .build()
                .inject(this);
    }

    @OnClick({R.id.credit_card_input_account_email, R.id.credit_card_input_account_net_bank, R.id.credit_card_input_account_hand})
    public void onClick(View view) {
        switch (view.getId()) {
            //邮箱导入
            case R.id.credit_card_input_account_email:
                if (NutEmailLeadInListener.getInstance().hasUnFinishedTask()) {
                    //如果当前已有进行中的任务，则直接进入到进度页
                    Intent intent = new Intent(this, EmailLeadingInProgressActivity.class);
                    intent.putExtra("email_symbol", NutEmailLeadInListener.getInstance().getCurTaskEmailSymbol());
                    startActivity(intent);
                } else {
                    //pv，uv统计
                    DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_BILL_CLICK_EMAIL_LEAD_IN);

                    presenter.clickFetchDebtWithMail();
                }
                break;
            //网银导入
            case R.id.credit_card_input_account_net_bank:
                presenter.clickFetchDebtWithVisa();
                break;
            //手动导入
            case R.id.credit_card_input_account_hand:
                //pv，uv统计

                presenter.clickAddDebtByHand();
                break;
        }
    }

    @Override
    public void setPresenter(DebtSourceContract.Presenter presenter) {
        //
    }

    @Override
    public void showSourceChannel(List<DebtChannel> list) {
    }

    /**
     * 进入手动记账的页面
     */
    @Override
    public void navigateDebtHand() {
        startActivity(new Intent(this, CreditCardDebtNewActivity.class));
    }

    @Override
    public void navigateUsedMail() {
        startActivity(new Intent(this, UsedEmailActivity.class));
    }

    @Override
    public void navigateNutMail() {
        startActivity(new Intent(this, NutEmailActivity.class));
    }


    /**
     * 进入 网银导入页面
     */
    @Override
    public void navigateDebtVisa() {
        startActivity(new Intent(this, EBankActivity.class));
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
