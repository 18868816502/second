package com.beihui.market.ui.presenter;

import com.beihui.market.api.Api;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.ui.contract.PersonalCenterContact;

import javax.inject.Inject;

/**
 * @name loanmarket
 * @class name：com.beihui.market.ui.presenter
 * @class describe
 * @anthor A
 * @time 2018/9/11 17:19
 */
public class PersonalCenterPresenter extends BaseRxPresenter implements PersonalCenterContact.Presenter {

    private Api api;
    private PersonalCenterContact.View view;

    @Inject
    PersonalCenterPresenter(Api api, PersonalCenterContact.View view) {
        this.api = api;
        this.view = view;
    }

    @Override
    public void fetchPersonalData() {

    }

    @Override
    public void fetchPersonalArticle() {

    }
}
