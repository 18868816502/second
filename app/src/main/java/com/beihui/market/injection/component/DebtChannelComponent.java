package com.beihui.market.injection.component;


import com.beihui.market.injection.module.DebtChannelModule;
import com.beihui.market.ui.activity.DebtChannelActivity;
import com.beihui.market.ui.fragment.AccountFlowLoanFragment;

import dagger.Component;

/**
 * @author xhb
 * 网贷记账
 */
@Component(dependencies = AppComponent.class, modules = DebtChannelModule.class)
public interface DebtChannelComponent {

    void inject(DebtChannelActivity activity);

    void inject(AccountFlowLoanFragment flowLoanFragment);
}
