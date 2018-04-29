package com.beihui.market.injection.component;


import com.beihui.market.injection.module.DebtSourceModule;
import com.beihui.market.ui.activity.DebtSourceActivity;

import dagger.Component;

/**
 * @author xhb
 * 添加信用卡账单
 */
@Component(dependencies = AppComponent.class, modules = DebtSourceModule.class)
public interface DebtSourceComponent {

    void inject(DebtSourceActivity activity);
}
