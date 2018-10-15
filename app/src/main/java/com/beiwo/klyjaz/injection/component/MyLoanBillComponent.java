package com.beiwo.klyjaz.injection.component;


import com.beiwo.klyjaz.injection.module.MyLoanBillModule;
import com.beiwo.klyjaz.ui.fragment.MyCreditCardDebtListFragment;
import com.beiwo.klyjaz.ui.fragment.MyFastDebtListFragment;
import com.beiwo.klyjaz.ui.fragment.MyLoanDebtListFragment;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = MyLoanBillModule.class)
public interface MyLoanBillComponent {

    void inject(MyLoanDebtListFragment fragment);

    void inject(MyCreditCardDebtListFragment fragment);

    void inject(MyFastDebtListFragment fragment);
}
