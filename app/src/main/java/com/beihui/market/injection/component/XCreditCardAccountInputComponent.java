package com.beihui.market.injection.component;


import com.beihui.market.injection.module.XCreditCardAccountInputModule;
import com.beihui.market.ui.activity.XCreditCardAccountInputActivity;

import dagger.Component;

/**
 * @author xhb
 * 添加信用卡账单
 */
@Component(dependencies = AppComponent.class, modules = XCreditCardAccountInputModule.class)
public interface XCreditCardAccountInputComponent {

    void inject(XCreditCardAccountInputActivity activity);
}
