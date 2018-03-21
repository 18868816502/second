package com.beihui.market.injection.component;


import com.beihui.market.entity.DebtChannel;
import com.beihui.market.injection.module.DebtNewModule;
import com.beihui.market.ui.fragment.DebtNewEvenFragment;
import com.beihui.market.ui.fragment.DebtNewOneTimeFragment;

import dagger.BindsInstance;
import dagger.Component;

@Component(dependencies = AppComponent.class, modules = DebtNewModule.class)
public interface DebtNewComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        DebtNewComponent.Builder debtChannel(DebtChannel debtChannel);

        DebtNewComponent.Builder appComponent(AppComponent appComponent);

        DebtNewComponent.Builder debtNewModule(DebtNewModule module);

        DebtNewComponent build();
    }

    void inject(DebtNewOneTimeFragment fragment);

    void inject(DebtNewEvenFragment fragment);
}
