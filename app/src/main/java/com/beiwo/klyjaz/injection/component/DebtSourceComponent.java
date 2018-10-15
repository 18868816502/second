package com.beiwo.klyjaz.injection.component;


import com.beiwo.klyjaz.injection.module.DebtSourceModule;
import com.beiwo.klyjaz.ui.activity.DebtSourceActivity;

import dagger.Component;

/**
 * @author xhb
 * 添加信用卡账单
 */
@Component(dependencies = AppComponent.class, modules = DebtSourceModule.class)
public interface DebtSourceComponent {

    void inject(DebtSourceActivity activity);
}
