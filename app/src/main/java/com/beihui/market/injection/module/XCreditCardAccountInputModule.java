package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.XCreditCardAccountInputContract;

import dagger.Module;
import dagger.Provides;

/**
 * @author xhb
 * 添加信用卡账单
 */
@Module
public class XCreditCardAccountInputModule {

    private XCreditCardAccountInputContract.View view;

    public XCreditCardAccountInputModule(XCreditCardAccountInputContract.View view) {
        this.view = view;
    }

    @Provides
    public XCreditCardAccountInputContract.View provideView() {
        return view;
    }
}
