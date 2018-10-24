package com.beiwo.klyjaz.injection.module;

import com.beiwo.klyjaz.ui.contract.ArticleDetailContact;

import dagger.Module;
import dagger.Provides;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beihui.market.injection.module
 * @class describe
 * @time 2018/9/12 18:57
 */
@Module
public class ArticleDetailModule {
    private ArticleDetailContact.View view;

    public ArticleDetailModule(ArticleDetailContact.View view) {
        this.view = view;
    }

    @Provides
    public ArticleDetailContact.View provideView() {
        return view;
    }
}