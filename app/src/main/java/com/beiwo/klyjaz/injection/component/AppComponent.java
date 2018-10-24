package com.beiwo.klyjaz.injection.component;

import android.content.Context;

import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.injection.module.ApiModule;
import com.beiwo.klyjaz.injection.module.AppModule;

import dagger.Component;

@Component(modules = {AppModule.class, ApiModule.class})
public interface AppComponent {
    Context getContext();

    Api getApi();
}