package com.beihui.market.component;

import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.module.ApiModule;
import com.beihui.market.module.AppModule;

import dagger.Component;

@Component(modules = {AppModule.class, ApiModule.class})
public interface AppComponent {

    Context getContext();

    Api getApi();

}
