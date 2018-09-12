package com.beihui.market.ui.presenter;

import com.beihui.market.api.Api;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.ui.contract.ArticleDetailContact;
import com.beihui.market.ui.contract.PersonalCenterContact;

import javax.inject.Inject;


/**
 * @name loanmarket
 * @class name：com.beihui.market.ui.presenter
 * @class describe
 * @author A
 * @time 2018/9/11 17:19
 */
public class ArticleDetailPresenter extends BaseRxPresenter implements ArticleDetailContact.Presenter {

    private Api api;
    private ArticleDetailContact.View view;

    @Inject
    ArticleDetailPresenter(Api api, ArticleDetailContact.View view) {
        this.api = api;
        this.view = view;
    }


    @Override
    public void fetchArticleDetailInfo(String userId) {

    }

    @Override
    public void fetchArticleComment() {

    }
}
