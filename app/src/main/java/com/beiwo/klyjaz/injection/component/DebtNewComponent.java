package com.beiwo.klyjaz.injection.component;


import com.beiwo.klyjaz.entity.DebtChannel;
import com.beiwo.klyjaz.injection.module.DebtNewModule;
import com.beiwo.klyjaz.ui.fragment.DebtNewEvenFragment;
import com.beiwo.klyjaz.ui.fragment.DebtNewOneTimeFragment;

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
