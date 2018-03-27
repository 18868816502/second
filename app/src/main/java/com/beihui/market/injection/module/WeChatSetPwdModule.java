package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.WeChatSetPwdContract;

import dagger.Module;
import dagger.Provides;

@Module
public class WeChatSetPwdModule {

    private WeChatSetPwdContract.View view;

    public WeChatSetPwdModule(WeChatSetPwdContract.View view) {
        this.view = view;
    }

    @Provides
    public WeChatSetPwdContract.View provideView() {
        return view;
    }
}
