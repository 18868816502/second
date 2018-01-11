package com.beihui.market.injection.component;


import com.beihui.market.entity.PayPlan;
import com.beihui.market.injection.module.ConfirmPayPlanModule;
import com.beihui.market.ui.activity.ConfirmPayPlanActivity;

import dagger.BindsInstance;
import dagger.Component;

@Component(dependencies = AppComponent.class, modules = ConfirmPayPlanModule.class)
public interface ConfirmPayPlanComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder payPlan(PayPlan payPlan);

        Builder appComponent(AppComponent appComponent);

        Builder ConfirmPayPlanModule(ConfirmPayPlanModule module);

        ConfirmPayPlanComponent build();
    }

    void inject(ConfirmPayPlanActivity activity);
}
