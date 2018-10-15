package com.beiwo.klyjaz.injection.component;

import com.beiwo.klyjaz.injection.module.RemindModule;
import com.beiwo.klyjaz.ui.activity.RemindActivity;

import dagger.Component;

/**
 * Copyright: kaola (C)2018
 * FileName: RemindComponent
 * Author: jiang
 * Create on: 2018/7/18 16:09
 * Description: TODO
 */


@Component(dependencies = AppComponent.class, modules = RemindModule.class)
public interface RemindComponent {
    void inject(RemindActivity activity);
}
