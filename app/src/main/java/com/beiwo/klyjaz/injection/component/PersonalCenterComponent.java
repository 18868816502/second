package com.beiwo.klyjaz.injection.component;

import com.beiwo.klyjaz.injection.module.PersonalCenterModule;
import com.beiwo.klyjaz.ui.activity.PersonalCenterActivity;

import dagger.Component;

/**
 * @name loanmarket
 * @class name：com.beihui.market.injection.component
 * @class describe
 * @anthor A
 * @time 2018/9/11 17:55
 */
@Component(dependencies = AppComponent.class, modules = PersonalCenterModule.class)
public interface PersonalCenterComponent {
    void inject(PersonalCenterActivity activity);
}