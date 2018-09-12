package com.beihui.market.injection.component;

import com.beihui.market.injection.module.ArticleDetailModule;
import com.beihui.market.injection.module.PersonalCenterModule;
import com.beihui.market.ui.activity.ArticleDetailActivity;
import com.beihui.market.ui.activity.PersonalCenterActivity;

import dagger.Component;

/**
 * @name loanmarket
 * @class name：com.beihui.market.injection.component
 * @class describe
 * @author A
 * @time 2018/9/11 17:55
 */
@Component(dependencies = AppComponent.class, modules = ArticleDetailModule.class)
public interface ArticleDetailComponent {
    void inject(ArticleDetailActivity activity);
}
