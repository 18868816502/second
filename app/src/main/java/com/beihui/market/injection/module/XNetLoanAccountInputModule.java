package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.XNetLoanAccountInputContract;

import dagger.Module;
import dagger.Provides;

/**
 * @author xhb
 * 网贷记账
 */
@Module
public class XNetLoanAccountInputModule {

    private XNetLoanAccountInputContract.View view;

    public XNetLoanAccountInputModule(XNetLoanAccountInputContract.View view) {
        this.view = view;
    }

    @Provides
    public XNetLoanAccountInputContract.View providesDebtChannelContractView() {
        return view;
    }
}
