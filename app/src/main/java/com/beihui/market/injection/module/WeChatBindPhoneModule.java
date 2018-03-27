package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.WeChatBindPhoneContract;

import dagger.Module;
import dagger.Provides;

@Module
public class WeChatBindPhoneModule {

    private WeChatBindPhoneContract.View view;

    public WeChatBindPhoneModule(WeChatBindPhoneContract.View view) {
        this.view = view;
    }

    @Provides
    public WeChatBindPhoneContract.View provideView() {
        return view;
    }
}
