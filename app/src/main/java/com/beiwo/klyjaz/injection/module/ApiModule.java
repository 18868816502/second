package com.beiwo.klyjaz.injection.module;


import com.beiwo.klyjaz.api.Api;

import dagger.Module;
import dagger.Provides;

@Module
public class ApiModule {

    @Provides
    protected Api provideApi() {
        return Api.getInstance();
    }

}
