package com.beihui.market.injection.component;


import com.beihui.market.injection.module.PagePersonalProductModule;
import com.beihui.market.ui.fragment.PagePersonalProductFragment;

import dagger.BindsInstance;
import dagger.Component;

@Component(dependencies = AppComponent.class, modules = PagePersonalProductModule.class)
public interface PagePersonalProductComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder groupId(String groupId);

        Builder appComponent(AppComponent appComponent);

        Builder pagePersonalProductModule(PagePersonalProductModule module);

        PagePersonalProductComponent build();

    }

    void inject(PagePersonalProductFragment fragment);
}
