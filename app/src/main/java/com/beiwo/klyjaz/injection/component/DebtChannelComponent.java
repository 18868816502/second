package com.beiwo.klyjaz.injection.component;


import com.beiwo.klyjaz.injection.module.DebtChannelModule;
import com.beiwo.klyjaz.ui.activity.DebtChannelActivity;
import com.beiwo.klyjaz.ui.fragment.AccountFlowLoanFragment;

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
