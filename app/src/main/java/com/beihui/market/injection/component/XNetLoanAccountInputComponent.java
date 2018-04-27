package com.beihui.market.injection.component;


import com.beihui.market.injection.module.XNetLoanAccountInputModule;
import com.beihui.market.ui.activity.XNetLoanAccountInputActivity;

import dagger.Component;

/**
 * @author xhb
 * 网贷记账
 */
@Component(dependencies = AppComponent.class, modules = XNetLoanAccountInputModule.class)
public interface XNetLoanAccountInputComponent {

    void inject(XNetLoanAccountInputActivity activity);
}
