package com.beiwo.klyjaz.injection.module;


import com.beiwo.klyjaz.ui.contract.WeChatSetPwdContract;

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