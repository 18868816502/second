package com.beihui.market.injection.component;

import com.beihui.market.injection.module.PersonalCenterModule;
import com.beihui.market.ui.activity.PersonalCenterActivity;

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
