package com.beihui.market.component;

import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.module.ApiModule;
import com.beihui.market.module.AppModule;

import dagger.Component;

/** Component(组件)
 * Created by Administrator on 2016/11/7.
 */
@Component(modules = {AppModule.class, ApiModule.class})
public interface AppComponent {

    Context getContext();

    Api getApi();

}
