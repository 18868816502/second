package com.beiwo.klyjaz.injection.component;

import com.beiwo.klyjaz.injection.module.ArticleDetailModule;
import com.beiwo.klyjaz.ui.activity.ArticleDetailActivity;

import dagger.Component;

/**
 * @author A
 * @name loanmarket
 * @class name：com.beihui.market.injection.component
 * @class describe
 * @time 2018/9/11 17:55
 */
@Component(dependencies = AppComponent.class, modules = ArticleDetailModule.class)
public interface ArticleDetailComponent {
    void inject(ArticleDetailActivity activity);
}
