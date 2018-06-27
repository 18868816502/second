package com.beihui.market.injection.component;


import com.beihui.market.injection.module.MyLoanBillModule;
import com.beihui.market.ui.fragment.MyCreditCardDebtListFragment;
import com.beihui.market.ui.fragment.MyFastDebtListFragment;
import com.beihui.market.ui.fragment.MyLoanDebtListFragment;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = MyLoanBillModule.class)
public interface MyLoanBillComponent {

    void inject(MyLoanDebtListFragment fragment);

    void inject(MyCreditCardDebtListFragment fragment);

    void inject(MyFastDebtListFragment fragment);
}
