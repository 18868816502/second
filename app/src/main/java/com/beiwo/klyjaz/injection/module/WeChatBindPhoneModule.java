package com.beiwo.klyjaz.injection.module;


import com.beiwo.klyjaz.ui.contract.WeChatBindPhoneContract;

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
